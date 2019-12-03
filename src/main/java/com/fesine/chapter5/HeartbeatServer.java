package com.fesine.chapter5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/3
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/3
 */
public class HeartbeatServer {

        public static void main (String[]args) throws Exception {

            // Configure the server.
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 100)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new HeartbeatHandlerInitializer());

                // Start the server.
                ChannelFuture f = b.bind(8079).sync();

                // Wait until the server socket is closed.
                f.channel().closeFuture().sync();
            } finally {
                // Shut down all event loops to terminate all threads.
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
    }
}
