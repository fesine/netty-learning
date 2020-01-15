package com.fesine.aync;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description: 实现带回调、超时的异步任务
 * @author: fesine
 * @createTime:2020/1/15
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/1/15
 */
public class BootstrapNew {

    public static void main(String[] args) {
        BootstrapNew bootstrap = new BootstrapNew();
        //创建worker对象
        Worker worker = bootstrap.newWorker();
        //创建包装器对象
        Wrapper wrapper = new Wrapper();
        //包装器赋值
        wrapper.setWorker(worker);
        wrapper.setParam("hello");
        //添加结果回调器
        wrapper.addListener(result -> System.out.println(result));
        CompletableFuture future = CompletableFuture.supplyAsync(() -> bootstrap.doWork(wrapper));
        try {
            future.get(800, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //超时了
            wrapper.getListener().result("time out exception");
        }
        System.out.println(Thread.currentThread().getName());
    }

    /**
     * 执行worker动作，并将结果添加到监听器中
     *
     * @param wrapper
     * @return
     */
    private Wrapper doWork(Wrapper wrapper) {
        //获取worker
        Worker worker = wrapper.getWorker();
        //worker 执行操作，参数由wrapper中传入
        String result = worker.action(wrapper.getParam());
        //执行监听器
        wrapper.getListener().result(result);
        return wrapper;
    }

    /**
     * 创建一个worker实例
     *
     * @return worker实例
     */
    private Worker newWorker() {
        return object -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return object + " world";
        };
    }
}
