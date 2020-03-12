package com.fesine.juc.chapter1;

import java.util.List;
import java.util.concurrent.*;

/**
 * @description: 线程池的使用
 * @author: fesine
 * @createTime:2020/3/12
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/12
 */
public class Demo9 {

    /**
     * 测试：提交15个任务，每个任务执行时间3秒，看不同结果
     * @param executor
     */
    private void testCommon(ThreadPoolExecutor executor) throws InterruptedException {
        for (int i = 0; i < 15; i++) {
            int n = i;
            executor.submit(()->{
                try {
                    System.out.println("开始执行："+n);
                    Thread.sleep(3000L);
                    System.err.println("执行结束："+n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("任务提交成功："+i);
        }
        //等待500毫秒，查看线程池线程数量
        Thread.sleep(500L);
        System.out.println("当前线程池线程数量："+executor.getPoolSize());
        System.out.println("当前线程池等待的数量："+executor.getQueue().size());
        //等待15秒，所有线程执行结束，查看线程池线程数量
        Thread.sleep(5000L);
        System.out.println("当前线程池线程数量："+executor.getPoolSize());
        System.out.println("当前线程池等待的数量："+executor.getQueue().size());

    }

    /**
     * 线程池信息：核心线程数量5，最大线程数量10，无界队列，超出核心线程数的线程存活时间5秒，指定拒绝策略
     * @throws InterruptedException
     */
    private void threadPoolExecutorTest1() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
        testCommon(executor);
        //预计结果，线程池线程数5，超出的任务进入队列，等待被执行
    }

    /**
     * 线程池信息：核心线程数量5，最大线程数量10，队列大小3，超出核心线程数的线程存活时间5秒，指定拒绝策略
     * @throws InterruptedException
     * 预计结果：
     * 1、5个任务被分配执行
     * 2、3个任务添加到队列等待
     * 3、队列不够用，临时增加5个线程来执行任务（5秒没任务，就销毁）
     * 4、队列和线程均不够用，多出2个任务没法被执行，被拒绝
     * 5、任务执行后5秒，没有任务，销毁临时创建的5个线程
     */
    private void threadPoolExecutorTest2() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3), (r, executor1) -> System.err.println("任务被拒绝了"));
        testCommon(executor);
    }

    /**
     * 线程池信息：核心线程数量5，最大线程数量5，无界队列，指定拒绝策略
     *
     * @throws InterruptedException
     *
     * 预计结果：线程数5，超出数量的任务进入队列等待
     */
    private void threadPoolExecutorTest3() throws InterruptedException {
        //效果相同
        //ThreadPoolExecutor executor = Executors.newFixedThreadPool(5);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
        testCommon(executor);
        //预计结果，线程池线程数5，超出的任务进入队列，等待被执行
    }

    /**
     * 线程池信息：核心线程数量0，最大线程数量Integer.MAX_VALUE，存活时间为20秒，同步无界队列，指定拒绝策略
     *
     * @throws InterruptedException
     *
     * 预计结果：线程数15，等待20秒之后，线程数为0
     */
    private void threadPoolExecutorTest4() throws InterruptedException {
        //效果相同
        //ThreadPoolExecutor executor = Executors.newCachedThreadPool();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 20, TimeUnit.SECONDS,
                new SynchronousQueue<>());
        testCommon(executor);
        Thread.sleep(20*1000L);
        System.out.println("20秒后，再查看线程池中的线程数量："+executor.getPoolSize());
    }

    /**
     * 线程池信息：核心线程数量5，最大线程数量Integer.MAX_VALUE，存活时间为0秒，DelayedWorkQueue延时队列
     *
     * @throws InterruptedException
     *
     * 预计结果：线程数5，等待3秒后，一次性执行
     */
    private void threadPoolExecutorTest5() throws InterruptedException {
        //效果相同
        //ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
        executor.schedule(() -> System.out.println("任务执行了：" + System.currentTimeMillis()), 3,
                TimeUnit.SECONDS);
        System.out.println("定时任务提交成功，提交时间：" + System.currentTimeMillis() + "，当前线程池中线程数量：" + executor.getPoolSize());

    }

    /**
     * 线程池信息：核心线程数量5，最大线程数量Integer.MAX_VALUE，存活时间为0秒，DelayedWorkQueue延时队列
     *
     * @throws InterruptedException
     *
     */
    private void threadPoolExecutorTest6() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
        /**
         * 周期性执行一个任务，线程池提供了两种执行方式。
         * 测试场景：提交的任务需要执行3秒才能完成，看两种不同方式的区别
         * 1、提交代码后2秒后开始执行，之后每隔1秒，固定执行一次（如果发现上次执行没有结束，则等待结束后，马上执行）
         * 如示例中每次执行3秒，每隔1秒执行一次，执行结束后立刻开始下一任务的执行，无需等待
         */
        executor.scheduleAtFixedRate(()->{
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务-1被执行，现在时间是："+System.currentTimeMillis()/1000);
        },2,1,TimeUnit.SECONDS);
        /**
         * 2、同样提交代码后2秒开始执行，之后每隔1秒，固定执行一次（如果发现上次执行没有结束，则等待结束后，再延时1秒，执行下一任务）
         * 其效果是3+1秒=4秒，每次间隔4秒执行一次
         */
        executor.scheduleWithFixedDelay(()->{
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("任务-2被执行，现在时间是："+System.currentTimeMillis()/1000);
        },2,1,TimeUnit.SECONDS);

    }

    /**
     * 终止线程：线程池信息：核心线程数量5，最大线程数量10，队列大小3，超出核心线程数的线程存活时间5秒，指定拒绝策略
     *
     * @throws InterruptedException
     *
     * 预计结果：
     * 1、10个任务被执行，3个任务进入队列，2个任务被拒绝
     * 2、调用shutdown方法后，不接收新的任务，已经继续等待13个任务执行完成
     * 3、线程池关闭后，不再接收新的任务
     */
    private void threadPoolExecutorTest7() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3), (r, executor1) -> System.err.println("任务被拒绝了"));
        for (int i = 0; i < 15; i++) {
            int n = i;
            executor.submit(() -> {
                try {
                    System.out.println("开始执行：" + n);
                    Thread.sleep(3000L);
                    System.err.println("执行结束：" + n);
                } catch (InterruptedException e) {
                    System.out.println("异常："+e.getMessage());
                }
            });
            System.out.println("任务提交成功：" + i);
        }
        Thread.sleep(1000L);
        executor.shutdown();
        executor.submit(() -> System.out.println("追加一个任务"));
    }
    /**
     * 终止线程：线程池信息：核心线程数量5，最大线程数量10，队列大小3，超出核心线程数的线程存活时间5秒，指定拒绝策略
     *
     * @throws InterruptedException
     *
     * 预计结果：
     * 1、10个任务被执行，3个任务进入队列，2个任务被拒绝
     * 2、调用shutdown方法后，3个任务在队列中的任务不在执行，10个线程被中止
     * 3、线程池关闭后，不再接收新的任务
     */
    private void threadPoolExecutorTest8() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3), (r, executor1) -> System.err.println("任务被拒绝了"));
        for (int i = 0; i < 15; i++) {
            int n = i;
            executor.submit(() -> {
                try {
                    System.out.println("开始执行：" + n);
                    Thread.sleep(3000L);
                    System.err.println("执行结束：" + n);
                } catch (InterruptedException e) {
                    System.out.println("异常："+e.getMessage());
                }
            });
            System.out.println("任务提交成功：" + i);
        }
        Thread.sleep(1000L);
        List<Runnable> runnables = executor.shutdownNow();
        executor.submit(() -> System.out.println("追加一个任务"));
        System.out.println("未完成的任务数："+runnables.size());
    }



    public static void main(String[] args) throws InterruptedException {
        //new Demo9().threadPoolExecutorTest1();
        //new Demo9().threadPoolExecutorTest2();
        //new Demo9().threadPoolExecutorTest3();
        //new Demo9().threadPoolExecutorTest4();
        //new Demo9().threadPoolExecutorTest5();
        //new Demo9().threadPoolExecutorTest6();
        //new Demo9().threadPoolExecutorTest7();
        new Demo9().threadPoolExecutorTest8();
    }
}
