package com.netease.ysf.shine.doc2vec;

import com.netease.ysf.shine.Constants;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
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
    static String inputFileName = "segmentOut.txt";
    static String modelFileName = "word2VecModel.bin";

    public static void word2Vec() throws IOException {
        SentenceIterator iter = new LineSentenceIterator( new File(Constants.fileBase + inputFileName ) );

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


        /*
         * at this moment we're supposed to have model built, and it can be saved for future use.
         */
        WordVectorSerializer.writeWord2VecModel(vec, Constants.fileBase + modelFileName);
        /*
         * Let's assume that some time passed, and now we have new corpus to be used to weights update.
         * Instead of building new model over joint corpus, we can use weights update mode.
         */
        Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel(Constants.fileBase + modelFileName);


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
