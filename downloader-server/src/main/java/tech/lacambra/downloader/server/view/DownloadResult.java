package tech.lacambra.downloader.server.view;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class DownloadResult {

  private String jobId;
  private Integer exitCode;
  private boolean done;
  private Float progress;
  private String status;
  private String shellNotification;
  private DownloadJobInfo downloadJobInfo;

  public DownloadResult(String jobId, Integer exitCode, boolean done, Float progress, String status, String shellNotification, DownloadJobInfo downloadJobInfo) {
    this.jobId = jobId;
    this.exitCode = exitCode;
    this.done = done;
    this.progress = progress;
    this.status = status;
    this.shellNotification = shellNotification;
    this.downloadJobInfo = Optional.ofNullable(downloadJobInfo).orElse(new DownloadJobInfo());
  }

  public Integer getExitCode() {
    return exitCode;
  }

  public JsonObject getJson() {

    if (exitCode == null) exitCode = -9999;

    return Json.createObjectBuilder()
        .add("jobId", jobId)
        .add("exitCode", exitCode)
        .add("line", Stream.of(shellNotification.split("\n")).collect(Collector.of(
            Json::createArrayBuilder,
            JsonArrayBuilder::add,
            JsonArrayBuilder::addAll,
            JsonArrayBuilder::build)))
        .add("done", done)
        .add("downloadProgress", progress)
        .add("status", status)
        .add("downloadJobInfo", downloadJobInfo.toJson())
        .build();
  }

  public String getShellNotification() {
    return shellNotification;
  }

  public boolean isDone() {
    return done;
  }

  @Override
  public String toString() {
    return "DownloadResult{" +
        "exitCode=" + exitCode +
        ", done=" + done +
        ", line=" + shellNotification +
        ", progress=" + progress +
        ", status='" + status + '\'' +
        '}';
  }
}
