package com.fesine.juc.jmm.finaldemo;

import java.lang.reflect.Field;

/**
 * @description: final关键字演示
 * @author: fesine
 * @createTime:2020/3/17
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/17
 */
public class Demo {
    public static final String a = "hello java";

    public static void main(String[] args) {
        System.out.println(a);
    }
}

//官方示例，可能读到y=0的情况
class FinalFieldExample{
    final int x;
    int y;

    static FinalFieldExample f;

    public FinalFieldExample() {
        this.x = 3;
        this.y = 4;
    }
    static void writer(){
        f = new FinalFieldExample();
    }

    static void reader(){
        if (f != null) {
            //此处的x肯定等于3
            int i = f.x;
            //此处的y可能为0；
            int j = f.y;
            System.out.println("i="+i+",j="+j);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                FinalFieldExample.writer();
                FinalFieldExample.reader();
            }).start();
        }
    }
}

//官方示例，new A().f()可能返回-1，0，1
class A{
    final int x;
    A(){
        x=1;
    }

    int f(){
        return d(this,this);
    }

    int d(A a1, A a2) {
        int i = a1.x;
        g(a1);
        int j = a2.x;
        return j-i;
    }

    static void g(A a1) {
        //使用反射技术修改A.x为2
        try {
            Field x = A.class.getDeclaredField("x");
            x.setAccessible(true);
            x.set(a1,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(new A().f());
    }
}
