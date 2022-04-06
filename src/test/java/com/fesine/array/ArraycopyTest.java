package com.fesine.array;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/7/14
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/7/14
 */
public class ArraycopyTest {

    public static void main(String[] args) {
        //int[] a = new int[10];
        //a[0] = 0;
        //a[1] = 1;
        //a[2] = 2;
        //a[3] = 3;
        //a[4] = 4;
        //a[5] = 5;
        //a[6] = 6;
        //for (int i : a) {
        //    System.out.print(i + " ");
        //}
        //System.out.println();
        //int[] d = new int[10];
        //System.arraycopy(a, 2, d, 7, 3);
        //for (int i : d) {
        //    System.out.print(i+" ");
        //}
        //System.out.println();
        //int[] b = new int[3];
        //b[0] = 0;
        //b[1] = 1;
        //b[2] = 2;
        //int[] c = Arrays.copyOf(b, 10);
        //for (int i : c) {
        //    System.out.print(i + " ");
        //}
        String urlV6 = "f6:00:1d:29:0b:01:8080";
        int pos = urlV6.indexOf(":");
        String ports = urlV6.substring(pos);

        Integer port = Integer.valueOf(ports+1);
        System.out.println();
    }
}
