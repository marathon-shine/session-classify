package com.netease.ysf.shine.classify.nn;

import com.netease.ysf.shine.classify.Util;

import java.io.File;

public class NeuralNetworkTest {

    private static final String TRAIN_FILE = "/work/marathon/dataset/top.keyword.train.txt";
    private static final String TEST_FILE = "/work/marathon/dataset/top.keyword.test.txt";
    private static final String MODEL_FILE = "/work/marathon/model_nn.bin";

    public static void main(String[] args) {
        NeuralNetworkClassifier classifier = new NeuralNetworkClassifier();
        classifier.learn(new File(TRAIN_FILE));
        classifier.saveModel(MODEL_FILE);
        Util.printStatistics(classifier.predict(new File(TEST_FILE)));
    }

}
