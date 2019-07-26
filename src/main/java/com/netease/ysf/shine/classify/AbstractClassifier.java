package com.netease.ysf.shine.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractClassifier implements IClassifier {

    public void learn(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            DataLine dataLine = null;
            while ((dataLine = Util.readFromFile(reader)) != null) {
                learn(dataLine.getVector(), dataLine.getCategory());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Statistics> predict(File file) {
        Map<Integer, Statistics> statisticsMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            DataLine dataLine = null;
            while ((dataLine = Util.readFromFile(reader)) != null) {
                int category = predict(dataLine.getVector());
                if (category == dataLine.getCategory()) {
                    Statistics statistics = getOrPut(statisticsMap, category);
                    statistics.incrTp();
                } else {
                    Statistics statistics = getOrPut(statisticsMap, category);
                    statistics.incrFp();
                    statistics = getOrPut(statisticsMap, dataLine.getCategory());
                    statistics.incrFn();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statisticsMap;
    }

    private Statistics getOrPut(Map<Integer, Statistics> statisticsMap, Integer category) {
        Statistics statistics = statisticsMap.get(category);
        if (statistics == null) {
            statistics = new Statistics();
            statisticsMap.put(category, statistics);
        }
        return statistics;
    }
}
