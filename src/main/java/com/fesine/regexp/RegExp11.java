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
public class RegExp11 {
    public static void main(String[] args) {
        String content = "https://www.bilibili.com/video/BV1Eq4y1E79W?p=15&spm_id_from=333.1007.top_right_bar_window_history.content.click";
        String regStr = "^(https?://)?([\\w-]+\\.)+[\\w-]+(\\/[\\w?.=&-/%#]*)?$";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            System.out.println("满足格式");
        }
    }
}
