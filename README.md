# Benchmarks

This repository hosts results and generated graphics of benchmark executions from the Quarkus performance lab, along with the graphics code for generating charts.

Images from this repository have https://quarkus.io/benchmarks urls. To construct a URL, use the path of the image relative to the `images` subdirectory. For example, http://quarkus.io/benchmarks/spring-quarkus-perf-comparison/tuned/results-latest-tuned-throughput-for-main-comparison-light.svg.

## To generate your own charts 

Assuming you have a file called `myfile.json` in the expected data format, you can run:
```
git clone git@github.com:quarkusio/benchmarks.git # this repo!
mvn -f benchmarks/graphics-generator verify -DskipTests
mkdir -p images
java -jar benchmarks/graphics-generator/target/quarkus-app/quarkus-run.jar myfile.json images true
```

This will generate a set of plots in an `images` directory. 

## Architecture & Workflow

This diagram shows the architecture & workflow of how the benchmarking executes.

![Workflow](https://raw.githubusercontent.com/quarkusio/spring-quarkus-perf-comparison/refs/heads/main/docs/benchmark-workflow.png)

## Results 

### spring-quarkus-perf-comparison

Benchmark source code: https://github.com/quarkusio/spring-quarkus-perf-comparison

#### Tuned

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-throughput-for-main-comparison-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-throughput-for-main-comparison-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-boot-and-first-response-time-for-main-comparison-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-boot-and-first-response-time-for-main-comparison-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-memory-rss-for-main-comparison-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-memory-rss-for-main-comparison-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-build-duration-for-main-comparison-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-build-duration-for-main-comparison-dark.svg#gh-dark-mode-only)

### All variations

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-throughput-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-throughput-for-all-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-boot-and-first-response-time-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-boot-and-first-response-time-for-all-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-memory-rss-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-memory-rss-for-all-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-build-duration-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/tuned/results-latest-tuned-build-duration-for-all-dark.svg#gh-dark-mode-only)

#### Out of the box
![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-throughput-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-throughput-for-all-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-boot-and-first-response-time-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-boot-and-first-response-time-for-all-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-memory-rss-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-memory-rss-for-all-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-build-duration-for-all-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/ootb/results-latest-ootb-build-duration-for-all-dark.svg#gh-dark-mode-only)
