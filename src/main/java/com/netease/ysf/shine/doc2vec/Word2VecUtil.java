package com.netease.ysf.shine.doc2vec;

import com.netease.ysf.shine.util.Constants;
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
    static String modelFileName = "word2VecModel.bin";

    public static void word2Vec(String inputFileName) throws IOException {
        SentenceIterator iter = new LineSentenceIterator( new File(Constants.fileBase + inputFileName ) );

        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        Word2Vec vec1 = new Word2Vec.Builder()
                .minWordFrequency(5)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();
        vec1.fit();


        /*
         * at this moment we're supposed to have model built, and it can be saved for future use.
         */
        WordVectorSerializer.writeWord2VecModel(vec1, Constants.fileBase + modelFileName);
        /*
         * Let's assume that some time passed, and now we have new corpus to be used to weights update.
         * Instead of building new model over joint corpus, we can use weights update mode.
         */
        Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel(Constants.fileBase + modelFileName);


        Collection<String> lst = word2Vec.wordsNearest("面膜", 10);
        System.out.println(lst);

        double cosSim = word2Vec.similarity("面膜", "眼霜");
        double cosSim2 = word2Vec.similarity("面膜", "快递");
        System.out.println(cosSim + "---" + cosSim2);
    }

    public static Word2Vec loadModel() {
        Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel(Constants.fileBase + modelFileName);
        return word2Vec;
    }

    public static double calcuateSimilarity( Word2Vec word2Vec, String input, String input2 ) {
        double cosSim = word2Vec.similarity(input, input2);
        return cosSim;
    }

    public static void queryRelatedWords(Word2Vec word2Vec, String... input) {

        for(int i=0; i<input.length; i++) {
            Collection<String> lst = word2Vec.wordsNearest(input[i], 20);
            System.out.println("Related to: " + input[i] + " -> " + lst);
        }
    }

    public static void main(String[] args) throws IOException {
//        word2Vec("parsed_all_session.withtype.txt.training.notype.txt");
        queryRelatedWords(loadModel(),"面膜", "尺寸", "快递", "尿不湿");
    }

}
