package org.acme.model;

import java.util.List;

public class Load {
    private List<Double> throughput;
    private List<Double> rss;
    private List<Double> throughputDensity;
    private Double avThroughput;
    private Double avMaxRss;
    private Double maxThroughputDensity;

    public List<Double> getThroughput() {
        return throughput;
    }

    public void setThroughput(List<Double> throughput) {
        this.throughput = throughput;
    }

    public List<Double> getRss() {
        return rss;
    }

    public void setRss(List<Double> rss) {
        this.rss = rss;
    }

    public List<Double> getThroughputDensity() {
        return throughputDensity;
    }

    public void setThroughputDensity(List<Double> throughputDensity) {
        this.throughputDensity = throughputDensity;
    }

    public Double getAvThroughput() {
        return avThroughput;
    }

    public void setAvThroughput(Double avThroughput) {
        this.avThroughput = avThroughput;
    }

    public Double getAvMaxRss() {
        return avMaxRss;
    }

    public void setAvMaxRss(Double avMaxRss) {
        this.avMaxRss = avMaxRss;
    }

    public Double getMaxThroughputDensity() {
        return maxThroughputDensity;
    }

    public void setMaxThroughputDensity(Double maxThroughputDensity) {
        this.maxThroughputDensity = maxThroughputDensity;
    }
}
