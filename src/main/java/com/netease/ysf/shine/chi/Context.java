package com.netease.ysf.shine.chi;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 词袋模型数据存储
 */
public class Context {

    private final AtomicInteger docCount = new AtomicInteger();
    // Map<类别, Set<文档>>
    private final Map<String, Set<String>> labelDocTable = new LinkedHashMap<>();
    //  Map<词, Set<文档>>
    private final Map<String, Set<String>> wordDocTable = new LinkedHashMap<>();
    //  Map<类别, Map<词, 文档>>
    private final Map<String, Map<String, Set<String>>> labelledTable = new LinkedHashMap<>();
    // Map<词, 词信息>
    private final Map<String, Map<String, Double>> vectorTable = new LinkedHashMap<>();

    private final Map<String, List<VectorInfo>> lableVector = new LinkedHashMap<>();

    public AtomicInteger getDocCount() {
        return docCount;
    }

    public Map<String, Set<String>> getLabelDocTable() {
        return labelDocTable;
    }

    public Map<String, Set<String>> getWordDocTable() {
        return wordDocTable;
    }

    public Map<String, Map<String, Set<String>>> getLabelledTable() {
        return labelledTable;
    }

    public Map<String, Map<String, Double>> getVectorTable() {
        return vectorTable;
    }

    public Map<String, List<VectorInfo>> getLableVector() {
        return lableVector;
    }
}
