package com.fesine.socket.biochatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/5/27
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/27
 */
public class ChatHandler implements Runnable {

    private ChatServer server;

    private Socket socket;

    public ChatHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            server.addClient(socket);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = null;
            while ((msg = reader.readLine())!= null){
                String message = "客户端[" + socket.getPort() + "]：" + msg + "\n";
                //在服务端打印客户端消息
                System.out.print(message);
                //再将消息转发到其他客户端
                server.forwardMessage(socket, message);

                //检查是否是退出命令
                if (server.readyToQuit(msg)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //无论正常，异常退出之后，均断开连接
                server.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
