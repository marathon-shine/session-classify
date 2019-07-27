package com.netease.ysf.shine.classify;

import com.netease.ysf.shine.CategoryIndexCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractClassifier implements IClassifier {

    private CategoryIndexCache categoryIndexCache = new CategoryIndexCache();

    public void learn(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            DataLine dataLine = null;
            while ((dataLine = Util.readFromFile(reader)) != null) {
                System.out.println("learn: " + dataLine.getCategory() + ", size: " + dataLine.getVector().length);
                learn(dataLine.getVector(), categoryIndexCache.getIndexOf(dataLine.getCategory()));
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
                int index = predict(dataLine.getVector());
                Integer category = categoryIndexCache.getCategoryOf(index);
                if (category == null) {
                    System.out.println("unrecognized category, index: " + index + ", origin: " + dataLine.getCategory());
                    Statistics statistics = getOrPut(statisticsMap, dataLine.getCategory());
                    statistics.incrFn();
                } else if (category == dataLine.getCategory()) {
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
