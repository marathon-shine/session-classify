package com.netease.ysf.shine.doc2vec;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class Word2VecUtil {

    public static void word2Vec() {
        String fileBase = "/Users/wangqi/Downloads/data/";
        String inputFileName = "segmentOut.txt";

        SentenceIterator iter = new LineSentenceIterator( new File(fileBase + inputFileName ) );

        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();
        vec.fit();

        // Write word vectors
//        WordVectorSerializer.writeWordVectors(vec, "pathToWriteto.txt");

        Collection<String> lst = vec.wordsNearest("面膜", 10);
        System.out.println(lst);

        double cosSim = vec.similarity("面膜", "眼霜");
        double cosSim2 = vec.similarity("面膜", "快递");
        System.out.println(cosSim + "---" + cosSim2);
    }

    public static void main(String[] args) throws IOException {
        word2Vec();
    }

}
