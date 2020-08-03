package com.fesine.array;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/7/24
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/7/24
 */
public class FastJsonTest {
    public static void main(String[] args) {
        Demo demo = JSON.parseObject("{'date':'2020-01-01 00:00:00'}", Demo.class);
    }

}

class Demo {
    public Date date;
}
