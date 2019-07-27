package com.netease.ysf.shine.classify.bayes;

import com.netease.ysf.shine.classify.Util;

import java.io.File;

public class NaiveBayesTest {

    private static final String TRAIN_FILE = "/work/marathon/dataset/top_keyword_train.txt";
    private static final String TEST_FILE = "/work/marathon/dataset/top_keyword_test.txt";

    private static final String MODEL_FILE = "/work/marathon/model_bayes.bin";

    public static void main(String[] args) {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.learn(new File(TRAIN_FILE));
        classifier.saveModel(MODEL_FILE);
        Util.printStatistics(classifier.predict(new File(TEST_FILE)));
    }
}
