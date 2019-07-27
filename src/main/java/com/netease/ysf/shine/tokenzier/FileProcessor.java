package com.netease.ysf.shine.tokenzier;

import com.netease.ysf.shine.Constants;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;


public class FileProcessor {

    public static void main(String[] args) throws IOException {
//        mergeFile("mergedSegmentedFile.txt", "new_message_694916_parsed.txt", "new_message_694917_parsed.txt");
//        mergeClassificationTranFile("mergedClassificationTranFile.txt",
//                new Pair<>("694916", "new_message_694916_parsed.txt.simple.vec.txt"),
//                new Pair<>("694917", "new_message_694917_parsed.txt.simple.vec.txt")
//        );

        fileStatistics("2018101112AllSession.txt");
    }

    public static void fileStatistics(String inputFileName) throws IOException {
        String spllitter = "#####";
        File inputFile = new File(Constants.fileBase + inputFileName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        Map<String, Integer> counter = new HashMap<>();
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if(line!=null && line.contains(spllitter)){
                    String[] splitted = line.split(spllitter);
                    String key = splitted[0];
                    if(!counter.containsKey(key)) {
                        counter.put(key, 0);
                    }
                    counter.put(key, counter.get(key)+1);
                }
            }
        } finally {
            it.close();
        }

        //这里将map.entrySet()转换成list
        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(counter.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator< Map.Entry<String,Integer> >() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });

        for(Map.Entry<String,Integer> mapping:list){
            if( mapping.getValue() > 10000 ) {
//                System.out.println(mapping.getKey() + ":" + mapping.getValue());
                System.out.println(mapping.getKey());
            }
        }
    }

    public static void mergeClassificationTranFile(String outputFileName, Pair<String, String>... files) throws IOException {
        File outputFile = new File(Constants.fileBase + outputFileName);

        for(Pair<String, String> oneFile : files) {
            String type = oneFile.getKey();
            File inputFile = new File(Constants.fileBase + oneFile.getValue() );
            LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    FileUtils.writeStringToFile(outputFile, type + ":" + line+"\n", Charset.defaultCharset(), true);
                }
            } finally {
                it.close();
            }
        }
    }

    public static void mergeFile(String outputFileName, String... files) throws IOException {
        File outputFile = new File(Constants.fileBase + outputFileName);

        for(String oneFile : files) {
            File inputFile = new File(Constants.fileBase + oneFile );
            LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    FileUtils.writeStringToFile(outputFile, line+"\n", Charset.defaultCharset(), true);
                }
            } finally {
                it.close();
            }
        }
    }
}
