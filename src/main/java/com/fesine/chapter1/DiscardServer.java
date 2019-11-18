package com.fesine.chapter1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/11/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/11/18
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        //接收连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //启动 NIO 服务的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    //指定使用NioServerSocketChannel接收进来的连接
                    .channel(NioServerSocketChannel.class)
                    //ChannelInitializer 是一个特殊的处理类，他的目的是帮助使用者配置一个新的
                    // Channel。也许你想通过增加一些处理类比如DiscardServerHandler 来配置一个新的 Channel
                    // 或者其对应的ChannelPipeline 来实现你的网络程序。当你的程序变的复杂时，可能你会增加更多的处理类到 pipline
                    // 上，然后提取这些匿名类到最顶层的类上。
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();
            // 等待服务器  socket 关闭 。
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws Exception {
        new DiscardServer(8079).run();
    }
}
