package com.sff.leetcode.other;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserCSVFile {

    public static void main(String[] args) {

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {

            String path = "/Users/shifeifei/Work/Project/leetcode-cn/leetcode-other/src/main/resources/csv.txt";
            String outPath = "/Users/shifeifei/Work/Project/leetcode-cn/leetcode-other/src/main/resources/out-csv.txt";
            br = new BufferedReader(new FileReader(new File(path)));
            bw = new BufferedWriter(new FileWriter(new File(outPath)));


            String lineStr = "";
            while ((lineStr = br.readLine()) != null) {

                List<String> list = null;
                if (lineStr.indexOf("\"") == -1 || lineStr.lastIndexOf("\"") == -1) {
                    list = Arrays.asList(lineStr.split(","));
                    writeFile(list, bw);
                    continue;
                }
                String a = lineStr.substring(lineStr.indexOf("\""), lineStr.lastIndexOf("\"") + 1);
                String b = lineStr.substring(0, lineStr.indexOf("\"") - 1);
                String c = lineStr.substring(lineStr.lastIndexOf("\"") + 2, lineStr.length());

                List<String> listA = Arrays.asList(b.split(","));
                List<String> listB = Arrays.asList(a);
                List<String> listC = Arrays.asList(c.split(","));

                List<String> lineList = new ArrayList<>();
                lineList.addAll(listA);
                lineList.addAll(listB);
                lineList.addAll(listC);

                writeFile(lineList, bw);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeFile(List<String> list, BufferedWriter bw) throws Exception {

        StringBuffer sb = new StringBuffer();
        for (String str : list) {
            sb.append(str).append("\t\t");
        }
        bw.write(sb.toString() + "\n");
    }

}
