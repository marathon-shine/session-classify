package com.netease.ysf.shine.doc2vec;

import com.alibaba.fastjson.JSONObject;
import com.netease.ysf.shine.Constants;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;


public class Doc2VecUtil {
    static String inputFileName = "mergedSegmentedFile.txt";
    static String modelFileName = "doc2VecModel.bin";

    public static void doc2VecTraning() {
        SentenceIterator iter = new LineSentenceIterator(new File(Constants.fileBase + inputFileName));
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        // ParagraphVectors training configuration
        ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .batchSize(10000)
                .epochs(10)
                .iterate(iter)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();

        // Start model training
        paragraphVectors.fit();

        WordVectorSerializer.writeWord2VecModel(paragraphVectors, Constants.fileBase + modelFileName);

        double[] vec = paragraphVectors.getWordVector("面膜");
        System.out.println(JSONObject.toJSONString(vec));
    }

    public static void main(String[] args) throws IOException {
        doc2VecTraning();
    }
}
