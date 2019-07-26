package com.netease.ysf.shine.tokenzier;

import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.netease.ysf.shine.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


public class FileProcessor {

    public static void main(String[] args) throws IOException {
        mergeFile("mergedSegmentedFile.txt", "new_message_694916_parsed.txt", "new_message_694917_parsed.txt");
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
