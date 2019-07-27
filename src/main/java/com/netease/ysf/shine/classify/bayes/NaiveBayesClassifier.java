package com.netease.ysf.shine.classify.bayes;

import com.netease.ysf.shine.classify.AbstractClassifier;
import smile.classification.NaiveBayes;

public class NaiveBayesClassifier extends AbstractClassifier {

    private static final int CLASS_AMOUNT= 2;
    private static final int DIMENSION = 100;

   private NaiveBayes bayes = new NaiveBayes(NaiveBayes.Model.MULTINOMIAL, CLASS_AMOUNT, DIMENSION);

    @Override
    public void learn(double[] vector, int category) {
        bayes.learn(vector, category);
    }

    @Override
    public int predict(double[] vector) {
        return bayes.predict(vector);
    }
}
