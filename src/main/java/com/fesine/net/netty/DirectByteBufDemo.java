package com.fesine.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.Arrays;

/**
 * @description: netty的堆外内存 byteBuf demo
 * @author: fesine
 * @createTime:2020/5/20
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/20
 */
public class DirectByteBufDemo {
    //  +-------------------+------------------+------------------+
    //  | discardable bytes |  readable bytes  |  writable bytes  |
    //  |                   |     (CONTENT)    |                  |
    //  +-------------------+------------------+------------------+
    //  |                   |                  |                  |
    //  0      <=       readerIndex   <=   writerIndex    <=    capacity

    @Test
    public void apiTest(){

        // 1.创建一个非池化的ByteBuf，大小为10个字节
        ByteBuf buf = Unpooled.directBuffer(10);
        System.out.println("原始ByteBuf为====================>" + buf.toString());
        //System.out.println("1.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");

        // 2.写入一段内容
        byte [] bytes = {1,2,3,4,5};
        buf.writeBytes(bytes);
        System.out.println("写入的内容为====================>" + Arrays.toString(bytes));
        System.out.println("写入一段内容后的ByteBuf为====================>" + buf.toString());
        //System.out.println("2.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");

        // 3.读取一段内容
        byte b1 = buf.readByte();
        byte b2 = buf.readByte();
        System.out.println("读取的内容为====================>" + Arrays.toString(new byte[]{b1,b2}));
        System.out.println("读取一段内容后的ByteBuf为====================>" + buf.toString());
        //System.out.println("3.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");

        // 4.将读取的内容丢弃
        buf.discardReadBytes();
        System.out.println("丢弃读取内容后的ByteBuf为====================>" + buf.toString());
        //System.out.println("4.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");

        // 5.清空读指针
        buf.clear();
        System.out.println("清空读指针后的ByteBuf为====================>" + buf.toString());
        //System.out.println("5.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");

        // 6.写入一段比第一次写入少的内容
        byte[] bytes2 = {1, 2, 3};
        buf.writeBytes(bytes2);
        System.out.println("写入的内容为====================>" + Arrays.toString(bytes2));
        System.out.println("写入一段内容后的ByteBuf为====================>" + buf.toString());
        //System.out.println("6.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");

        // 7.清空buf
        buf.setZero(0, buf.capacity());
        System.out.println("清空后的ByteBuf为====================>" + buf.toString());
        //System.out.println("7.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");

        // 8.写入一段超出容量的内容
        byte[] bytes3 = {1, 2, 3,4,5,6,7,8,9,10,11};
        buf.writeBytes(bytes3);
        System.out.println("写入的内容为====================>" + Arrays.toString(bytes2));
        System.out.println("写入一段内容后的ByteBuf为====================>" + buf.toString());
        //System.out.println("8.ByteBuf中的内容为===============>" + Arrays.toString(buf.array()) + "\n");
        //  随机访问索引 getByte
        //  顺序读 read*
        //  顺序写 write*
        //  清除已读内容 discardReadBytes
        //  清除缓冲区 clear
        //  搜索操作
        //  标记和重置
        //  完整代码示例：参考
        // 搜索操作 读取指定位置 buf.getByte(1);
        //
        /**
         * 原始ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0, widx: 0,
         * cap: 10)
         * 写入的内容为====================>[1, 2, 3, 4, 5]
         * 写入一段内容后的ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0,
         * widx: 5, cap: 10)
         * 读取的内容为====================>[1, 2]
         * 读取一段内容后的ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 2,
         * widx: 5, cap: 10)
         * 丢弃读取内容后的ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0,
         * widx: 3, cap: 10)
         * 清空读指针后的ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0,
         * widx: 0, cap: 10)
         * 写入的内容为====================>[1, 2, 3]
         * 写入一段内容后的ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0,
         * widx: 3, cap: 10)
         * 清空后的ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0, widx:
         * 3, cap: 10)
         * 写入的内容为====================>[1, 2, 3]
         * 写入一段内容后的ByteBuf为====================>UnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0,
         * widx: 14, cap: 64)
         */
    }
}
