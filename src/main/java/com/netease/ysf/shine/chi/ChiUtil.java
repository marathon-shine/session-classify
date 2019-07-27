package com.netease.ysf.shine.chi;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
import com.netease.ysf.shine.doc2vec.Word2VecUtil;
import com.netease.ysf.shine.tokenzier.JiebaCutter;
import com.netease.ysf.shine.util.Constants;
import com.netease.ysf.shine.util.GetTypeInfo;
import com.netease.ysf.shine.util.SortUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class ChiUtil {
    static String chiFilePathName = "chi_vector_calculate_result.txt";


    public static void main(String[] args) throws IOException {
        String type = calculateTypeByDistance("请问有什么需要帮您呢发错货了，能不能换货亲爱的用户，您方便提供一下您的相关订单号吗 （您输入框左下方“+”处就可以看到您的订单哦）您点击发送给小考拉就可以了呢拍的精华，发的乳液能换吗方便提供下照片吗  小考拉去看看");
        System.out.println( GetTypeInfo.getTypeInfo(type) );
    }

    public static String calculateTypeByDistance(String input) throws IOException {
        Word2Vec word2Vec = Word2VecUtil.loadModel();
        Map<String, Set<String>> chiMap = chiFileParser();
        List<String> keyword =  HanLP.extractKeyword(input, 10);
        System.out.println(JSONObject.toJSONString(keyword));

        Map<String, Double> typeDistance = new HashMap<>();

        chiMap.keySet().forEach( type -> {
            typeDistance.put(type, 0D);
            Set<String> typeKeys = chiMap.get(type);
            for (int i = 0; i < keyword.size(); i++) {
                String thisKey = keyword.get(i);
                typeKeys.forEach( thisTypeKey -> {
                    double distance = Word2VecUtil.calcuateSimilarity(word2Vec, thisKey, thisTypeKey);
                    typeDistance.put(type, typeDistance.get(type) + distance);
                });
            }
        });

        List<Map.Entry<String,Double>> sorted = SortUtil.sortDoubleMap(typeDistance);
        return sorted.get(sorted.size()-1).getKey();
    }

    public static Map<String, Set<String>> chiFileParser() throws IOException {
        File inputFile = new File(Constants.fileBase + chiFilePathName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        Map<String, Set<String>> typeWords = new HashMap<>();
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                String[] split = line.split(Constants.spllitter);
                String type = split[0];
                if(GetTypeInfo.typeSet().contains(type)) {
                    String[] words = split[1].split(" ");
                    if ( !typeWords.containsKey(type) ) {
                        typeWords.put(type, new HashSet<>());
                    }
                    for (int i = 0; i < words.length; i++) {
                        typeWords.get(type).add(words[i]);
                    }
                }
            }
        } finally {
            it.close();
        }
        return typeWords;
    }
}
