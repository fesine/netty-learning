package com.fesine.chapter3.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/11/19
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/11/19
 */
public class SimpleChatClient {

    private String host;

    private int port;

    public SimpleChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChatClientInitializer());
            Channel channel = b.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("客户端启动方式：java.net.preferIPv6Address：" + System.getProperty("java.net.preferIPv6Address"));
        System.out.println("客户端启动方式：java.net.preferIPv4Stack：" + System.getProperty(
                "java.net.preferIPv4Stack"));
        String host="localhost";
        if (args != null && args.length > 0 && args[0] != null) {
            host = args[0];
        }
        Integer port = 8078;
        //通过传参获取监听端口号
        if (args != null && args.length > 1 && args[1] != null ) {
            port = Integer.valueOf(args[1]);
        }
        System.out.println("host："+host+"，port："+port);
        new SimpleChatClient(host, port).run();
    }
}
