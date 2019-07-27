package com.netease.ysf.shine.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CategoryIndexCache implements Serializable {

    private Map<Integer, Integer> categoryIndexMap = new HashMap<>();
    private Map<Integer, Integer> indexCategoryMap = new HashMap<>();
    private Integer categoryCount = 0;

    public int getIndexOf(Integer category) {
        Integer index = categoryIndexMap.get(category);
        if (index == null) {
            index = categoryCount++;
            categoryIndexMap.put(category, index);
            indexCategoryMap.put(index, category);
        }
        return index;
    }

    public Integer getCategoryOf(int index) {
        return indexCategoryMap.get(index);
    }
}
