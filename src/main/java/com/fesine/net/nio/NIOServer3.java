package com.fesine.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 使用selector多路复用reactor线程模型实现高并发
 * @author: fesine
 * @createTime:2020/5/13
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/13
 */
public class NIOServer3 {

    /**
     * 业务处理线程
     */
    private static ExecutorService workPool = Executors.newCachedThreadPool();

    /**
     * 封装了selector.select()等事件轮询代码
     */
    abstract class ReactorThread extends Thread {
        Selector selector;

        LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

        /**
         * selector监听到事件后，调用此方法
         * @param channel
         * @throws Exception
         */
        public abstract void handler(SelectableChannel channel) throws Exception;

        private ReactorThread() throws IOException {
            selector = Selector.open();
        }

        volatile boolean running = false;

        @Override
        public void run() {
            while (running) {
                try {
                    Runnable task;
                    while ((task = taskQueue.poll()) != null) {
                        task.run();
                    }
                    selector.select(1000);

                    //查询结果
                    Set<SelectionKey> selected = selector.selectedKeys();
                    //遍历结果
                    Iterator<SelectionKey> iterator = selected.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        //移除当前事件
                        iterator.remove();
                        int ops = key.readyOps();
                        if ((ops & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || ops == 0) {
                            try {
                                SelectableChannel channel = (SelectableChannel) key.attachment();
                                channel.configureBlocking(false);
                                handler(channel);
                                // 如果关闭了,就取消这个KEY的订阅
                                if(!channel.isOpen()){
                                    key.cancel();
                                }
                            } catch (Exception e) {
                                // 如果有异常,就取消这个KEY的订阅
                                key.cancel();
                            }

                        }
                    }
                    selector.selectNow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private SelectionKey register(SelectableChannel channel) throws Exception {
            //1、为什么register要以任务的形式，让reactor线程去处理
            //2、因为线程在执行channel注册到selector的过程中，会和调用selector.select()方法的线程争同一把锁
            //3、而select()方法实在eventLoop中通过while循环调用，争抢的可能性很高，
            // 为了让register更快的执行，就放到同一个线程中处理
            FutureTask<SelectionKey> futureTask = new FutureTask<>(
                    () -> channel.register(selector, 0, channel)
            );
            taskQueue.add(futureTask);
            return futureTask.get();
        }

        private void doStart(){
            if (!running) {
                running= true;
                start();
            }
        }
    }

    private ServerSocketChannel serverSocketChannel;

    /**
     * 创建一个多线程，accept处理reactor线程（accept线程）
     */

    private ReactorThread[] mainReactorThreads = new ReactorThread[1];
    /**
     * 创建一个多线程，io处理reactor线程(I/O线程)
     */
    private ReactorThread[] subReactorThreads = new ReactorThread[8];

    private void newGroup() throws IOException {
        //创建IO线程，负责处理客户端连接以后的socketChanel的IO读写
        for (int i = 0; i < subReactorThreads.length; i++) {
            subReactorThreads[i] = new ReactorThread() {
                @Override
                public void handler(SelectableChannel channel) throws Exception {
                    //work线程只处理IO，不处理accept事件
                    SocketChannel ch = (SocketChannel) channel;
                    ByteBuffer request = ByteBuffer.allocate(1024);
                    while (ch.isOpen() && ch.read(request) != -1) {
                        //长连接情况下，需要手动判断读取数据有没有结束（此处做一个简单的判断，位置超过0，即说明请求结束）
                        if (request.position() > 0) {
                            break;
                        }
                    }
                    //如果没有数据了，则不再往后处理
                    if (request.position() == 0) {
                        return;
                    }
                    //开始读取数据,转入读模式
                    request.flip();
                    byte[] content = new byte[request.limit()];
                    request.get(content);
                    System.out.println(new String(content));
                    System.out.println("收到数据，来自：" + ch.getRemoteAddress());
                    //TODO 业务处理。。。
                    workPool.submit(()->{});
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-length:11\r\n\r\n" +
                            "Hello world";
                    //封装buffer对象
                    ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                    //当buffer对象仍然有数据时，通过socketChanel写入数据
                    while (buffer.hasRemaining()) {
                        //非阻塞
                        ch.write(buffer);
                    }

                }
            };
        }

        //创建mainReactor线程，只负责处理ServerSocketChannel
        for (int i = 0; i < mainReactorThreads.length; i++) {
            mainReactorThreads[i] = new ReactorThread() {
                AtomicInteger incr = new AtomicInteger(0);
                @Override
                public void handler(SelectableChannel channel) throws Exception {
                    ServerSocketChannel server = (ServerSocketChannel) channel;
                    //将拿到的客户端连接注册到selector
                    SocketChannel client = server.accept();
                    //设置非阻塞
                    client.configureBlocking(false);
                    //收到连接建立通知后，分发给I/O线程继续去读取数据
                    int index = incr.getAndIncrement() % subReactorThreads.length;
                    ReactorThread workEventLoop = subReactorThreads[index];
                    workEventLoop.doStart();
                    SelectionKey selectionKey = workEventLoop.register(client);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("收到新连接，" + client.getRemoteAddress());
                }
            };
        }
    }

    /**
     * 初始化channel，并且绑定一个eventLoop线程
     * @throws Exception
     */
    private void initAndRegister() throws Exception {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        int index = new Random().nextInt(mainReactorThreads.length);
        mainReactorThreads[index].doStart();
        SelectionKey selectionKey = mainReactorThreads[index].register(serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
    }

    /**
     * 绑定端口
     * @throws IOException
     */
    private void bind() throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(8081));
        System.out.println("启动完成，端口8081");
    }

    public static void main(String[] args) throws Exception {
        NIOServer3 nioServer3 = new NIOServer3();
        //创建main和sub两组线程
        nioServer3.newGroup();
        //创建serverSocketChannel
        nioServer3.initAndRegister();
        //绑定端口
        nioServer3.bind();
    }

}
