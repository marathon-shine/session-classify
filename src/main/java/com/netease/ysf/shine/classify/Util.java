package com.netease.ysf.shine.classify;

import com.netease.ysf.shine.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class Util {

    private static final double BASE = 0;
    public static void printStatistics(Map<Integer, Statistics> statisticsMap) {
        for (Map.Entry<Integer, Statistics> entrySet : statisticsMap.entrySet()) {
            System.out.println("--------category: " + entrySet.getKey() + "-----------");
            System.out.println(entrySet.getValue().statistics());
        }
    }

    public static DataLine readFromFile(BufferedReader reader) throws IOException {
        DataLine dataLine = null;
        do {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            dataLine = fromLine(line);
        } while (dataLine == null);
        return dataLine;
    }

    private static DataLine fromLine(String line) {
        String[] splits = line.split(Constants.VECTOR_SPLITTER);
        if (splits.length != 2) {
            return null;
        }
        int category = Integer.parseInt(splits[0]);
        String[] vectorStrs = splits[1].substring(1, splits[1].length() - 1).split(",");
        double[] vector = new double[vectorStrs.length];
        for (int i = 0; i < vector.length; ++i) {
            vector[i] = Double.parseDouble(vectorStrs[i]) + BASE;
        }

        DataLine dataLine = new DataLine();
        dataLine.setCategory(category);
        dataLine.setVector(vector);
        return dataLine;
    }

//    private static DataLine fromLine(String line) {
//        String[] splits = line.split("\t");
//        if (splits.length != 3) {
//            return null;
//        }
//        int category = Integer.parseInt(splits[0]);
//        double[] vector = new double[splits.length - 1];
//        for (int i = 0; i < vector.length; ++i) {
//            vector[i] = Double.parseDouble(splits[i + 1].trim()) + BASE;
//        }
//
//        DataLine dataLine = new DataLine();
//        dataLine.setCategory(category);
//        dataLine.setVector(vector);
//        return dataLine;
//    }
}
