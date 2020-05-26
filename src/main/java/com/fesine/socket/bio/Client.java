package com.fesine.socket.bio;

import java.io.*;
import java.net.Socket;

/**
 * @description: BIO实现客户端
 * @author: fesine
 * @createTime:2020/5/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/26
 */
public class Client {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final Integer DEFAULT_PORT = 8011;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            //建立连接
            socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
            System.out.println("客户端启动成功!");
            System.out.print("请输入消息：");
            //向服务端发送消息
            //获取控制台输入流，使用装饰者模式
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));
            //创建输出流，用于向服务端发送消息
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String msg;
            if ((msg = reader.readLine()) != null) {
                writer.write(msg+"\n");
                writer.flush();
            }
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if ((msg = reader.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
