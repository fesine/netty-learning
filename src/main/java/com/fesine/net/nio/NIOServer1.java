package com.fesine.net.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @description: NIO服务端，解决无法同时处理多个连接的问题1
 * @author: fesine
 * @createTime:2020/4/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/26
 */
public class NIOServer1 {

    private static List<SocketChannel> channelList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        //创建网络服务器
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置阻塞模式为非阻塞，所有操作系统socket默认都是阻塞的
        serverSocketChannel.configureBlocking(false);
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8081));
        System.out.println("服务端启动成功！");
        //只要服务端没有关闭
        while (true){
            //此处socketChanel可以为空，然后直接返回
            SocketChannel socketChannel = serverSocketChannel.accept();
            //tcp请求 读写、响应
            if(socketChannel != null) {
                System.out.println("收到新连接，" + socketChannel.getRemoteAddress());
                //此时连接通道也需要设置为非阻塞
                socketChannel.configureBlocking(false);
                channelList.add(socketChannel);
            }else{
                Iterator<SocketChannel> iterator = channelList.iterator();
                while (iterator.hasNext()) {
                    SocketChannel channel = iterator.next();
                    try {
                        //接收数据，打印 nio
                        ByteBuffer request = ByteBuffer.allocate(1024);
                        //表示当前通道没有数据处理，呆会再处理
                        if (channel.read(request) == 0) {
                            continue;
                        }
                        while (channel.isOpen() && channel.read(request) != -1) {
                            //长连接情况下，需要手动判断读取数据有没有结束（此处做一个简单的判断，位置超过0，即说明请求结束）
                            if (request.position() > 0) {
                                break;
                            }
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
                        iterator.remove();
                    } catch (IOException e) {
                        e.printStackTrace();
                        iterator.remove();
                    }
                }

            }


        }
    }
}
