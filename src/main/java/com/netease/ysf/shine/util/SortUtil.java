package com.netease.ysf.shine.util;

import java.util.*;

public class SortUtil {

    public static List<Map.Entry<String,Long>> sortMap(Map<String, Long> wordCount) {
        //这里将map.entrySet()转换成list
        List<Map.Entry<String,Long>> list = new ArrayList<>(wordCount.entrySet());
        //然后通过比较器来实现排序
        //升序排序
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
        return list;
    }

    public static List<Map.Entry<String,Double>> sortDoubleMap(Map<String, Double> wordCount) {
        //这里将map.entrySet()转换成list
        List<Map.Entry<String,Double>> list = new ArrayList<>(wordCount.entrySet());
        //然后通过比较器来实现排序
        //升序排序
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
        return list;
    }
}
