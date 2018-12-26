package tech.lacambra.downloader;

import java.util.Optional;

public class ProgressStep {

  private String line;
  private float progress;
  private long etaInSeconds;
  private Integer exitCode;

  public ProgressStep(String line, float progress, long etaInSeconds, Integer exitCode) {
    this.line = line;
    this.progress = progress;
    this.etaInSeconds = etaInSeconds;
    this.exitCode = exitCode;
  }

  public String getLine() {
    return line;
  }

  public float getProgress() {
    return progress;
  }

  public long getEtaInSeconds() {
    return etaInSeconds;
  }

  public Optional<Integer> getExitCode() {
    return Optional.ofNullable(exitCode);
  }

  @Override
  public String toString() {
    return "ProgressStep{" +
        "line='" + line + '\'' +
        ", progress=" + progress +
        ", etaInSeconds=" + etaInSeconds +
        ", exitCode=" + exitCode +
        '}';
  }
}
