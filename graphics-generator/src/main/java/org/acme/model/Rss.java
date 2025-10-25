package org.acme.model;

import java.util.List;

public class Rss {
    private List<Double> startup;
    private List<Double> firstRequest;
    private Double avStartupRss;
    private Double avFirstRequestRss;

    public List<Double> getStartup() {
        return startup;
    }

    public void setStartup(List<Double> startup) {
        this.startup = startup;
    }

    public List<Double> getFirstRequest() {
        return firstRequest;
    }

    public void setFirstRequest(List<Double> firstRequest) {
        this.firstRequest = firstRequest;
    }

    public Double getAvStartupRss() {
        return avStartupRss;
    }

    public void setAvStartupRss(Double avStartupRss) {
        this.avStartupRss = avStartupRss;
    }

    public Double getAvFirstRequestRss() {
        return avFirstRequestRss;
    }

    public void setAvFirstRequestRss(Double avFirstRequestRss) {
        this.avFirstRequestRss = avFirstRequestRss;
    }
}

