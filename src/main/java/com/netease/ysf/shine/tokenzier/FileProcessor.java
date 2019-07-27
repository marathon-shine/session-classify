package com.netease.ysf.shine.tokenzier;

import com.netease.ysf.shine.util.Constants;
import com.netease.ysf.shine.util.GetTypeInfo;
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

//        fileStatistics("parsed_all_session.txt.doc2vec.tran.txt");

        splitTranAndTestFile(
                "5000.txt",
                0.8d);

//        fetchTranFile("parsed_all_session.withtype.txt", false);
    }

    public static void fetchTranFile(String inputFileName, boolean withType) throws IOException {
        int countForEachType = 5000;

        String[] targetSessionType = Constants.overll10000SessionType.split(",");
        Map<String, Integer> targetSessionTypeCounter = new HashMap<>();
        for (int i = 0; i < targetSessionType.length; i++) {
            targetSessionTypeCounter.put(targetSessionType[i], 0);
        }

        File inputFile = new File(Constants.fileBase + inputFileName );
        File outputFile;
        File labelFile = null;
        if(withType){
            outputFile = new File(Constants.fileBase + inputFileName + ".training.withtype.txt");
        }else {
            outputFile = new File(Constants.fileBase + inputFileName + ".training.notype.txt");
            labelFile = new File(Constants.fileBase + inputFileName + ".training.label.txt");
        }
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");

        int index = 0;
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                String[] split = line.split(Constants.spllitter);
                index++;
                if(split.length > 1) {
                    if (targetSessionTypeCounter.containsKey(split[0]) && targetSessionTypeCounter.get(split[0]) < countForEachType) {
                        targetSessionTypeCounter.put( split[0], targetSessionTypeCounter.get(split[0]) + 1 );
                        String content = withType ? line : split[1];
                        FileUtils.writeStringToFile(outputFile, content+"\n", Charset.defaultCharset(), true);
                        if(!withType){
                            FileUtils.writeStringToFile(labelFile, split[0]+"\n", Charset.defaultCharset(), true);
                        }
                    }
                }
                if( index % 1000 == 0 ) {
                    System.out.println( "Index: " + index );
                }
            }
        } finally {
            it.close();
        }
    }

    public static void splitTranAndTestFile(String inputFileName, double percentOfTranSet) throws IOException {
        File inputFile = new File(Constants.fileBase + inputFileName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");

        final int AMOUNT_BY_CATEGORY = 5000;

        File outputFileTran = new File(Constants.fileBase + inputFileName + ".trainingSet.txt");
        File outputFileTest = new File(Constants.fileBase + inputFileName + ".testingSet.txt");

        Set<String> filterred = new HashSet<>();
        filterred.add("1228523");
        filterred.add("973944");
        filterred.add("698732");
        filterred.add("747990");
        filterred.add("1236167");
        filterred.add("694980");
        filterred.add("920518");
        filterred.add("1236180");
        filterred.add("1235155");
        filterred.add("787122");
        Map<String, Integer> store = new HashMap<>();
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                String[] splitted = line.split(Constants.spllitter);
                String type = splitted[0];
                if (filterred.contains(type)) {
                    continue;
                }
                int count = store.getOrDefault(type, 0);
                if (count < AMOUNT_BY_CATEGORY * (1 - percentOfTranSet)) {
                    store.put(type, count + 1);
                    FileUtils.writeStringToFile(outputFileTest, line+"\n", Charset.defaultCharset(), true);
                } else {
                    FileUtils.writeStringToFile(outputFileTran, line+"\n", Charset.defaultCharset(), true);
                }
            }
        } finally {
            it.close();
        }
    }

    public static void fileStatistics(String inputFileName) throws IOException {
        File inputFile = new File(Constants.fileBase + inputFileName );
        LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
        Map<String, Integer> counter = new HashMap<>();
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if(line!=null && line.contains(Constants.spllitter)){
                    String[] splitted = line.split(Constants.spllitter);
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

        int matchedCount = 0;
        for(Map.Entry<String,Integer> mapping:list){
            if( mapping.getValue() >= 10000 ) {
                System.out.println(mapping.getKey() + ":" + mapping.getValue());
//                System.out.println(mapping.getKey());
                matchedCount += mapping.getValue();
            }
        }
        System.out.println( "Mattched: " + matchedCount );
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
