package com.netease.ysf.shine.classify.bayes;

import com.netease.ysf.shine.classify.Util;

import java.io.File;
import java.io.IOException;

public class NaiveBayesTest {

    private static final String TRAIN_FILE = "/work/marathon/dataset/5000.txt.trainingSet.txt";
    private static final String TEST_FILE = "/work/marathon/dataset/5000.txt.testingSet.txt";

    private static final String MODEL_FILE = "/work/marathon/model_bayes.bin";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(MODEL_FILE);
//        classifier.learn(new File(TRAIN_FILE));
//        classifier.saveModel(MODEL_FILE);
        Util.printStatistics(classifier.predict(new File(TEST_FILE)));
    }
}
