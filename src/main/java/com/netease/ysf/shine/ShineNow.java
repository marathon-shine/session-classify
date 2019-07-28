package com.netease.ysf.shine;

import com.alibaba.fastjson.JSONObject;
import com.netease.ysf.shine.classify.AbstractClassifier;
import com.netease.ysf.shine.classify.bayes.NaiveBayesClassifier;
import com.netease.ysf.shine.doc2vec.BagOfWordsUtil;
import com.netease.ysf.shine.tokenzier.JiebaCutter;
import com.netease.ysf.shine.util.Constants;
import com.netease.ysf.shine.util.GetTypeInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShineNow {

    private static final String MODEL_FILE = Constants.fileBase + "model_bayes.bin";

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
        if(text.length() > 0) {
            // 分词
            String cutted = JiebaCutter.cutWord(text);
            System.out.println("分词结果：" + cutted);
            // 词袋
            double[] vector = BagOfWordsUtil.getVector(cutted);
            System.out.println("词袋向量：" + JSONObject.toJSONString(vector));
            // 分类
            int[] indexes = bayesClassifier.predictTop(vector);
            System.out.println("分类可能是：");
            for (int index : indexes) {
                int category = bayesClassifier.getCategory(index);
                System.out.print(GetTypeInfo.getTypeInfo(String.valueOf(category)));
                System.out.print(" / ");
            }
            System.out.println();
        }

    }
    private void loadClassifier(String modelFile) throws IOException, ClassNotFoundException {
        bayesClassifier = new NaiveBayesClassifier(modelFile);
    }
}
