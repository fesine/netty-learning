package com.fesine.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 域名解析
 * @author: fesine
 * @createTime:2022/4/4
 * @update:修改内容
 * @author: fesine
 * @updateTime:2022/4/4
 */
public class HomeWork03 {
    public static void main(String[] args) {
        String content = "http://www.baidu.com:8080/abc/def/index.html";
        String regStr = "^([a-zA-Z]+)://([\\w.]+):(\\d+)[\\w-/]*/([\\w.]+)$";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        if (matcher.matches()) {
            System.out.println("整体匹配成功="+matcher.group(0));
            System.out.println("协议="+matcher.group(1));
            System.out.println("域名="+matcher.group(2));
            System.out.println("端口="+matcher.group(3));
            System.out.println("文件="+matcher.group(4));

        }else{
            System.out.println("匹配失败");
        }
    }
}
