package com.fesine.socket.aiochatroom;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

/**
 * @description: aio客户端
 * @author: fesine
 * @createTime:2020/6/11
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/6/11
 */
public class ChatClient {
    private static final Integer DEFAULT_SERVER_PORT = 8081;

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";

    private static final String QUIT = "quit";

    private int BUFFER = 1024;

    private Charset charset = Charset.forName("UTF-8");

    private String host;

    private int port;

    private AsynchronousSocketChannel clientChannel;

    private RWHandler handler;

    public ChatClient() {
       this(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 检查客户端是否准备退出
     *
     * @param msg
     * @return
     */
    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    /**
     * 关闭服务端socket
     */
    public synchronized void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        ByteBuffer buffer = charset.encode(msg);
        clientChannel.write(buffer, null, handler);
        buffer.clear();
    }

    public void start(){
        try {
            clientChannel = AsynchronousSocketChannel.open();
            clientChannel.connect(new InetSocketAddress(host,
                    port), null, new ConnectHandler(this));
            while (clientChannel.isOpen()) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(clientChannel);
        }
    }


    private class ConnectHandler implements CompletionHandler<Void,Object> {
        private ChatClient chatClient;
        public ConnectHandler(ChatClient chatClient) {
            this.chatClient = chatClient;
        }

        @Override
        public void completed(Void result, Object attachment) {
            handler = new RWHandler(clientChannel);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER);
            //异步调用read，当服务器有消息转发给该用户，便触发回调函数
            clientChannel.read(buffer, buffer, handler);
            //启动新线程监听用户输入
            new Thread(new UserInputHandler(chatClient)).start();
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println("客户端连接失败");

        }
    }

    private class RWHandler implements CompletionHandler<Integer, Object> {
        private AsynchronousSocketChannel clientChannel;

        public RWHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void completed(Integer result, Object attachment) {
            ByteBuffer buffer = (ByteBuffer) attachment;
            if (buffer != null) {
                if (result > 0) {
                    buffer.flip();
                    String msg = String.valueOf(charset.decode(buffer));
                    System.out.println(msg);
                    buffer.clear();
                    //继续异步读，相当于一直在监听服务器是否有消息转发过来
                    clientChannel.read(buffer,buffer,this);
                }
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            close(clientChannel);
        }
    }

    public static void main(String[] args) {
        new ChatClient().start();
    }

}
