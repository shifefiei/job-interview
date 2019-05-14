package com.sff.leetcode.other;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * 打印出文本文件的最后n行文本
 * <p>
 * 要求:
 * 请正确处理 test-files 目录中的测试文件
 * <p>
 * 本次程序能满足题目要求，但还有改进的地方：
 * 1.如果目录中还包含目录，需要再特殊处理
 * 2.异常处理需要再优化
 */
public class ReadFile {

    public static void main(String[] args) {
        String path = "/Users/shifeifei/Work/Project/leetcode-cn/leetcode-other/src/main/resources";
        read(path, 5);
    }

    /**
     * 读取文件
     *
     * @param path       文件目录
     * @param lineNumber 行数
     */
    private static void read(String path, int lineNumber) {

        File file = new File(path);
        if (!file.isDirectory()) {
            return;
        }


        File[] files = file.listFiles();

        for (File f : files) {

            if (!f.isDirectory()) {
                //里面是文件了
                readFile(f, lineNumber);
            }
        }

    }

    /**
     * 处理文本文件
     *
     * @param file       文本文件
     * @param lineNumber 最后n行
     */
    private static void readFile(File file, int lineNumber) {
        RandomAccessFile textFile = null;

        int countLine = 0;
        try {
            textFile = new RandomAccessFile(file, "r");
            long fileLength = textFile.length();

            if (fileLength == 0L) {
                return;
            }
            //初始化游标
            long pos = fileLength - 1;
            while (pos > 0) {
                pos--;
                textFile.seek(pos);

                if (textFile.readByte() == '\n') {
                    System.out.println(textFile.readLine());
                    countLine++;
                    if (countLine == lineNumber) {
                        break;
                    }
                }
            }

            if (pos == 0) {
                textFile.seek(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeTextResource(textFile);
        }
    }

    private static void closeTextResource(RandomAccessFile textFile) {

        if (null != textFile) {
            try {
                textFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
