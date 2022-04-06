package com.fesine.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2022/4/3
 * @update:修改内容
 * @author: fesine
 * @updateTime:2022/4/3
 */
public class RegExp01 {

    public static void main(String[] args) {
        //\u0391 \uffe5
        String regexp = "\\S*[\\u0391-\\uffe5\\d@]+\\S*(?<!([vV]\\d{1,2}))$";
        regexp("interface", regexp);
        regexp("v1", regexp);
        regexp("v12", regexp);
        regexp("中文1v1", regexp);
        regexp("中文1v12", regexp);
        regexp("Cvmv1", regexp);
        regexp("CvmV12", regexp);
        regexp("v123", regexp);
        regexp("CvmV123", regexp);
        regexp("v1user123", regexp);
        regexp("v999_user_123", regexp);
        regexp("v9.abc.v123", regexp);
        regexp("user123123", regexp);
        regexp("USER123123", regexp);
        regexp("20220301", regexp);
        regexp("2022-03-01", regexp);
        regexp("2022_03_01", regexp);
        regexp("中文", regexp);
        regexp("中-文", regexp);
        regexp("中_文", regexp);
        regexp("中文_1", regexp);
        regexp("中文-1", regexp);
        regexp("中文1", regexp);
        regexp("user@qq.com", regexp);
    }

    private static void regexp(String content,String regexp){
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            System.out.println("找到："+matcher.group(0));
        }else{
            System.out.println("当前正则无法匹配："+ content);

        }
    }
}
