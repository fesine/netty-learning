package com.fesine.aync;

/**
 * @description: 将执行和监听器绑定到一个包装对象中
 * @author: fesine
 * @createTime:2020/1/15
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/1/15
 */
public class Wrapper {

    private Worker worker;

    private Listener listener;

    private Object param;

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Listener getListener() {
        return listener;
    }

    public void addListener(Listener listener) {
        this.listener = listener;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
