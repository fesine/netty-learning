package com.fesine.chapter1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: 抛弃服务器，接收到消息就丢弃
 * @author: fesine
 * @createTime:2019/11/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/11/18
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //丢弃消息
        //((ByteBuf)msg).release();
        //try {
        //    ByteBuf buf = (ByteBuf) msg;
        //    while (buf.isReadable()) {
        //        System.out.print((char) buf.readByte());
        //        System.out.flush();
        //    }
        //} finally {
        //    ReferenceCountUtil.release(msg);
        //}
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
