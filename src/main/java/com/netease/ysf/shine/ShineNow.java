package com.netease.ysf.shine;

import com.netease.ysf.shine.classify.IClassifier;
import com.netease.ysf.shine.classify.bayes.NaiveBayesClassifier;

import java.io.*;

public class ShineNow {

    private IClassifier bayesClassifier;

    public ShineNow() throws IOException, ClassNotFoundException {
        loadClassifier("");
    }
    public static void main(String[] args) throws Exception {
        ShineNow shineNow = new ShineNow();
        System.out.println("------> 随便来 <-------");

    }

    private void predict() throws IOException {
        System.out.println(" >>>>>> 输入一大段话");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String text = br.readLine();
        // 分词
        // 词袋
        // 分类
    }
    private void loadClassifier(String modelFile) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(modelFile);
            ois = new ObjectInputStream(fis);
            bayesClassifier = (NaiveBayesClassifier) ois.readObject();
        }  finally {
            if (ois != null) {
                ois.close();
            }
        }
    }
}
