package com.fesine.regexp;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2022/4/5
 * @update:修改内容
 * @author: fesine
 * @updateTime:2022/4/5
 */
public class RegExpUrl {

    public static void main(String[] args) {
        String url = "https://www.baidu.com/#/api/v1/USR001/2022-04-05";
        String url1 = "https://www.baidu.com/#/api/v1/USR001/check";
        String template = "https://www.baidu.com/#/api/v1/{}/{}";
        // String regStr = "https?://[\\S\\.]+(com|cn)(/#)?";
        // Pattern pattern = Pattern.compile(regStr);
        // Matcher matcher = pattern.matcher(url);
        // if (matcher.find()) {
        //     String domain = matcher.group(0);
        //     String subPath = url.substring(domain.length());
        // }
    }
}
