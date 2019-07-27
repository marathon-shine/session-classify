package com.netease.ysf.shine.chi;

public class VectorInfo {

    private String word;
    private Double chi;

    public VectorInfo(String word, Double chi) {
        this.word = word;
        this.chi = chi;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getChi() {
        return chi;
    }

    public void setChi(Double chi) {
        this.chi = chi;
    }
}
