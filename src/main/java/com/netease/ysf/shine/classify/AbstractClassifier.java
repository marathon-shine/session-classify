package com.netease.ysf.shine.classify;

import com.netease.ysf.shine.util.CategoryIndexCache;

import java.io.*;
import java.util.*;

public abstract class AbstractClassifier implements IClassifier {

    private CategoryIndexCache categoryIndexCache = new CategoryIndexCache();

    public void learn(File file) {
        List<DataLine> dataLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            DataLine dataLine = null;
            while ((dataLine = Util.readFromFile(reader)) != null) {
                dataLines.add(dataLine);
            }
            Collections.shuffle(dataLines);
            int progress = 0;
            for (DataLine dataLine1 : dataLines) {
                learn(dataLine1.getVector(), categoryIndexCache.getIndexOf(dataLine1.getCategory()));
                printProgress(progress++, dataLines.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void saveModel(String file) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
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

    private Statistics getOrPut(Map<Integer, Statistics> statisticsMap, Integer category) {
        Statistics statistics = statisticsMap.get(category);
        if (statistics == null) {
            statistics = new Statistics();
            statisticsMap.put(category, statistics);
        }
        return statistics;
    }
}
