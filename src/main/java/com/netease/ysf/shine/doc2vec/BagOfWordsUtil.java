package com.netease.ysf.shine.doc2vec;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.netease.ysf.shine.Constants;
import com.netease.ysf.shine.SortUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class BagOfWordsUtil {

    public static void main(String[] args) throws IOException {
//        getVecBaseWords();
        genBagOfWordsVec(
                "parsed_all_session.txt.doc2vec.tran.withtype.txt",
                loadBagOfWords("TopKeyWords.txt", 2000),
                "topKeyword"
        );
    }

    public static List<String> loadBagOfWords(String input, int limit) throws IOException {
//        String bagOfWordsMeta = "BagOfWordsMeta.txt";
        // Load TopFreqWords
        File inputFileTopFreq = new File(Constants.fileBase + input );
        LineIterator itTopFreq = FileUtils.lineIterator(inputFileTopFreq, "UTF-8");
        List<String> bagOfWords = new ArrayList<>();
        try {
            while (itTopFreq.hasNext()) {
                try {
                    String line = itTopFreq.nextLine();
                    String[] split = line.split(":");
                    String keyword = split[0];
                    if(keyword.length() > 0) {
                        bagOfWords.add(keyword);
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        } finally {
            itTopFreq.close();
        }
        List<String> result = bagOfWords.subList(bagOfWords.size()-limit, bagOfWords.size());
        return result;
    }

    public static void genBagOfWordsVec(String input, List<String> bagOfWords, String fileType) throws IOException {
        // Load TopFreqWords
        File inputFile = new File(Constants.fileBase + input );
        File outputFile = new File(Constants.fileBase +  input + "." + fileType +".bagofwords.vec.txt" );
        LineIterator iterator = FileUtils.lineIterator(inputFile, "UTF-8");
        try {
            int counter = 0;
            while (iterator.hasNext()) {
                try {
                    String line = iterator.nextLine();
                    counter++;

                    String[] split = line.split(Constants.spllitter);
                    String type = split[0];
                    String[] content = split[1].split(" ");
                    Set<String> contentSet = Sets.newHashSet(content);

                    Integer[] vec = new Integer[bagOfWords.size()];
                    for (int i = 0; i < bagOfWords.size(); i++) {
                        if( contentSet.contains( bagOfWords.get(i) ) ) {
                            vec[i] = 1;
                        }else {
                            vec[i] = 0;
                        }
                    }
                    String oneLine =  type + Constants.spllitter + JSONObject.toJSONString(vec);
                    FileUtils.writeStringToFile(outputFile, oneLine+"\n", Charset.defaultCharset(), true);

                    if(counter%10 == 0){
                        System.out.println("Counter: " + counter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            iterator.close();
        }
    }

    // 全局关键词合集中 - 分类关键词的交集
    public static void getVecBaseWords() throws IOException {
        String topFreqWordsFileName = "TopFreqWords.txt";
        String topKeyWordsFileName = "TopKeyWords.txt";
        String topTypedKeyWordsFileName = "TypedTopKeyWords.txt";
        // 出现在多少个分类中的词，认为是表意能力不够强的
        int excludeThreshold = 10;
        int topKeyMinCount = 8;
        int topFreqMinCount = 10;

        // Load TopFreqWords
        File inputFileTopFreq = new File(Constants.fileBase + topFreqWordsFileName );
        LineIterator itTopFreq = FileUtils.lineIterator(inputFileTopFreq, "UTF-8");
        Set<String> topFreq = new HashSet<>();
        try {
            while (itTopFreq.hasNext()) {
                try {
                    String line = itTopFreq.nextLine();
                    String[] split = line.split(":");
                    String keyword = split[0];
                    String count = split[1];
                    if(!topFreq.contains(keyword) & Integer.parseInt(count)>topFreqMinCount) {
                        topFreq.add(keyword);
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        } finally {
            itTopFreq.close();
        }

        // Load TopFreqWords
        File inputFileTopKey = new File(Constants.fileBase + topKeyWordsFileName );
        LineIterator itTopKey = FileUtils.lineIterator(inputFileTopKey, "UTF-8");
        Map<String, Long> topKey = new HashMap<>();
        try {
            while (itTopKey.hasNext()) {
                try {
                    String line = itTopKey.nextLine();
                    String[] split = line.split(":");
                    String keyword = split[0];
                    String count = split[1];
                    if(!topKey.containsKey(keyword) & Long.parseLong(count)>topKeyMinCount) {
                        topKey.put(keyword, Long.parseLong(count));
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        } finally {
            itTopKey.close();
        }

        // Load TypedTopKeyWords
        File inputFile = new File(Constants.fileBase + topTypedKeyWordsFileName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        Map<String,Map<String, Long>> typedWordCount = new HashMap<>();
        try {
            while (it.hasNext()) {
                try {
                    String line = it.nextLine();
                    String[] split = line.split(":");
                    String type = split[0];
                    String keyword = split[1];
                    String count = split[2];
                    if(!typedWordCount.containsKey(type)){
                        typedWordCount.put(type, new HashMap<>());
                    }
                    Map<String, Long> wordCount = typedWordCount.get(type);
                    wordCount.put(keyword, Long.parseLong(count));
                } catch (Exception e) {
                    // Ignore
                }
            }
        } finally {
            it.close();
        }

        Map<String, Long> output = new HashMap<>();
        topKey.keySet().forEach(one -> {
            if (inTypeCount(typedWordCount, one) <= excludeThreshold) {
                output.put( one, topKey.get(one) );
            }
        });
        List<Map.Entry<String,Long>> sortedOutput = SortUtil.sortMap(output);
        sortedOutput.forEach( one-> {
            if(one.getValue() > topKeyMinCount) {
                System.out.println(one.getKey() + ": " + one.getValue());
            }
        } );
    }

    private static int inTypeCount(Map<String,Map<String, Long>> typedWordCount, String word) {
        int count = 0;
        List<String> keys = new ArrayList<>();
        keys.addAll( typedWordCount.keySet() );
        for (int i=0; i<keys.size(); i++) {
            String oneType = keys.get(i);
            if(typedWordCount.get(oneType).containsKey(word)) {
                count++;
            }
        }
        return count;
    }
}
