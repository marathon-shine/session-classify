package com.netease.ysf.shine.doc2vec;

import com.alibaba.fastjson.JSONObject;
import com.netease.ysf.shine.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


public class Doc2VecUtil {
    public static void doc2VecTraning(String tranInputFile, String modelFileName) throws IOException {
        SentenceIterator iter = new LineSentenceIterator(new File(Constants.fileBase + tranInputFile));
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        // ParagraphVectors training configuration
        ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.25)
                .minLearningRate(0.01)
                .batchSize(1000)
                .epochs(5)
                .iterate(iter)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();

        // Start model training
        paragraphVectors.fit();

        WordVectorSerializer.writeParagraphVectors(paragraphVectors, Constants.fileBase + modelFileName);

        double[] vec = paragraphVectors.inferVector("尺码 北 挽 帮到 麻烦 查询 平时 运动鞋 41 请稍等 查询 请 耐心 等待 选大 一码 脚长 好像 255 260 帮 尺码 表 40.5 退亲 这款 支持 天无 理由 退货 41 页面 卖 4140.5").toDoubleVector();
        System.out.println(JSONObject.toJSONString(vec));

//        doc2vec( paragraphVectors, "new_message_694916_parsed.txt.simple", "new_message_694917_parsed.txt.simple");
    }

    public static void loadModelAndGetVec() throws IOException {
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        ParagraphVectors paragraphVectors = WordVectorSerializer.readParagraphVectors(Constants.fileBase + "doc2VecModel.bin");
        paragraphVectors.setTokenizerFactory(tokenizerFactory);
        double[] vec = paragraphVectors.inferVector("尺码 北 挽 帮到 麻烦 查询 平时 运动鞋 41 请稍等 查询 请 耐心 等待 选大 一码 脚长 好像 255 260 帮 尺码 表 40.5 退亲 这款 支持 天无 理由 退货 41 页面 卖 4140.5").toDoubleVector();
        System.out.println(JSONObject.toJSONString(vec));
    }

    public static void doc2vec(String modelFileName, String... inputfiles) throws IOException {
        ParagraphVectors paragraphVectors = WordVectorSerializer.readParagraphVectors(Constants.fileBase + modelFileName);

        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        paragraphVectors.setTokenizerFactory(tokenizerFactory);

        for(String oneFile : inputfiles) {
            File inputFile = new File(Constants.fileBase + oneFile );
            File outputFile = new File(Constants.fileBase + oneFile + ".vec.txt");
            LineIterator it = FileUtils.lineIterator(inputFile, "UTF-8");
            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    System.out.println(line);
                    try {
                        double[] vec = paragraphVectors.inferVector(line).toDoubleVector();
                        System.out.println(JSONObject.toJSONString(vec));
                        FileUtils.writeStringToFile(outputFile, JSONObject.toJSONString(vec)+"\n", Charset.defaultCharset(), true);
                    } catch (Exception e) {
                        System.out.println("----------------------------------");
                    }
                }
            } finally {
                it.close();
            }
        }
    }


    public static void fetchDoc2VecTranFile(String inputFileName) throws IOException {
        int countForEachType = 10000;

        String[] targetSessionType = Constants.overll10000SessionType.split(",");
        Map<String, Integer> targetSessionTypeCounter = new HashMap<>();
        for (int i = 0; i < targetSessionType.length; i++) {
            targetSessionTypeCounter.put(targetSessionType[i], 0);
        }

        File inputFile = new File(Constants.fileBase + inputFileName );
        File outputFile = new File(Constants.fileBase + inputFileName + ".doc2vec.tran.txt");
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
                        FileUtils.writeStringToFile(outputFile, split[1]+"\n", Charset.defaultCharset(), true);
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

    public static void main(String[] args) throws IOException {
//        doc2VecTraning("parsed_words.txt.simple", "doc2VecModel.bin");
//        doc2vec( "doc2VecModel.bin", "new_message_694916_parsed.txt.simple", "new_message_694917_parsed.txt.simple");
//        loadModelAndGetVec();
        fetchDoc2VecTranFile( "parsed_all_session.txt" );
    }
}
