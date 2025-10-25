package org.acme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

    @JsonProperty("QUARKUS_VERSION")
    private String quarkusVersion;

    @JsonProperty("CMD_PREFIX")
    private String cmdPrefix;

    @JsonProperty("NATIVE_QUARKUS_BUILD_OPTIONS")
    private String nativeQuarkusBuildOptions;

    @JsonProperty("NATIVE_SPRING_BUILD_OPTIONS")
    private String nativeSpringBuildOptions;

    @JsonProperty("JVM_MEMORY")
    private String jvmMemory;

    @JsonProperty("repo")
    private Repo repo;

    @JsonProperty("GRAALVM_VERSION")
    private String graalvmVersion;

    @JsonProperty("profiler")
    private Profiler profiler;

    @JsonProperty("JAVA_VERSION")
    private String javaVersion;

    public String getQuarkusVersion() {
        return quarkusVersion;
    }

    public void setQuarkusVersion(String quarkusVersion) {
        this.quarkusVersion = quarkusVersion;
    }

    public String getCmdPrefix() {
        return cmdPrefix;
    }

    public void setCmdPrefix(String cmdPrefix) {
        this.cmdPrefix = cmdPrefix;
    }

    public String getNativeQuarkusBuildOptions() {
        return nativeQuarkusBuildOptions;
    }

    public void setNativeQuarkusBuildOptions(String nativeQuarkusBuildOptions) {
        this.nativeQuarkusBuildOptions = nativeQuarkusBuildOptions;
    }

    public String getNativeSpringBuildOptions() {
        return nativeSpringBuildOptions;
    }

    public void setNativeSpringBuildOptions(String nativeSpringBuildOptions) {
        this.nativeSpringBuildOptions = nativeSpringBuildOptions;
    }

    public String getJvmMemory() {
        return jvmMemory;
    }

    public void setJvmMemory(String jvmMemory) {
        this.jvmMemory = jvmMemory;
    }

    public Repo getRepo() {
        return repo;
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    public String getGraalvmVersion() {
        return graalvmVersion;
    }

    public void setGraalvmVersion(String graalvmVersion) {
        this.graalvmVersion = graalvmVersion;
    }

    public Profiler getProfiler() {
        return profiler;
    }

    public void setProfiler(Profiler profiler) {
        this.profiler = profiler;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public Cgroup getCgroup() {
        return cgroup;
    }

    public void setCgroup(Cgroup cgroup) {
        this.cgroup = cgroup;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    @JsonProperty("cgroup")
    private Cgroup cgroup;

    @JsonProperty("SPRING_BOOT_VERSION")
    private String springBootVersion;

}
