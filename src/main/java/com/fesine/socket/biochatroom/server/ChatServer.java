package com.fesine.socket.biochatroom.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/5/27
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/27
 */
public class ChatServer {

    private static final Integer DEFAULT_PORT = 8081;

    private static final String QUIT = "quit";

    /**
     * 服务端socket
     */
    private ServerSocket serverSocket;

    /**
     * 存放已经连接的客户端，key=port，value=输出流
     */
    private Map<Integer, Writer> connectedClients;


    public ChatServer() {
        connectedClients = new HashMap<>();
    }

    /**
     * 添加新连接的客户端到map集合中
     * @param socket
     * @throws IOException
     */
    public synchronized void addClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            connectedClients.put(port, writer);
            System.out.println("客户端["+port+"]已经连接到服务器");

        }
    }

    /**
     * 当客户端断开时，关闭write，并从map中移除对应的客户端连接
     * @param socket
     * @throws IOException
     */
    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (connectedClients.containsKey(port)) {
                //关闭write就是关闭客户端连接
                connectedClients.get(port).close();
            }
            connectedClients.remove(port);
            System.out.println("客户端["+port+"]已经下线");

        }
    }

    /**
     * 转发给其他客户端消息
     * @param socket
     * @param message
     */
    public synchronized void forwardMessage(Socket socket,String message) throws IOException {
        for (Integer id : connectedClients.keySet()) {
            if (!id.equals(socket.getPort())) {
                Writer writer = connectedClients.get(id);
                writer.write(message);
                writer.flush();
            }
        }
    }

    /**
     * 关闭服务端socket
     */
    public synchronized void close(){
        if(serverSocket != null){
            try {
                serverSocket.close();
                System.out.println("关闭server socket.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    public void start(){
        try {
            //绑定端口
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("服务器启动成功，监听端口：" + DEFAULT_PORT + "...");
            while (true) {
                //等待客户端连接，此方法阻塞
                Socket socket = serverSocket.accept();
                //创建chatHandler线程
                new Thread(new ChatHandler(this,socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
