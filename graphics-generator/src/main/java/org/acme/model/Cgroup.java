package org.acme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cgroup {
    @JsonProperty("mem_max")
    private String memMax;
    private String name;
    private String cpu;

    public String getMemMax() {
        return memMax;
    }

    public void setMemMax(String memMax) {
        this.memMax = memMax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
}
