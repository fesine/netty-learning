package com.fesine.mq.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/8/3
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/8/3
 */
public class BrokerServer implements Runnable{

    public static int SERVICE_PORT=9999;

    private final Socket socket;

    public BrokerServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream())){
            {
                while (true) {
                    String str = in.readLine();
                    if (str == null) {
                        continue;
                    }
                    System.out.println("接收到原始数据：" + str);
                    if(str.equals("CONSUME")){
                        String message = Broker.consume();
                        out.println(message);
                        out.flush();
                    }else{
                        Broker.produce(str);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(SERVICE_PORT);
        while (true) {
            BrokerServer brokerServer = new BrokerServer(server.accept());
            new Thread(brokerServer).start();
        }
    }
}
