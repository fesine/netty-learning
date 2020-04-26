package com.fesine.net.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @description: BIO客户端
 * @author: fesine
 * @createTime:2020/4/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/26
 */
public class BIOClient {
    private static Charset charset = Charset.forName("utf-8");
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8011);
        //输入流
        OutputStream outputStream = socket.getOutputStream();

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine();
        outputStream.write(msg.getBytes(charset));
        scanner.close();
        socket.close();
    }
}
