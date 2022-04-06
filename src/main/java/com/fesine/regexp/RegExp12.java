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
public class RegExp12 {
    public static void main(String[] args) {
        String content = "hello test14234 good33333 mor 1122 ning5225 8998.";
        //需要匹配连续相同的两个数字
        // String regStr = "(\\d)\\1";
        //找到连续5个相同的数字
        // String regStr = "(\\d)\\1{4}";
        // 匹配第一位与第四位相同 第二位与第三位相同的数字如5225 1331
        String regStr = "(\\d)(\\d)\\2\\1";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println("找到：" + matcher.group(0));
        }
    }
}
