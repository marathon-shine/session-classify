package com.netease.ysf.shine;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Cutter {

    /**
     * 修改python脚本路径，必须为绝对路径
     */
    private static final String path = "/Users/wanghuan/Desktop/cut/main.py";

    public static void main(String[] args) {
        System.out.println(cutWord("南京市长江大桥"));
    }

    public static String cutWord(String content) {
        Process proc;
        try {
            // 输入
            String[] args1 = new String[] { "python", path, content};
            // 执行py文件
            proc = Runtime.getRuntime().exec(args1);

            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            List<String> outputs = in.lines().collect(Collectors.toList());
            String parsed = "";
            if (outputs != null && outputs.size() > 0) {
                parsed = outputs.get(0);
            }

            in.close();
            proc.waitFor();
            return parsed;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
