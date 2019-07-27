package com.netease.ysf.shine.chi;

import com.netease.ysf.shine.util.Constants;
import org.apache.log4j.Logger;

import java.io.IOException;

public class PredictProcessor {

    private static final Logger logger = Logger.getLogger(PredictProcessor.class);

    public static void main(String[] args) throws IOException {
        try {
            logger.info("start to running...");
            predict("vector_calculate_result.txt", "output_session_last_part1.txt");
            logger.info("process end!");
        } catch (Exception e) {
            logger.info("process error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void predict(String vectorFilePath, String testFilePath) {
        LabelCalculator labelCalculator = new LabelCalculator(Constants.CHI_FILE_PATH + vectorFilePath);

        double percent = labelCalculator.predict(Constants.CHI_FILE_PATH + testFilePath);
        logger.info("=================================================");
        logger.info(String.format("predict percent: %s", percent * 100));
        logger.info("=================================================");
    }
}
