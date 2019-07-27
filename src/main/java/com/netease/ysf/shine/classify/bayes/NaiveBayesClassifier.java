package com.netease.ysf.shine.classify.bayes;

import com.netease.ysf.shine.classify.AbstractClassifier;
import com.netease.ysf.shine.util.Constants;

import java.io.IOException;

public class NaiveBayesClassifier extends AbstractClassifier {

    private static final int CATEGORY_AMOUNT = Constants.CATEGORY_AMOUNT;
    private static final int DIMENSION = Constants.DIMENSION;

    private NaiveBayes bayes;

    public NaiveBayesClassifier() {
        bayes = new NaiveBayes(NaiveBayes.Model.MULTINOMIAL, CATEGORY_AMOUNT, DIMENSION);
    }

    public NaiveBayesClassifier(String modelFile) throws IOException, ClassNotFoundException {
        bayes = (NaiveBayes) loadModel(modelFile);
    }

    @Override
    public void learn(double[] vector, int category) {
        bayes.learn(vector, category);
    }

    @Override
    public int predict(double[] vector) {
        return bayes.predict(vector);
    }

    @Override
    public int[] predictTop(double[] vector) {
        return bayes.predictTop(vector);
    }

    public void saveModel(String modelFile) {
        saveModel(modelFile, bayes);
    }
}
