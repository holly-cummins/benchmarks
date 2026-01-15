package io.quarkus.infra.performance.graphics.model;

public record Repo(
        String branch,
        String url,
        String scenario) {

  public Repo(String branch, String url) {
    this(branch, url, "tuned");
  }
}
