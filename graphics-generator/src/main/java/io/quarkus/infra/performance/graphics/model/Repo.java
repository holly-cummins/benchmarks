package io.quarkus.infra.performance.graphics.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Repo(
        String branch,
        String url,
        String scenario,
        String commit,
        @JsonProperty("short_commit") String shortCommit) {

  public Repo(String branch, String url, String scenario, String commit) {
    this(
      branch,
      url,
      scenario,
      commit,
      deriveShortCommit(commit));
  }

  public Repo {
    if (shortCommit == null) {
      shortCommit = deriveShortCommit(commit);
    }
  }

  private static String deriveShortCommit(String commit) {
    return Optional.ofNullable(commit)
        .filter(c -> c.length() >= 10)
        .map(c -> c.substring(0, 10))
        .orElse(commit);
  }
}
