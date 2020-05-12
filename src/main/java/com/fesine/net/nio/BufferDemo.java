package com.fesine.net.nio;

import java.nio.ByteBuffer;

/**
 * @description: 理解buffer
 * @author: fesine
 * @createTime:2020/4/26
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/26
 */
public class BufferDemo {
    public static void main(String[] args) {
        //开辟一个4字节大小的缓冲空间
        ByteBuffer buffer = ByteBuffer.allocate(4);
        //默认写入模式，三个重要指标
        System.out.println(String.format("初始化：capacity容量：%s.position位置：%s.limit限制：%s",
                buffer.capacity(), buffer.position(), buffer.limit()));
        //写入2个字节的数据
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        //再次查看三个指标
        System.out.println(String.format("写入3字节之后：capacity容量：%s.position位置：%s.limit限制：%s",
                buffer.capacity(), buffer.position(), buffer.limit()));

        //转换为读模式（如果不调用flip()方法，也是可以读取的，但读取的位置是写的位置，读取的内容不对）
        System.out.println("####开始读取");
        buffer.flip();
        byte a = buffer.get();
        System.out.println(a);
        byte b =buffer.get();
        System.out.println(b);
        System.out.println(String.format("读取2字节之后：capacity容量：%s.position位置：%s.limit限制：%s",
                buffer.capacity(), buffer.position(), buffer.limit()));
        //续续写入3个字节数据，此时读模式下，limit=3，position=2，继续写入只能覆盖写入一条数据
        //clear()方法清除整个缓冲区，compact()方法仅清除已读数据，转为写入模式
        buffer.compact();
        buffer.put((byte) 4);
        buffer.put((byte) 5);
        buffer.put((byte) 6);
        //再次查看三个指标
        System.out.println(String.format("最终的情况：capacity容量：%s.position位置：%s.limit限制：%s",
                buffer.capacity(), buffer.position(), buffer.limit()));
        //以下方法需要理解
        //rewind();重置position位置为0
        //mark();标记当前position位置
        //reset();重置当前position为上次mark()标记的位置
    }
}
