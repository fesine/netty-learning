package com.fesine.net.netty;

/**
 * @description: 链表形式调用-netty就是使用这种形式调用
 * @author: fesine
 * @createTime:2020/5/22
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/5/22
 */
public class PipelineDemo {
    /**
     * 初始化的时候创建一个head，作为责任链的开始，但是没有具体的实现
     */
    public HandlerChainContext head = new HandlerChainContext(new AbstractHandler() {
        @Override
        void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
            handlerChainContext.runNext(arg0);
        }
    });

    /**
     * 处理请求
     * @param arg0
     */
    public void requestProcess(Object arg0){
        this.head.handler(arg0);
    }

    /**
     * 添加handler到head的next节点，组成链表
     * @param handler
     */
    public void addLast(AbstractHandler handler) {
        HandlerChainContext context = head;
        while (context.next != null){
            context = context.next;
        }
        context.next = new HandlerChainContext(handler);

    }

    public static void main(String[] args) {
        PipelineDemo pipelineDemo = new PipelineDemo();
        pipelineDemo.addLast(new Handler2());
        pipelineDemo.addLast(new Handler1());
        pipelineDemo.addLast(new Handler1());
        pipelineDemo.addLast(new Handler2());

        pipelineDemo.requestProcess("小火车呜呜呜~~");
    }
}

/**
 * handler上下文，主要负责维护链和链的执行
 */
class HandlerChainContext{
    /**
     * 下一个节点
     */
    HandlerChainContext next;

    AbstractHandler handler;

    /**
     * 构造器
     * @param handler 需要处理的handler
     */
    public HandlerChainContext(AbstractHandler handler) {
        this.handler = handler;
    }

    /**
     * handler方法
     * @param arg0
     */
    void handler(Object arg0){
        this.handler.doHandler(this,arg0);
    }

    /**
     * 链式处理下一个handler
     * @param arg0
     */
    void runNext(Object arg0){
        if(this.next != null){
            this.next.handler(arg0);
        }
    }
}

/**
 * 处理器抽象类
 */
abstract class AbstractHandler{

    /**
     * 处理器只做一件事情，在传入的字符串中增加一个尾巴
     * @param handlerChainContext
     * @param arg0
     */
    abstract void doHandler(HandlerChainContext handlerChainContext,Object arg0);

}

class Handler1 extends AbstractHandler{

    @Override
    void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
        arg0 = arg0.toString() + "----handler1的小尾巴";
        System.out.println("我是Handler1的实例，我在处理：" + arg0);
        handlerChainContext.runNext(arg0);
    }
}

class Handler2 extends AbstractHandler{

    @Override
    void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
        arg0 = arg0.toString() + "----handler2的小尾巴";
        System.out.println("我是Handler2的实例，我在处理：" + arg0);
        handlerChainContext.runNext(arg0);
    }
}
