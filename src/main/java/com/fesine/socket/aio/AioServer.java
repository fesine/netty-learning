package com.fesine.socket.aio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 异步通信服务端
 * @author: fesine
 * @createTime:2020/6/8
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/6/8
 */
public class AioServer {
    private static final Integer DEFAULT_SERVER_PORT = 8081;

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";

    AsynchronousServerSocketChannel serverSocketChannel;

    /**
     * 关闭服务端socket
     */
    public synchronized void close(Closeable closeable) {
        try {
            closeable.close();
            System.out.println("关闭服务："+ closeable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            //AsynchronousChannelGroup
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(DEFAULT_SERVER_HOST,
                    DEFAULT_SERVER_PORT));
            System.out.println("服务器启动，监听端口：" + DEFAULT_SERVER_PORT + "...");
            while (true) {

                serverSocketChannel.accept(null, new AcceptHandler());
                System.in.read();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(serverSocketChannel);
        }
    }

    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,Object> {
        @Override
        public void completed(AsynchronousSocketChannel result, Object attachment) {
            if (serverSocketChannel.isOpen()) {
                serverSocketChannel.accept(null,this);
            }
            AsynchronousSocketChannel clientChannel = result;
            if (clientChannel != null && clientChannel.isOpen()) {
                ClientHandler handler = new ClientHandler(clientChannel);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                Map<String, Object> info = new HashMap<>();
                info.put("type", "read");
                info.put("buffer", buffer);
                clientChannel.read(buffer,info, handler);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {

        }

    }

    private class ClientHandler implements CompletionHandler<Integer, Object> {

        private AsynchronousSocketChannel clientChannel;

        public ClientHandler(AsynchronousSocketChannel channel) {
            this.clientChannel = channel;
        }

        @Override
        public void completed(Integer result, Object attachment) {
            Map<String, Object> info = (Map<String, Object>) attachment;
            String type = (String) info.get("type");
            if ("read".equals(type)) {
                ByteBuffer buffer = (ByteBuffer) info.get("buffer");
                buffer.flip();
                info.put("type", "write");
                //直接写回去
                clientChannel.write(buffer,info,this);
                buffer.clear();
            } else if ("write".equals(type)) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                info = new HashMap<>();
                info.put("type", "read");
                info.put("buffer", buffer);
                clientChannel.read(buffer, info, this);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {

        }
    }

    public static void main(String[] args) {
        new AioServer().start();
    }
}
