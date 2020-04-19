package com.fesine.juc.forkjoin;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @description: 使用ForkJoin进行用户信息查询
 * {"userId":"fesine","address":"hangzhou","age":18,"userName":"张三"}
 * @author: fesine
 * @createTime:2020/4/19
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/19
 */
public class UserInfoServiceForkJoin {

    /**
     * 本质上是一个线程池，默认线程数量是cup的核数
     */
    ForkJoinPool forkJoinPool = new ForkJoinPool
            (10,
                    ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                    null, true);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        UserInfoServiceForkJoin service = new UserInfoServiceForkJoin();
        service.getUserAllInfo();
    }

    /**
     * 并行查询，直接使用多线程无法获取结果
     * @return
     */
    private Object getUserAllInfo() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        //提交一个forkJoinTask
        //forkJoinPool.submit();
        ArrayList<String> methods = new ArrayList<>();
        methods.add("getUserInfo");
        methods.add("getUserExtraInfo");
        HttpJsonRequest request = new HttpJsonRequest(methods, 0, methods.size() - 1);
        ForkJoinTask<JSONObject> task = forkJoinPool.submit(request);
        JSONObject result = task.get();
        long end = System.currentTimeMillis();
        System.out.println("获取用户所有信息耗时为：" + (end - start));
        System.out.println("用户所有信息：" + result.toJSONString());
        return result;
    }



}

//任务
class HttpJsonRequest extends RecursiveTask<JSONObject> {

    private static final long serialVersionUID = 7619984768890831805L;

    /**
     * 模拟要调用的方法列表
     */
    ArrayList<String> methods;
    int start;
    int end;

    HttpJsonRequest(ArrayList<String> methods,int start,int end){
        this.methods = methods;
        this.start= start;
        this.end = end;
    }

    /**
     * 实际执行的入口(任务拆分)
     * @return
     */
    @Override
    protected JSONObject compute() {
        //表示当前task需要处理多少个数据
        int count = end-start;
        if(count == 0){
            //如果只有一个方法，直接调用，通过反射调用方法
            try {
                long startTime = System.currentTimeMillis();
                Method method = this.getClass().getDeclaredMethod(methods.get(start), null);
                JSONObject result = (JSONObject)method.invoke(this, null);
                System.out.println(Thread.currentThread()+",方法调用完毕，耗时：" + (System.currentTimeMillis() - startTime)+"，method:"+ methods.get(start));
                return result;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(Thread.currentThread()+"，拆分一次");
            //多个方法调用，进行拆分成子任务
            int x = (start+end) /2;
            //处理前半部分
            HttpJsonRequest httpJsonRequest = new HttpJsonRequest(methods, start, x);
            httpJsonRequest.fork();
            //处理后半部分
            HttpJsonRequest httpJsonRequest1 = new HttpJsonRequest(methods, x + 1, end);
            httpJsonRequest1.fork();
            JSONObject result = new JSONObject();
            //通过join方法获取结果
            result.putAll(httpJsonRequest.join());
            result.putAll(httpJsonRequest1.join());
            return result;

        }
        return null;
    }


    /**
     * 模拟获取用户基本信息
     *
     * @return
     */
    private JSONObject getUserInfo() {
        JSONObject object = new JSONObject();
        object.put("userId", "fesine");
        object.put("userName", "张三");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 模拟获取用户扩展信息
     *
     * @return
     */
    private JSONObject getUserExtraInfo() {
        JSONObject object = new JSONObject();
        object.put("userId", "fesine");
        object.put("age", 18);
        object.put("address", "hangzhou");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return object;
    }
}
