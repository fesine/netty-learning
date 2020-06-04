package com.fesine.socket.niochatroom.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
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
public class ChatClient {

    private static final Integer DEFAULT_SERVER_PORT = 8081;

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";

    private static final String QUIT = "quit";

    private int BUFFER = 1024;

    private SocketChannel client;

    private Selector selector;


    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);

    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);

    private Charset charset = Charset.forName("UTF-8");

    private String host;

    private int port;

    public ChatClient() {
        this(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * TODO 向服务器发送消息
     * @param msg
     * @throws IOException
     */
    public void send(String msg) throws IOException {
        if (msg.isEmpty()) {
            return;
        }
        wBuffer.clear();
        wBuffer.put(charset.encode(msg));
        //转成读模式
        wBuffer.flip();
        while (wBuffer.hasRemaining()) {
            client.write(wBuffer);
        }
        if (QUIT.equals(msg)) {
            close(selector);
        }

    }

    /**
     * TODO 从服务器接收消息
     * @return
     * @throws IOException
     */
    private String receive(SocketChannel client) throws IOException {
        rBuffer.clear();
        while (client.read(rBuffer) > 0) {
        }
        //转成读模式
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));
    }

    /**
     * 检查客户端是否准备退出
     * @param msg
     * @return
     */
    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    /**
     * 关闭服务端socket
     */
    public void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start(){
        try {
            client = SocketChannel.open();
            client.configureBlocking(false);
            selector = Selector.open();
            client.connect(new InetSocketAddress(host, port));
            client.register(selector, SelectionKey.OP_CONNECT);
            System.out.println("客户端启动，端口：" + port + "...");
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
        } catch (ClosedSelectorException e) {

        } finally {
            close(selector);
        }
    }

    private void handles(SelectionKey selectedKey) throws IOException {
        //connect事件-连接就绪
        if (selectedKey.isConnectable()) {
            SocketChannel client = (SocketChannel) selectedKey.channel();
            if (client.isConnectionPending()) {
                client.finishConnect();
                //处理用户的输入
                new Thread(new UserInputHandler(this)).start();
            }
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectedKey.isReadable()) {
            //read事件-服务器转发消息
            SocketChannel client = (SocketChannel) selectedKey.channel();
            String msg = receive(client);
            if (msg.isEmpty()) {
                //服务器异常
                close(selector);
            }else{
                System.out.println(msg);
            }
        }

    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}
