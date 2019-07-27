package com.netease.ysf.shine.classify;

import com.netease.ysf.shine.util.CategoryIndexCache;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractClassifier implements IClassifier {

    private CategoryIndexCache categoryIndexCache = new CategoryIndexCache();

    public void learn(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            DataLine dataLine = null;
            int progress = 0;
            while ((dataLine = Util.readFromFile(reader)) != null) {
                learn(dataLine.getVector(), categoryIndexCache.getIndexOf(dataLine.getCategory()));
                printProgress(progress++, 240000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCategory(double[] vector) {
        int index = predict(vector);
        Integer category = categoryIndexCache.getCategoryOf(index);
        return category == null ? -1 : category;
    }

    public int getCategory(int index) {
        Integer category = categoryIndexCache.getCategoryOf(index);
        return category == null ? -1 : category;
    }

    private void printProgress(int progress, int total) {
        if (progress % 500  == 0) {
            System.out.println("progress: " + progress + " / " + total);
        }
    }

    public Map<Integer, Statistics> predict(File file) {
        Map<Integer, Statistics> statisticsMap = new HashMap<>();
        Statistics global = new Statistics();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            DataLine dataLine = null;
            while ((dataLine = Util.readFromFile(reader)) != null) {
                int index = predict(dataLine.getVector());
                Integer category = categoryIndexCache.getCategoryOf(index);
                if (category == null) {
                    System.out.println("unrecognized category, index: " + index + ", origin: " + dataLine.getCategory());
                    Statistics statistics = getOrPut(statisticsMap, dataLine.getCategory());
                    statistics.incrFn();
                    global.incrFn();
                } else if (category == dataLine.getCategory()) {
                    Statistics statistics = getOrPut(statisticsMap, category);
                    statistics.incrTp();
                    global.incrTp();
                } else {
                    Statistics statistics = getOrPut(statisticsMap, category);
                    statistics.incrFp();
                    statistics = getOrPut(statisticsMap, dataLine.getCategory());
                    statistics.incrFn();
                    global.incrFp();
                }
            }
            statisticsMap.put(-1, global);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statisticsMap;
    }

    protected void saveModel(String file, Object model) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            oos.writeObject(categoryIndexCache);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Object loadModel(String modelFile) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(modelFile);
            ois = new ObjectInputStream(fis);
            Object model =  ois.readObject();
            categoryIndexCache = (CategoryIndexCache) ois.readObject();
            return model;
        }  finally {
            if (ois != null) {
                ois.close();
            }
        }
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
