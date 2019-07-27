package com.netease.ysf.shine.tokenzier;

import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.netease.ysf.shine.util.Constants;
import com.netease.ysf.shine.util.SortUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class WordSegment {

    public static void typedKeywordsStatics(String inputFileName, String outputFileName) throws IOException {
        File inputFile = new File(Constants.fileBase + inputFileName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        Map<String,Map<String, Long>> typedWordCount = new HashMap<>();

        try {
            int indexer = 0;
            while (it.hasNext()) {
                try {
                    String line = it.nextLine();
                    indexer++;

                    String[] split = line.split(Constants.spllitter);
                    String type = split[0];
                    String content = split[1];
                    if(!typedWordCount.containsKey(type)){
                        typedWordCount.put(type, new HashMap<>());
                    }
                    Map<String, Long> wordCount = typedWordCount.get(type);

                    List<String> keyWords = HanLP.extractKeyword(content, 10);
                    keyWords.forEach( key -> {
                        if( !wordCount.containsKey(key) ) {
                            wordCount.put(key, 0L);
                        }
                        wordCount.put(key, wordCount.get(key)+1);
                    });

                    if( indexer%10 == 0 ) {
                        System.out.println("indexer: " + indexer);
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        } finally {
            it.close();
        }

        File outputFile = new File(Constants.fileBase + outputFileName);
        typedWordCount.keySet().forEach( type -> {
            //这里将map.entrySet()转换成list
            List<Map.Entry<String,Long>> list = SortUtil.sortMap(typedWordCount.get(type));
            for(Map.Entry<String,Long> mapping : list){
                String oneStr = type + ":" + mapping.getKey() + ":" + mapping.getValue();
                try {
                    FileUtils.writeStringToFile(outputFile, oneStr+"\n", Charset.defaultCharset(), true);
                } catch (IOException e) {
                    // Ignore
                }
            }
        } );
    }

    public static void keyWordsStatics(String inputFileName) throws IOException {
        File inputFile = new File(Constants.fileBase + inputFileName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        Map<String, Long> wordCount = new HashMap<>();

        try {
            int indexer = 0;
            while (it.hasNext()) {
                try {
                    String line = it.nextLine();
                    indexer++;
                    List<String> keyWords = HanLP.extractKeyword(line, 10);
                    keyWords.forEach( key -> {
                        if( !wordCount.containsKey(key) ) {
                            wordCount.put(key, 0L);
                        }
                        wordCount.put(key, wordCount.get(key)+1);
                    });
                    if( indexer%10 == 0 ) {
                        System.out.println("indexer: " + indexer);
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        } finally {
            it.close();
        }

        //这里将map.entrySet()转换成list
        List<Map.Entry<String,Long>> list = SortUtil.sortMap(wordCount);

        for(Map.Entry<String,Long> mapping : list){
            System.out.println(mapping.getKey() + ":" + mapping.getValue());
        }
    }

    public static void wordStatistics(String inputFileName) throws IOException {
        File inputFile = new File(Constants.fileBase + inputFileName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        Map<String, Long> wordCount = new HashMap<>();
        try {
            while (it.hasNext()) {
                try {
                    String line = it.nextLine();
                    String[] splitted = line.split(" ");
                    if(splitted!=null && splitted.length>0) {
                        for (int i = 0; i < splitted.length; i++) {
                            String key = splitted[i];
                            if( !wordCount.containsKey(key) ) {
                                wordCount.put(key, 0L);
                            }
                            wordCount.put(key, wordCount.get(key)+1);
                        }
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        } finally {
            it.close();
        }

        List<Map.Entry<String,Long>> list = SortUtil.sortMap(wordCount);
        for(Map.Entry<String,Long> mapping : list){
            if( mapping.getValue() > 1 ) {
                System.out.println(mapping.getKey() + ":" + mapping.getValue());
            }
        }
    }

    public static void wordSegment() throws IOException {
        String inputFileName = "new_message_694917.txt";
        String outputFileName = "new_message_694917_parsed.txt";

        File inputFile = new File(Constants.fileBase + inputFileName );
        File outputFile = new File(Constants.fileBase + outputFileName);
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if( null!=line && line.length()>0 && line.charAt(0)>='0' && line.charAt(0)<='9' && line.contains("###")){
                    String[] lineSplit = line.split("###");
                    String content = lineSplit[lineSplit.length-1];

                    List<String> keyWords = HanLP.extractKeyword(content, 10);
                    List<String> summary = HanLP.extractSummary(content, 2);
                    List<String> segment = HanLP.newSegment().seg(content).stream().map(one->one.word).collect(Collectors.toList());

                    System.out.println( );
                    System.out.println("~~~~~~原始~~~~~~" + content );
                    System.out.println("-----关键词-----" +  JSON.toJSONString( keyWords ) );
                    System.out.println("******概要******" +  JSON.toJSONString( summary ) );
                    System.out.println("@@@@@@分词@@@@@@" + JSON.toJSONString( segment ) );

                    StringBuilder oneStr = new StringBuilder();
                    segment.forEach( one -> oneStr.append(one).append(" "));
                    FileUtils.writeStringToFile(outputFile, oneStr.toString()+"\n", Charset.defaultCharset(), true);
                }
            }
        } finally {
            it.close();
        }
    }

    public static void main(String[] args) throws IOException {
//        wordSegment();
//        wordStatistics("parsed_all_session.notype.txt");
        keyWordsStatics("parsed_all_session.notype.txt");
//        typedKeywordsStatics("parsed_all_session.txt.doc2vec.tran.withtype.txt", "TypedTopKeyWords.txt");
    }
}
