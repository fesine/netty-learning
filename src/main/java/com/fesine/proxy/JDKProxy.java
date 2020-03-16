package com.fesine.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: jdk代理
 * @author: fesine
 * @createTime:2020/3/16
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/16
 */
public class JDKProxy implements InvocationHandler {

    private Object target;

    public JDKProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return ((RealHello)target).invoke();
    }

    public static void main(String[] args) {
        JDKProxy proxy = new JDKProxy(new RealHello());
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        //把生成的代理类保存到文件中
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        //生成代理类
        Hello test = (Hello) Proxy.newProxyInstance(classLoader, new Class[]{Hello.class}, proxy);
        System.out.println(test.say());

    }
}
