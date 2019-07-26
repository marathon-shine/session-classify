package com.netease.ysf.shine.tokenzier;

import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.netease.ysf.shine.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class WordSegment {
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
        wordSegment();
    }
}
