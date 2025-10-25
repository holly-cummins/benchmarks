package org.acme.model;

import java.util.List;

public class Build {
    private List<Double> timings;
    private NativeBuild nativeBuild;  // maps the "native" JSON object
    private double avBuildTime;
    private Double avNativeRSS;       // optional
    private String classCount;
    private String fieldCount;
    private String methodCount;
    private String reflectionClassCount;
    private String reflectionFieldCount;
    private String reflectionMethodCount;

    public List<Double> getTimings() {
        return timings;
    }

    public void setTimings(List<Double> timings) {
        this.timings = timings;
    }

    public NativeBuild getNativeBuild() {
        return nativeBuild;
    }

    public void setNativeBuild(NativeBuild nativeBuild) {
        this.nativeBuild = nativeBuild;
    }

    public double getAvBuildTime() {
        return avBuildTime;
    }

    public void setAvBuildTime(double avBuildTime) {
        this.avBuildTime = avBuildTime;
    }

    public Double getAvNativeRSS() {
        return avNativeRSS;
    }

    public void setAvNativeRSS(Double avNativeRSS) {
        this.avNativeRSS = avNativeRSS;
    }

    public String getClassCount() {
        return classCount;
    }

    public void setClassCount(String classCount) {
        this.classCount = classCount;
    }

    public String getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(String fieldCount) {
        this.fieldCount = fieldCount;
    }

    public String getMethodCount() {
        return methodCount;
    }

    public void setMethodCount(String methodCount) {
        this.methodCount = methodCount;
    }

    public String getReflectionClassCount() {
        return reflectionClassCount;
    }

    public void setReflectionClassCount(String reflectionClassCount) {
        this.reflectionClassCount = reflectionClassCount;
    }

    public String getReflectionFieldCount() {
        return reflectionFieldCount;
    }

    public void setReflectionFieldCount(String reflectionFieldCount) {
        this.reflectionFieldCount = reflectionFieldCount;
    }

    public String getReflectionMethodCount() {
        return reflectionMethodCount;
    }

    public void setReflectionMethodCount(String reflectionMethodCount) {
        this.reflectionMethodCount = reflectionMethodCount;
    }
}
