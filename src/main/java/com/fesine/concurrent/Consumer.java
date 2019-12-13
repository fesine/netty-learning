package com.fesine.concurrent;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/13
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/13
 */
public class Consumer implements Runnable {

    private ShareResource shareResource;

    public Consumer(ShareResource shareResource) {
        this.shareResource = shareResource;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            shareResource.popup();
        }

    }
}
