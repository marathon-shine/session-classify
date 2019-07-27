package com.netease.ysf.shine.classify.nn;

import com.netease.ysf.shine.util.Constants;
import com.netease.ysf.shine.classify.AbstractClassifier;
import smile.classification.NeuralNetwork;

public class NeuralNetworkClassifier extends AbstractClassifier {

    private NeuralNetwork neuralNetwork;

    public NeuralNetworkClassifier() {
        int[] numUnits = {Constants.DIMENSION, Constants.DIMENSION, Constants.CATEGORY_AMOUNT};
        neuralNetwork = new NeuralNetwork(NeuralNetwork.ErrorFunction.CROSS_ENTROPY, numUnits);
    }
    @Override
    public void learn(double[] vector, int category) {
        neuralNetwork.learn(vector, category);
    }

    @Override
    public int predict(double[] vector) {
        return neuralNetwork.predict(vector);
    }
}
