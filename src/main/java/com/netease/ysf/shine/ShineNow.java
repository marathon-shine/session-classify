package com.netease.ysf.shine;

import com.netease.ysf.shine.classify.AbstractClassifier;
import com.netease.ysf.shine.classify.bayes.NaiveBayesClassifier;
import com.netease.ysf.shine.doc2vec.BagOfWordsUtil;
import com.netease.ysf.shine.tokenzier.JiebaCutter;
import com.netease.ysf.shine.util.GetTypeInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShineNow {

    private static final String MODEL_FILE = "/work/marathon/model_bayes.bin";

    private AbstractClassifier bayesClassifier;

    public ShineNow() throws IOException, ClassNotFoundException {
        loadClassifier(MODEL_FILE);
    }

    public static void main(String[] args) throws Exception {
        ShineNow shineNow = new ShineNow();
        System.out.println("------> 随便来 <-------");
        while (true) {
            shineNow.predict();
        }
    }

    private void predict() throws IOException {
        System.out.println(" >>>>>> 输入一大段话");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String text = br.readLine();
        // 分词
        String cutted = JiebaCutter.cutWord(text);
        // 词袋
        double[] vector = BagOfWordsUtil.getVector(cutted);
        // 分类
        int category = bayesClassifier.getCategory(vector);
        System.out.println("分类是：" + GetTypeInfo.getTypeInfo(String.valueOf(category)));
    }
    private void loadClassifier(String modelFile) throws IOException, ClassNotFoundException {
        bayesClassifier = new NaiveBayesClassifier(modelFile);
    }
}
