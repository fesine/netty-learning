package com.fesine.socket.aiochatroom;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: aio实现服务端
 * @author: fesine
 * @createTime:2020/6/10
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/6/10
 */
public class ChatServer {
    private static final int DEFAULT_PORT = 8081;

    private static final String QUIT = "quit";

    private static final int BUFFER = 1024;

    private static final int THREAD_POOL_SIZE=8;

    private Charset charset = Charset.forName("UTF-8");

    private int port;

    private AsynchronousChannelGroup channelGroup;

    private AsynchronousServerSocketChannel serverChannel;

    private List<ClientHandler> clients;

    public ChatServer() {
        this(DEFAULT_PORT);
    }


    public ChatServer(int port) {
        this.port = port;
        clients = new ArrayList<>();
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

    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    public void start(){
        try {
            //AsynchronousChannelGroup
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
            serverChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverChannel.bind(new InetSocketAddress(DEFAULT_PORT));
            System.out.println("服务器启动，监听端口：" + DEFAULT_PORT + "...");
            while (true) {
                serverChannel.accept(null, new AcceptHandler());
                System.in.read();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(serverChannel);
        }
    }

    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

        @Override
        public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
            if (serverChannel.isOpen()) {
                serverChannel.accept(null,this);
            }
            //添加当前用户到客户列表
            if (clientChannel != null && clientChannel.isOpen()) {
                ClientHandler handler = new ClientHandler(clientChannel);
                addClient(handler);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                clientChannel.read(buffer, buffer, handler);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println("连接失败."+exc);

        }
    }

    private void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        System.out.println(getClientName(clientHandler.clientChannel)+"连接成功");
    }

    private class ClientHandler implements CompletionHandler<Integer,Object> {
        private AsynchronousSocketChannel clientChannel;
        public ClientHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void completed(Integer result, Object attachment) {
            ByteBuffer buffer = (ByteBuffer) attachment;
            if (buffer != null) {
                if (result <= 0) {
                    //客户端异常，移除客户端
                    removeClient(this);
                }else{
                    buffer.flip();
                    String msg = receive(buffer);
                    System.out.println(getClientName(clientChannel) + ":" + msg);
                    forwardMsg(clientChannel, msg);
                    buffer.clear();
                }
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {

        }
    }

    private void forwardMsg(AsynchronousSocketChannel clientChannel, String msg) {
        ByteBuffer buffer;
        for (ClientHandler clientHandler : clients) {
            String pre;
            if (clientHandler.clientChannel.equals(clientChannel)) {
                pre = "[me]";
            }else{
                pre = getClientName(clientChannel);
            }
            buffer = charset.encode(pre+msg);
            clientHandler.clientChannel.write(buffer, null, clientHandler);
            buffer.clear();
        }
    }

    private String receive(ByteBuffer buffer) {
        return String.valueOf(charset.decode(buffer));
    }

    private String getClientName(AsynchronousSocketChannel clientChannel) {
        try {
            return "客户端["+((InetSocketAddress)clientChannel.getRemoteAddress()).getPort()+"]";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println(getClientName(clientHandler.clientChannel)+"已退出");
        close(clientHandler.clientChannel);
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
