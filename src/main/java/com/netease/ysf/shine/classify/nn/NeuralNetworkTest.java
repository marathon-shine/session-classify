package com.netease.ysf.shine.classify.nn;

import com.netease.ysf.shine.classify.Util;

import java.io.File;

public class NeuralNetworkTest {

    private static final String TRAIN_FILE = "/work/marathon/dataset/train.txt";
    private static final String TEST_FILE = "/work/marathon/dataset/test.txt";

    public static void main(String[] args) {
        NeuralNetworkClassifier classifier = new NeuralNetworkClassifier();
        classifier.learn(new File(TRAIN_FILE));
        Util.printStatistics(classifier.predict(new File(TEST_FILE)));
    }

}
