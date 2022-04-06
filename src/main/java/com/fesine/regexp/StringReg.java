package com.fesine.regexp;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2022/4/4
 * @update:修改内容
 * @author: fesine
 * @updateTime:2022/4/4
 */
public class StringReg {
    public static void main(String[] args) {
        String content = "hello test14234JDK1.3 good33333 morJDK1.4 1122 ning5225 8998.";
        content = content.replaceAll("JDK1\\.([34])", "JDK");
        System.out.println(content);
        content = "13988889999";
        //整体匹配
        if (content.matches("13[89]\\d{8}")) {
            System.out.println("验证成功");
        }else{
            System.out.println("验证失败");
        }
        content = "hello#world@good!java-杭州23中~国";
        String[] split = content.split("#|@|!|-|\\d+|~");
        for (String s : split) {
            System.out.println(s);
        }
    }
}
