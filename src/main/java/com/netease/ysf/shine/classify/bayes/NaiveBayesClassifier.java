package com.netease.ysf.shine.classify.bayes;

import com.netease.ysf.shine.classify.IClassifier;
import com.netease.ysf.shine.classify.Statistics;
import smile.classification.NaiveBayes;

import java.io.File;
import java.util.Map;
import java.util.Random;

public class NaiveBayesClassifier implements IClassifier {

    private static final int CLASS_AMOUNT= 1000;
    private static final int DIMENSION = 100;

   private NaiveBayes bayes = new NaiveBayes(NaiveBayes.Model.MULTINOMIAL, CLASS_AMOUNT, DIMENSION);

    @Override
    public void learn(File file) {

    }

    @Override
    public Map<Integer, Statistics> predict(File file) {
        return null;
    }
}
