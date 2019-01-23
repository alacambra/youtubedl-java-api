package tech.lacambra.downloader.server.view;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Optional;

public class DownloadResult {

  private Integer exitCode;
  private boolean done;
  private Float progress;
  private String status;
  private String line;
  private DownloadJobInfo downloadJobInfo;

  public DownloadResult(Integer exitCode, boolean done, Float progress, String status, String line, DownloadJobInfo downloadJobInfo) {
    this.exitCode = exitCode;
    this.done = done;
    this.progress = progress;
    this.status = status;
    this.line = line;
    this.downloadJobInfo = Optional.ofNullable(downloadJobInfo).orElse(new DownloadJobInfo("", "", ""));
  }

  public Integer getExitCode() {
    return exitCode;
  }

  public JsonObject getJson() {

    if (exitCode == null) exitCode = -9999;

    return Json.createObjectBuilder()
        .add("exitCode", exitCode)
        .add("line", line)
        .add("done", done)
        .add("downloadProgress", progress)
        .add("status", status)
        .add("downloadJobInfo", downloadJobInfo.toJson())
        .build();
  }

  public boolean isDone() {
    return done;
  }

  @Override
  public String toString() {
    return "DownloadResult{" +
        "exitCode=" + exitCode +
        ", done=" + done +
        ", line=" + line +
        ", progress=" + progress +
        ", status='" + status + '\'' +
        '}';
  }
}
