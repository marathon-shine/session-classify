package com.netease.ysf.shine.classify;

import lombok.Data;

@Data
public class Statistics {

    private int tp;
    private int tn;
    private int fp;
    private int fn;

    public void incrTp() {
        ++tp;
    }

    public void incrTn() {
        ++tn;
    }

    public void incrFp() {
        ++fp;
    }

    public void incrFn() {
        ++fn;
    }

    public String statistics() {
        float recallRate = tp * 1.0f / (tp + fn);
        float preciousRate = tp * 1.0f / (tp + fp);
        float accuracyRate = tp * 1.0f / (tp + tn + fp + fn);

        return "召回率：" + (recallRate * 100) + "% \n"
                + "精确率：" + (preciousRate * 100) + "% \n"
                + "准确率：" + (accuracyRate * 100) + "% \n"
                + "tp: " + tp + "\n"
                + "fp: " + fp + "\n"
                + "fn: " + fn + "\n";
    }
}
