package com.fesine.chapter3.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/11/19
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/11/19
 */
public class SimpleChatServer {

    private int port;

    public SimpleChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new SimpleChatServerInitializer())
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            System.out.println("simpleChatServer 启动了");
            //绑定端口，接收连接
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();


        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.out.println("simpleChatServer 关闭了");
        }
    }

    public static void main(String[] args) throws Exception {
        new SimpleChatServer(8078).run();
    }
}
