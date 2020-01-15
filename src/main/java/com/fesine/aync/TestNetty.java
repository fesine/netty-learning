package com.fesine.aync;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/1/15
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/1/15
 */
public class TestNetty {
    private static String host = "127.0.0.1";
    private static Integer port = 8001;


    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .remoteAddress(host,port)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer() {

                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                //10秒没消息时，发送心跳包
                                .addLast(new IdleStateHandler(10,0,0));
                    }
                });
        try {
            bootstrap.connect().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
