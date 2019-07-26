package com.netease.ysf.shine.classify;

import java.io.*;
import java.util.Map;

public class Util {

    public static void printStatistics(Map<Integer, Statistics> statisticsMap) {
        for (Map.Entry<Integer, Statistics> entrySet : statisticsMap.entrySet()) {
            System.out.println("--------category: " + entrySet.getKey() + "-----------");
            System.out.println(entrySet.getValue().statistics());
        }
    }

    public static DataLine readFromFile(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        return fromLine(line);
    }

    private static DataLine fromLine(String line) {
        String[] splits = line.split(":");
        if (splits.length != 2) {
            return null;
        }
        int category = Integer.parseInt(splits[0]);
        String[] vectorStrs = splits[1].substring(1, splits[1].length() - 1).split(",");
        double[] vector = new double[vectorStrs.length];
        for (int i = 0; i < vector.length; ++i) {
            vector[i] = Double.parseDouble(vectorStrs[i]);
        }

        DataLine dataLine = new DataLine();
        dataLine.setCategory(category);
        dataLine.setVector(vector);
        return dataLine;
    }
}
