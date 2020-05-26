package com.fesine.socket.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: BIO socket服务端
 * @author: fesine
 * @createTime:2020/5/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/26
 */
public class Server {

    private static final Integer DEFAULT_PORT=8011;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            //绑定端口
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("服务端启动成功!监听端口："+DEFAULT_PORT);
            //等待客户端连接
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("客户端[" + socket.getPort() + "],连接成功！");
                //获取输入流，使用装饰者模式
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //创建输出流，用于向客户端响应消息
                BufferedWriter writer =
                        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String msg;
                if ((msg = reader.readLine()) != null) {
                    System.out.println("收到客户端[" + socket.getPort() + "]消息：" + msg);
                    writer.write("[服务端]"+msg+"\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null) {
                try {
                    System.out.println("关闭服务端连接。");
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
