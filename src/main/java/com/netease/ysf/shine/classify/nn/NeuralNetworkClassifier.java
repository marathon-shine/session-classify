package com.netease.ysf.shine.classify.nn;

import com.netease.ysf.shine.classify.AbstractClassifier;
import smile.classification.NeuralNetwork;

public class NeuralNetworkClassifier extends AbstractClassifier {

    NeuralNetwork neuralNetwork = new NeuralNetwork(NeuralNetwork.ErrorFunction.LEAST_MEAN_SQUARES, 3,4,5,4,3);

    @Override
    public void learn(double[] vector, int category) {
        neuralNetwork.learn(vector, category);
    }

    @Override
    public int predict(double[] vector) {
        return neuralNetwork.predict(vector);
    }
}
