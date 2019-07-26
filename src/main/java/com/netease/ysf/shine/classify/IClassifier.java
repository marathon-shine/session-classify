package com.netease.ysf.shine.classify;

import java.io.File;
import java.util.Map;

public interface IClassifier {

    void learn(File file);

    Map<Integer, Statistics> predict(File file);

}
