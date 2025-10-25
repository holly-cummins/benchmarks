package org.acme.model;

import java.util.List;

public class Startup {
    private List<Double> timings;
    private Double avStartTime;

    public List<Double> getTimings() {
        return timings;
    }

    public void setTimings(List<Double> timings) {
        this.timings = timings;
    }

    public Double getAvStartTime() {
        return avStartTime;
    }

    public void setAvStartTime(Double avStartTime) {
        this.avStartTime = avStartTime;
    }
}
