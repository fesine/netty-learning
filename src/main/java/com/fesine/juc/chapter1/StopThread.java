package com.fesine.juc.chapter1;

/**
 * @description: 线程中止演示
 * @author: fesine
 * @createTime:2020/3/11
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/11
 */
public class StopThread extends Thread {
    private int i=0,j=0;

    @Override
    public void run() {
        synchronized (this){
            ++i;
            //让线程等待10秒
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++j;
        }
    }

    public void print(){
        System.out.println("i="+i+",j=" + j);
    }
}
