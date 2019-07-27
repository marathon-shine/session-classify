package com.netease.ysf.shine.chi;

import com.netease.ysf.shine.util.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LabelCalculator {

    private static final Logger logger = Logger.getLogger(LabelCalculator.class);

    private final Map<String, List<VectorInfo>> lableVector = new LinkedHashMap<>();

    public LabelCalculator(String vectorFilePath) {
        readVectorFile(vectorFilePath);
    }

    /**
     * 执行预测
     *
     * @param testFilePath
     * @return
     */
    public double predict(String testFilePath) {
        logger.info("start to predict...");
        Map<String, Map<String, Integer>> sourceInfo = readTestFile(testFilePath);
        int correctCount = 0;
        for (String lable : sourceInfo.keySet()) {
            Map<String, Integer> words = sourceInfo.get(lable);
            String predictLable = getPredictLable(words);
            if (lable.equalsIgnoreCase(predictLable)) {
                logger.info("label matched!");
                correctCount ++;
            }
        }
        return Float.valueOf(correctCount)/sourceInfo.size();
    }

    private Map<String, Map<String, Integer>> readTestFile(String testFilePath) {
        Map<String, Map<String, Integer>> sourceInfo = new HashMap<>();
        File inputFile = new File(testFilePath);
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(inputFile, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();

                readLineAndAddResult(line, sourceInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (it != null) {
                try {
                    it.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sourceInfo;
    }

    private void readLineAndAddResult(String line, Map<String, Map<String, Integer>> sourceInfo) {
        if (StringUtils.isEmpty(line) || !line.contains(Constants.LABEL_SPLITTER)) {
            return;
        }

        String[] lineArr = line.split(Constants.LABEL_SPLITTER);
        if (lineArr.length != 2) {
            return;
        }

        String lable = lineArr[0];
        String[] wordArr = lineArr[1].split(Constants.WORD_SPLITTER);

        Map<String, Integer> wordMap = new HashMap<>();
        for (String word : wordArr) {
            Integer count = wordMap.get(word);
            if (count == null) {
                count = 0;
            }
            wordMap.put(word, count + 1);
        }

        if (wordMap.size() > 0) {
            sourceInfo.put(lable, wordMap);
        }
    }

    private String getPredictLable(Map<String, Integer> words) {
        double maxScore = 0;
        String predictLable = null;
        for (String lable : lableVector.keySet()) {
            double score = calculateScore(lableVector.get(lable), words);
            if (score > maxScore) {
                maxScore = score;
                predictLable = lable;
            }
        }
        return predictLable;
    }

    private double calculateScore(List<VectorInfo> vectorInfos, Map<String, Integer> words) {
        double score = 0;
        Integer count;
        for (VectorInfo vectorInfo : vectorInfos) {
            count = words.get(vectorInfo.getWord());
            if (count != null) {
                score += vectorInfo.getChi() * count;// count 根据情况选择要不要
            }
        }
        return score;
    }

    private void readVectorFile(String vectorFilePath) {
        logger.info("read vector info...");
        File inputFile = new File(vectorFilePath);
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(inputFile, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();

                readLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (it != null) {
                try {
                    it.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readLine(String line) {
        if (StringUtils.isEmpty(line) || !line.contains(Constants.LABEL_SPLITTER)) {
            return;
        }

        String[] lineArr = line.split(Constants.LABEL_SPLITTER);

        String lable = lineArr[0];
        String[] wordArr = lineArr[1].split(Constants.WORD_SPLITTER);

        List<VectorInfo> vectorList = new ArrayList<>();
        for (String vectorStr : wordArr) {
            String[] vectorArr = vectorStr.split(Constants.VECTOR_SPLITTER);
            if (vectorArr.length == 2) {
                vectorList.add(new VectorInfo(vectorArr[0], Double.valueOf(vectorArr[1])));
            }
        }

        if (vectorList.size() > 0) {
            lableVector.put(lable, vectorList);
        }
    }
}
