package org.acme.model;


import io.quarkus.bootstrap.runner.Timing;

public class BenchmarkData {
    private Timing timing;
    private Results results;
    private Config config;

    public Timing getTiming() {
        return timing;
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
