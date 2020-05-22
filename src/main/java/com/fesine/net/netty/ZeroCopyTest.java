package com.fesine.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * @description: netty 零拷贝测试
 * @author: fesine
 * @createTime:2020/5/22
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/22
 */
public class ZeroCopyTest {

    /**
     * netty 通过wrap数组后，得到的byteBuf，数据底层还是原数组引用
     * 所以修改数组后，byteBuf数据也修改
     */
    @Test
    public void wrapTest(){
        byte[] arr = {1, 2, 3, 4, 5};
        ByteBuf byteBuf = Unpooled.wrappedBuffer(arr);
        System.out.println(byteBuf.getByte(4));
        arr[4] = 6;
        System.out.println(byteBuf.getByte(4));
    }

    /**
     * 从一个byteBuf对象分割出多个byteBuf对象
     * 分割出的对象，本质上还是引用了原对象
     */
    @Test
    public void sliceTest(){
        ByteBuf byteBuf = Unpooled.wrappedBuffer("hello".getBytes());
        ByteBuf newByteBuf = byteBuf.slice(1, 2);
        newByteBuf.unwrap();
        System.out.println(newByteBuf.toString());
    }

    /**
     * 将多byteBuf合并成一个逻辑上的byteBuf，避免了多个byteBuf之间的拷贝
     */
    @Test
    public void compositeTest(){
        ByteBuf b1 = Unpooled.buffer(3);
        b1.writeByte(1);
        ByteBuf b2 = Unpooled.buffer(3);
        b2.writeByte(4);
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        CompositeByteBuf newBuf = compositeByteBuf.addComponents(true, b1, b2);
        System.out.println(newBuf);
    }
}
