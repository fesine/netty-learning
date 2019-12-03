package com.fesine.chapter5;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/3
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/3
 */
public class HeartbeatHandlerInitializer extends ChannelInitializer<Channel> {
    private static final int READ_IDEL_TIME_OUT = 4; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 5;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 7; // 所有超时
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT,
                ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
        pipeline.addLast();
    }
}
