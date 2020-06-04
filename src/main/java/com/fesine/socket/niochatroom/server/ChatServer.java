package com.fesine.socket.niochatroom.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/5/27
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/27
 */
public class ChatServer {

    private static final int DEFAULT_PORT = 8081;

    private static final String QUIT = "quit";

    private int BUFFER = 1024;

    private ServerSocketChannel server;

    private Selector selector;


    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);

    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);

    private Charset charset = Charset.forName("UTF-8");

    private int port;

    public ChatServer(){
        this(DEFAULT_PORT);
    }


    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            selector = Selector.open();
            //监听accept事件
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动，监听端口："+port+"...");
            while (true) {
                //阻塞等待事件
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (SelectionKey selectedKey : selectedKeys) {
                    handles(selectedKey);
                }
                //清空事件
                selectedKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(selector);
        }
    }

    private void handles(SelectionKey selectedKey) throws IOException {
        //accept事件-和客户端建立了连接
        if (selectedKey.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) selectedKey.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            //注册read事件
            client.register(selector, SelectionKey.OP_READ);
            System.out.println(getClientName(client)+"已连接.");
        } else if (selectedKey.isReadable()) {
            //处理read事件
            SocketChannel client = (SocketChannel) selectedKey.channel();
            String fwdMsg = receive(client);

            if (fwdMsg.isEmpty()) {
                //取消监听
                selectedKey.cancel();
                //selector唤醒
                selector.wakeup();
            }else{
                System.out.println(getClientName(client) + ":"+fwdMsg);
                forwardMessage(client, fwdMsg);
                //判断是否是退出
                if (readyToQuit(fwdMsg)) {
                    //取消监听
                    selectedKey.cancel();
                    //selector唤醒
                    selector.wakeup();
                    System.out.println(getClientName(client) + "已断开.");
                }
            }
        }
    }

    /**
     * 转发消息
     * @param client
     * @param fwdMsg
     */
    private void forwardMessage(SocketChannel client, String fwdMsg) throws IOException {
        //通过selector keys获取所有客户端
        for (SelectionKey key : selector.keys()) {
            Channel connectedClient = key.channel();
            if (connectedClient instanceof ServerSocketChannel) {
                continue;
            }
            if (key.isValid()) {
                wBuffer.clear();
                String pre;
                if (client.equals(connectedClient)) {
                    pre = "[me]";
                }else {
                    pre = getClientName(client);
                }
                wBuffer.put(charset.encode(pre+":" + fwdMsg));
                //由写转为读
                wBuffer.flip();
                while (wBuffer.hasRemaining()) {
                    ((SocketChannel)connectedClient).write(wBuffer);
                }
            }
        }
    }

    /**
     * 读取数据
     * @param client
     * @return
     */
    private String receive(SocketChannel client) throws IOException {
        rBuffer.clear();
        while (client.read(rBuffer) > 0){}
        //转成读模式
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));
    }

    private String getClientName(SocketChannel client) {
        return "客户端[" + client.socket().getPort() + "]";
    }


    /**
     * 关闭服务端socket
     */
    public synchronized void close(Closeable closeable){
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }



    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
