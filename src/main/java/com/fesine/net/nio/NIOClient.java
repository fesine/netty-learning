package com.fesine.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @description: NIO客户端
 * @author: fesine
 * @createTime:2020/4/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/26
 */
public class NIOClient {
    private static Charset charset = Charset.forName("utf-8");
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8081));
        //连接没有完成连接，线程一直阻塞
        while (!socketChannel.finishConnect()) {
            Thread.yield();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine();
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
        System.out.println("收到服务器响应：");
        //接收数据，打印 nio
        ByteBuffer request = ByteBuffer.allocate(1024);
        while (socketChannel.isOpen() && socketChannel.read(request) != -1) {
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
        scanner.close();
        socketChannel.close();
    }
}
