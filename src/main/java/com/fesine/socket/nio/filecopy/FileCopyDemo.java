package com.fesine.socket.nio.filecopy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/6/1
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/6/1
 */
public class FileCopyDemo {
    private static final  int ROUNDS = 5;

    private static void benchmark(FileCopyRunner test, File source, File target) {
        long elapsed = 0L;
        for (int i = 0; i < ROUNDS; i++) {
            long start = System.currentTimeMillis();
            test.copyFile(source, target);
            elapsed += System.currentTimeMillis() - start;
            target.delete();
        }
        System.out.println(test + ": " + elapsed / ROUNDS);
    }
    public static void main(String[] args) {
        //使用流实现文件复制
        FileCopyRunner noBufferStreamCopy = new FileCopyRunner() {
            @Override
            public void copyFile(File source, File target) {
                FileInputStream fin = null;
                FileOutputStream fout = null;
                try {
                    fin = new FileInputStream(source);
                    fout = new FileOutputStream(target);
                    int result;
                    while ((result = fin.read()) != -1) {
                        fout.write(result);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(fin);
                    close(fout);
                }
            }

            @Override
            public String toString() {
                return "noBufferStreamCopy";
            }
        };
        //使用流实现文件复制
        FileCopyRunner bufferedStreamCopy = new FileCopyRunner() {
            @Override
            public void copyFile(File source, File target) {
                BufferedInputStream bin = null;
                BufferedOutputStream bout = null;
                try {
                    bin = new BufferedInputStream(new FileInputStream(source));
                    bout = new BufferedOutputStream(new FileOutputStream(target));
                    byte[] buffer = new byte[1024];
                    int result;
                    while ((result = bin.read(buffer)) != -1) {
                        bout.write(buffer, 0, result);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(bin);
                    close(bout);
                }
            }

            @Override
            public String toString() {
                return "bufferedStreamCopy";
            }
        };

        FileCopyRunner nioBufferCopy = new FileCopyRunner() {
            @Override
            public void copyFile(File source, File target) {
                FileChannel fin = null;
                FileChannel fout = null;
                try {
                    fin = new FileInputStream(source).getChannel();
                    fout = new FileOutputStream(target).getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while ((fin.read(buffer)) != -1) {
                        //转成读模式
                        buffer.flip();
                        //当buffer中还有数据时，一直读取buffer
                        while (buffer.hasRemaining()) {
                            fout.write(buffer);
                        }
                        //清空，转换成写模式
                        buffer.clear();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(fin);
                    close(fout);
                }
            }

            @Override
            public String toString() {
                return "nioBufferCopy";
            }
        };
        FileCopyRunner nioTransferCopy = new FileCopyRunner() {
            @Override
            public void copyFile(File source, File target) {
                FileChannel fin = null;
                FileChannel fout = null;
                try {
                    fin = new FileInputStream(source).getChannel();
                    fout = new FileOutputStream(target).getChannel();
                    long size = fin.size();
                    long transferTo = 0L;
                    while (transferTo != size) {
                        transferTo += fin.transferTo(0, size, fout);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(fin);
                    close(fout);
                }
            }

            @Override
            public String toString() {
                return "nioTransferCopy";
            }
        };

        File smallFile = new File("/var/tmp/smallFile");
        File smallFileCopy = new File("/var/tmp/smallFile-copy");
        System.out.println("----Copying small file----");
        benchmark(noBufferStreamCopy,smallFile,smallFileCopy);
        benchmark(bufferedStreamCopy,smallFile,smallFileCopy);
        benchmark(nioBufferCopy,smallFile,smallFileCopy);
        benchmark(nioTransferCopy,smallFile,smallFileCopy);

        File bigFile = new File("/var/tmp/bigFile");
        File bigFileCopy = new File("/var/tmp/bigFile-copy");
        System.out.println("----Copying big file----");
        //benchmark(noBufferStreamCopy,bigFile,bigFileCopy);
        benchmark(bufferedStreamCopy,bigFile,bigFileCopy);
        benchmark(nioBufferCopy,bigFile,bigFileCopy);
        benchmark(nioTransferCopy,bigFile,bigFileCopy);

        File hugeFile = new File("/var/tmp/hugeFile");
        File hugeFileCopy = new File("/var/tmp/hugeFile-copy");
        System.out.println("----Copying huge file----");
        //benchmark(noBufferStreamCopy,hugeFile,hugeFileCopy);
        benchmark(bufferedStreamCopy,hugeFile,hugeFileCopy);
        benchmark(nioBufferCopy,hugeFile,hugeFileCopy);
        benchmark(nioTransferCopy,hugeFile,hugeFileCopy);
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

interface FileCopyRunner{
    void copyFile(File source, File target);
}
