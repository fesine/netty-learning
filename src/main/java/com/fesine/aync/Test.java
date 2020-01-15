package com.fesine.aync;

import java.util.concurrent.*;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/1/15
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/1/15
 */
public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(new Task());
        //这一步会阻塞
        System.out.println(future.get());
        executorService.shutdown();
    }


    private static class Task implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("子线程正在计算");
            Thread.sleep(2000);
            return 1;
        }
    }
}
