package org.acme.model;

import java.util.List;

public class NativeBuild {
    private List<Double> rss;
    private double binarySize;

    public List<Double> getRss() {
        return rss;
    }

    public void setRss(List<Double> rss) {
        this.rss = rss;
    }

    public double getBinarySize() {
        return binarySize;
    }

    public void setBinarySize(double binarySize) {
        this.binarySize = binarySize;
    }
}
