package com.fesine.socket.aio;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @description: 异步客户端
 * @author: fesine
 * @createTime:2020/6/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/6/9
 */
public class AioClient {
    private static final Integer DEFAULT_SERVER_PORT = 8081;

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";

    AsynchronousSocketChannel clientChannel;


    /**
     * 关闭服务端socket
     */
    public synchronized void close(Closeable closeable) {
        try {
            closeable.close();
            System.out.println("关闭服务：" + closeable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            clientChannel = AsynchronousSocketChannel.open();
            Future<Void> future =
                    clientChannel.connect(new InetSocketAddress(DEFAULT_SERVER_HOST,
                            DEFAULT_SERVER_PORT));
            //等待连接成功
            future.get();
            BufferedReader consoleReader =
                    new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                //阻塞调用
                String input = consoleReader.readLine();
                //发送给服务器端
                ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
                Future<Integer> writeResult = clientChannel.write(buffer);
                //阻塞等待
                writeResult.get();
                //由写转成读
                buffer.flip();
                //再从服务器读取消息
                Future<Integer> readResult = clientChannel.read(buffer);
                readResult.get();
                String echo = new String(buffer.array());
                buffer.clear();
                System.out.println(echo);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            close(clientChannel);
        }
    }

    public static void main(String[] args) {
        new AioClient().start();
    }
}
