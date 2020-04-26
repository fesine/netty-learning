package com.fesine.net.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: BIO服务端，支持http请求
 * @author: fesine
 * @createTime:2020/4/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/26
 */
public class BIOServerMultThreadHttp {
   private static ExecutorService poolExecutor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8011);
        System.out.println("服务端启动成功！");
        //只要服务端没有关闭
        while (!serverSocket.isClosed()){
            //服务端阻塞等待
            Socket request = serverSocket.accept();
            //当有请求进来的时候，会走到下一步
            System.out.println("收到新连接，" + request.toString());
            poolExecutor.execute(()->{
                try {
                    //接收数据，打印 net+io
                    InputStream inputStream = request.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                            "utf-8"));
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        if (msg.length() == 0) {
                            break;
                        }
                        System.out.println(msg);
                    }
                    System.out.println("收到数据，来自：" + request.toString());
                    OutputStream outputStream = request.getOutputStream();
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write("Content-length:11\r\n\r\n".getBytes());
                    outputStream.write("Hello world".getBytes());
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        request.close();
                    } catch (Exception e) {
                        try {
                            request.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            });

        }
    }
}
