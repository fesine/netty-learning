package com.fesine.regexp;

/**
 * @description: 难是不是整数或小数
 * @author: fesine
 * @createTime:2022/4/4
 * @update:修改内容
 * @author: fesine
 * @updateTime:2022/4/4
 */
public class HomeWork02 {
    public static void main(String[] args) {
        String content = "123";
        String regStr = "^[-+]?([1-9]\\d*|0)(\\.\\d+)?$";
        if (content.matches(regStr)) {
            System.out.println("匹配成功");
        }else{
            System.out.println("匹配失败");
        }
    }
}
