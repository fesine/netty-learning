package com.fesine.aync;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/1/15
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/1/15
 */
public class Bootstrap {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        //创建worker对象
        Worker worker = bootstrap.newWorker();
        //创建包装器对象
        Wrapper wrapper = new Wrapper();
        //包装器赋值
        wrapper.setWorker(worker);
        wrapper.setParam("hello");
        //执行worker，并返回传入的wrapper，并添加监听器获取worker执行结果
        bootstrap.doWork(wrapper).addListener(new Listener() {
            @Override
            public void result(Object result) {
                System.out.println(Thread.currentThread().getName());
                System.out.println(result);
            }
        });
        System.out.println(Thread.currentThread().getName());

    }

    /**
     * 执行worker动作，并将结果添加到监听器中
     * @param wrapper
     * @return
     */
    private Wrapper doWork(Wrapper wrapper) {
        new Thread(() ->{
            //获取worker
            Worker worker = wrapper.getWorker();
            //worker 执行操作，参数由wrapper中传入
            String result = worker.action(wrapper.getParam());
            //将结果添加到监听器中
            wrapper.getListener().result(result);
        }
        ).start();
        return wrapper;
    }

    /**
     * 创建一个worker实例
     * @return worker实例
     */
    private Worker newWorker(){
        return new Worker() {
            @Override
            public String action(Object object) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return object+" world";
            }
        };
    }
}
