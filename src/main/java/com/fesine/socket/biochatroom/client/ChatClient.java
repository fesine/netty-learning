package com.fesine.socket.biochatroom.client;

import java.io.*;
import java.net.Socket;

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

    private Socket socket;

    private BufferedReader reader;

    private BufferedWriter writer;

    /**
     * 向服务器发送消息
     * @param msg
     * @throws IOException
     */
    public void send(String msg) throws IOException {
        if (!socket.isOutputShutdown()) {
            writer.write(msg + "\n");
            writer.flush();
        }
    }

    /**
     * 从服务器接收消息
     * @return
     * @throws IOException
     */
    private String receive() throws IOException {
        String msg = null;
        if (!socket.isInputShutdown()) {
            msg = reader.readLine();
        }
        return msg;
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
    public  void close() {
        if (writer != null) {
            try {
                writer.close();
                System.out.println("关闭客户端.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void start(){
        try {
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //处理用户输入，新线程
            new Thread(new UserInputHandler(this)).start();
            //读取服务器转发的信息
            String msg = null;
            while ((msg = receive())!= null){
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}
