package com.netease.ysf.shine.chi;

import com.netease.ysf.shine.util.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class VectorCalculator {

    private static final Logger logger = Logger.getLogger(VectorCalculator.class);

    private final int keptTermCountEachLabel = 30;

    private String filePath;
    private Context context;

    public VectorCalculator(String filePath) {
        this.filePath = filePath;
    }

    public Context getContext() {
        if (context == null) {
            context = new Context();
            System.out.println("process file....");
            processFile();
            System.out.println("start to calculate vector...");
            vectorCalculate();
            System.out.println("calculate finish...");
        }
        return context;
    }

    private void processFile() {
        File inputFile = new File(filePath);
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(inputFile, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();

                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
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

    private void parseLine(String line) {
        if (StringUtils.isEmpty(line) || !line.contains(Constants.LABEL_SPLITTER)) {
            return;
        }

        String[] lineArr = line.split(Constants.LABEL_SPLITTER);
        if (lineArr.length != 2) {
            return;
        }

        logger.debug("parsing file line ...");

        String lable = lineArr[0];
        String doc = lineArr[1];

        // 处理docCount
        context.getDocCount().incrementAndGet();
        // 处理labelDocTable
        Set<String> docSet = context.getLabelDocTable().get(lable);
        if (docSet == null) {
            docSet = new HashSet<>();
            context.getLabelDocTable().put(lable, docSet);
        }
        docSet.add(doc);
        // 处理labbledTable
        Map<String, Set<String>> labbledMap = context.getLabelledTable().get(lable);
        if (labbledMap == null) {
            labbledMap = new HashMap<>();
            context.getLabelledTable().put(lable, labbledMap);
        }
        // 处理wordDocTable
        String[] wordArr = doc.split(Constants.WORD_SPLITTER, -1);
        for (String word : wordArr) {
            if (word.trim().length() == 0) {
                continue;
            }
            Set<String> wordDocSet = context.getWordDocTable().get(word);
            if (wordDocSet == null) {
                wordDocSet = new HashSet<>();
                context.getWordDocTable().put(word, wordDocSet);
            }
            wordDocSet.add(doc);

            // 处理labbledTable
            Set<String> labelledDocSet = labbledMap.get(word);
            if (labelledDocSet == null) {
                labelledDocSet = new HashSet<>();
                labbledMap.put(word, labelledDocSet);
            }
            labelledDocSet.add(doc);
        }
    }


    public void vectorCalculate() {
        // compute CHI value for selecting feature terms
        for (String label : context.getLabelDocTable().keySet()) {
            // for each label, compute CHI vector
            logger.info("Compute CHI for: label=" + label);
            processOneLabel(label);
        }

        // sort and select CHI vectors
        Map<String, Map<String, Double>> vectorTable = context.getVectorTable();
        for (String label : vectorTable.keySet()) {
            Map<String, Double> vectorMap = vectorTable.get(label);
            List<VectorInfo> sortedList = sort(vectorMap);
            logger.info("CHI terms after sort: label=" + label + ", termCount=" + sortedList.size());
            context.getLableVector().put(label, sortedList.size() > keptTermCountEachLabel ? sortedList.subList(0, keptTermCountEachLabel) : sortedList);
        }
    }

    @SuppressWarnings("unchecked")
    private List<VectorInfo> sort(Map<String, Double> terms) {
        Map.Entry<String, Double>[] a = new Map.Entry[terms.size()];
        List<VectorInfo> sortList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : terms.entrySet()) {
            sortList.add(new VectorInfo(entry.getKey(), entry.getValue()));
        }
        Collections.sort(sortList, (Comparator<VectorInfo>) (o1, o2) -> (o2.getChi().compareTo(o1.getChi())));
        return sortList;
    }

    private void processOneLabel(String label) {
        for (String word : context.getWordDocTable().keySet()) {
            // A: doc count containing the word in this label
            int docCountContainingWordInLabel = 0;
            Map<String, Set<String>> labelledSet = context.getLabelledTable().get(label);
            if (labelledSet != null) {
                Set<String> labelledDocSet = labelledSet.get(word);
                if (labelledDocSet != null) {
                    docCountContainingWordInLabel = labelledDocSet.size();
                }
            }

            if (docCountContainingWordInLabel == 0) {
                continue;// 不包含这个词就不需要这个向量值
            }

            // B: doc count containing the word not in this label
            int docCountContainingWordNotInLabel = 0;
            Set<String> docContainsWord = context.getWordDocTable().get(word);
            if (docContainsWord != null) {
                docCountContainingWordNotInLabel = docContainsWord.size() - docCountContainingWordInLabel;
            }

            // C: doc count not containing the word in this label
            int docCountNotContainingWordInLabel = 0;
            Set<String> docContaingInLable = context.getLabelDocTable().get(label);
            if (docContaingInLable != null) {
                docCountNotContainingWordInLabel = docContaingInLable.size() - docCountContainingWordInLabel;
            }

            // D: doc count not containing the word not in this label
            int docCountNotContainingWordNotInLabel = context.getDocCount().get() + docCountContainingWordInLabel;
            if (docContainsWord != null) {
                docCountNotContainingWordNotInLabel = docCountNotContainingWordNotInLabel - docContainsWord.size();
            }
            if (docContaingInLable != null) {
                docCountNotContainingWordNotInLabel = docCountNotContainingWordNotInLabel - docContaingInLable.size();
            }

            // compute CHI value
            BigDecimal N = new BigDecimal(context.getDocCount().get());
            BigDecimal A = new BigDecimal(docCountContainingWordInLabel);
            BigDecimal B = new BigDecimal(docCountContainingWordNotInLabel);
            BigDecimal C = new BigDecimal(docCountNotContainingWordInLabel);
            BigDecimal D = new BigDecimal(docCountNotContainingWordNotInLabel);
            BigDecimal temp = A.multiply(D).subtract(B.multiply(C));
//            double chi = (double) N*temp*temp / (A+C)*(A+B)*(B+D)*(C+D);
            BigDecimal chi = N.multiply(temp).multiply(temp).divide((A.add(C)).multiply(A.add(B)).multiply(B.add(D)).multiply(C.add(D)), 10, RoundingMode.CEILING);
//            logger.info(String.format("Compute CHI for: label=%s, chi=%s", word, chi));

            Map<String, Double> vectorMap = context.getVectorTable().get(label);
            if (vectorMap == null) {
                vectorMap = new HashMap<>();
                context.getVectorTable().put(label, vectorMap);
            }
            vectorMap.put(word, chi.doubleValue());
        }
    }
}
