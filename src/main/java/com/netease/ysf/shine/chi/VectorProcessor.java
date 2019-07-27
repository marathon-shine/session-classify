package com.netease.ysf.shine.chi;

import com.netease.ysf.shine.util.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VectorProcessor {

    private static final Logger logger = Logger.getLogger(VectorProcessor.class);

    public static void main(String[] args) throws IOException {
        try {
            logger.info("start to running...");
            processFile("output_session_part1.txt", "vector_calculate_result.txt");
            logger.info("process end!");
        } catch (Exception e) {
            logger.info("process error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processFile(String inputFileName, String outputFileName) {
        VectorCalculator calculator = new VectorCalculator(Constants.CHI_FILE_PATH + inputFileName);
        Context context = calculator.getContext();
//        logger.info(JSON.toJSONString(context));

        File outputFile = new File(Constants.CHI_FILE_PATH + outputFileName);
        if (outputFile.exists()) {
            outputFile.delete();
            outputFile = new File(Constants.CHI_FILE_PATH + outputFileName);
        }

        StringBuilder builder = new StringBuilder();
        for (String lable : context.getLableVector().keySet()) {
            List<VectorInfo> vectorTable = context.getLableVector().get(lable);
            if (vectorTable == null) {
                continue;
            }
            builder.setLength(0);

            builder.append(lable).append(Constants.LABEL_SPLITTER);
            for (VectorInfo vectorInfo : vectorTable) {
                builder.append(vectorInfo.getWord()).append(Constants.VECTOR_SPLITTER).append(vectorInfo.getChi()).append(Constants.WORD_SPLITTER);
            }

            try {
                logger.info("print vector result: " + builder.substring(0, builder.length() - Constants.WORD_SPLITTER.length()));
                FileUtils.writeStringToFile(outputFile, builder.toString() + "\n", "UTF-8", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
