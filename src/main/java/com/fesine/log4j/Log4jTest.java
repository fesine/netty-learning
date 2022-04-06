package com.fesine.log4j;

import org.apache.log4j.Logger;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/11/4
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/11/4
 */
public class Log4jTest {
    private static final  Logger logger = Logger.getLogger(Log4jTest.class);

    public static void main(String[] args) {
        logger.trace("------->trace");
        logger.debug("------->debug");
        logger.info("------->info");
        //logger.warn("------->warn");
        //logger.error("------->error");
    }
}
