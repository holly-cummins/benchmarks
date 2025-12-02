# Benchmarks

## To generate your own charts 

Assuming you have a file called `myfile.json` in the expected data format, you can run:
```
git clone git@github.com:quarkusio/benchmarks.git # this repo!
mvn -f benchmarks/graphics-generator verify -DskipTests
mkdir -p images
java -jar benchmarks/graphics-generator/target/quarkus-app/quarkus-run.jar myfile.json images true
```

This will generate a set of plots in an `images` directory. 

## spring-quarkus-perf-comparison

Benchmark source code: https://github.com/quarkusio/spring-quarkus-perf-comparison

![](images/spring-quarkus-perf-comparison/results-latest-throughput-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/results-latest-throughput-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/results-latest-boot-and-first-response-time-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/results-latest-boot-and-first-response-time-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/results-latest-memory-(rss)-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/results-latest-memory-(rss)-dark.svg#gh-dark-mode-only)

![](images/spring-quarkus-perf-comparison/results-latest-build-duration-light.svg#gh-light-mode-only)
![](images/spring-quarkus-perf-comparison/results-latest-build-duration-dark.svg#gh-dark-mode-only)
