package com.netease.ysf.shine.classify.bayes;

import com.netease.ysf.shine.util.Constants;
import com.netease.ysf.shine.classify.AbstractClassifier;
import smile.classification.NaiveBayes;

public class NaiveBayesClassifier extends AbstractClassifier {

    private static final int CATEGORY_AMOUNT = Constants.CATEGORY_AMOUNT;
    private static final int DIMENSION = Constants.DIMENSION;

   private NaiveBayes bayes = new NaiveBayes(NaiveBayes.Model.MULTINOMIAL, CATEGORY_AMOUNT, DIMENSION);

    @Override
    public void learn(double[] vector, int category) {
        bayes.learn(vector, category);
    }

    @Override
    public int predict(double[] vector) {
        return bayes.predict(vector);
    }
}
