package com.fesine.regexp;

/**
 * @description: 邮件格式匹配
 * @author: fesine
 * @createTime:2022/4/4
 * @update:修改内容
 * @author: fesine
 * @updateTime:2022/4/4
 */
public class HomeWork01 {
    public static void main(String[] args) {
        String content = "fesine@qq.com.cn";
        // String regStr = "\\w+@([a-zA-Z]+\\.)+[a-zA-Z]+";
        // 严谨写法
        String regStr = "^\\w+@([a-zA-Z]+\\.)+[a-zA-Z]+$";
        //此方法是完整匹配
        if (content.matches(regStr)) {
            System.out.println("格式正确");
        }else{
            System.out.println("格式不正确");
        }
    }
}
