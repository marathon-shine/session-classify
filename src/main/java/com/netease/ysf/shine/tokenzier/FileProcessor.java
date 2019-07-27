package com.netease.ysf.shine.tokenzier;

import com.netease.ysf.shine.Constants;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


public class FileProcessor {

    public static void main(String[] args) throws IOException {
//        mergeFile("mergedSegmentedFile.txt", "new_message_694916_parsed.txt", "new_message_694917_parsed.txt");
        mergeClassificationTranFile("mergedClassificationTranFile.txt",
                new Pair<>("694916", "new_message_694916_parsed.txt.simple.vec.txt"),
                new Pair<>("694917", "new_message_694917_parsed.txt.simple.vec.txt")
        );
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
