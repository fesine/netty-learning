package com.fesine.juc.jmm.volatiletest;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * 1、 jre/bin/server  放置hsdis动态链接库
 * 测试代码 将运行模式设置为-server， 变成死循环   。 没加默认就是client模式，就是正常（可见性问题）
 * 2、 通过设置JVM的参数，打印出jit编译的内容 （这里说的编译非class文件），通过可视化工具jitwatch进行查看
 * -server -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=jit.log
 * 关闭jit优化-Djava.compiler=NONE
 * @author: fesine
 * @createTime:2020/3/14
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/14
 */
public class VisibilityDemo {
    private boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        VisibilityDemo demo = new VisibilityDemo();
        Thread thread = new Thread(() -> {
            int i = 0;
            //class -> 运行时jit编译 ->汇编指令 ->重排序
            while (demo.flag) {
                i++;
            }
            System.out.println(i);
        });
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        //设置flag为false，终止上面的while循环
        demo.flag = false;
        System.out.println("flag被设置为false了");
    }
}
