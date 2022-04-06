package com.fesine.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2022/4/4
 * @update:修改内容
 * @author: fesine
 * @updateTime:2022/4/4
 */
public class RegExp13 {
    public static void main(String[] args) {
        String content = "我我...要...学学学学...java！";
        Pattern pattern = Pattern.compile("\\.");
        Matcher matcher = pattern.matcher(content);
        //替换所有.号
        content = matcher.replaceAll("");
        System.out.println(content);
        content = Pattern.compile("(.)\\1+").matcher(content).replaceAll("$1");
        System.out.println(content);
    }
}
