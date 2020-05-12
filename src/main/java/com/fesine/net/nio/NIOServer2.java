package com.fesine.net.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: NIO服务端，解决无法同时处理多个连接的问题，引入selector选择器
 * @author: fesine
 * @createTime:2020/4/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/26
 */
public class NIOServer2 {


    public static void main(String[] args) throws IOException {
        //创建网络服务器
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置阻塞模式为非阻塞，所有操作系统socket默认都是阻塞的
        serverSocketChannel.configureBlocking(false);
        //绑定端口
        Selector selector = Selector.open();
        //将serverSocketChannel注册到selector
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, serverSocketChannel);
        //注册感兴趣事件accept操作
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8081));
        System.out.println("服务端启动成功！");
        //只要服务端没有关闭
        while (true){
            //不再使用轮询通道，改用轮询事件，具有阻塞效果，直到有事件通知才会有返回
            selector.select();
            //获取事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历查询事件
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                //被封装的查询结果
                SelectionKey key = iterator.next();
                iterator.remove();
                //只关注read和accept事件
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.attachment();
                    //将拿到的客户端连接注册到selector
                    SocketChannel client = server.accept();
                    //设置非阻塞
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ, client);
                    System.out.println("收到新连接，" + client.getRemoteAddress());
                }

                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.attachment();
                    try {
                        //接收数据，打印 nio
                        ByteBuffer request = ByteBuffer.allocate(1024);
                        while (channel.isOpen() && channel.read(request) != -1) {
                            //长连接情况下，需要手动判断读取数据有没有结束（此处做一个简单的判断，位置超过0，即说明请求结束）
                            if (request.position() > 0) {
                                break;
                            }
                        }
                        //如果没有数据了，则不再往后处理
                        if (request.position() == 0) {
                            continue;
                        }
                        //开始读取数据,转入读模式
                        request.flip();
                        byte[] content = new byte[request.limit()];
                        request.get(content);
                        System.out.println(new String(content));
                        System.out.println("收到数据，来自：" + channel.getRemoteAddress());
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-length:11\r\n\r\n" +
                                "Hello world";
                        //封装buffer对象
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                        //当buffer对象仍然有数据时，通过socketChanel写入数据
                        while (buffer.hasRemaining()) {
                            //非阻塞
                            channel.write(buffer);
                        }
                    } catch (IOException e) {
                        //e.printStackTrace();
                        //取消事件订阅
                        key.cancel();
                    }
                }

            }
            selector.selectNow();
        }
    }
}
