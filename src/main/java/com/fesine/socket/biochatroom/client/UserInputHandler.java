package com.fesine.socket.biochatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/5/27
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/27
 */
public class UserInputHandler implements Runnable {

    private ChatClient chatClient;

    public UserInputHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String msg = reader.readLine();
                chatClient.send(msg);
                if (chatClient.readyToQuit(msg)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
