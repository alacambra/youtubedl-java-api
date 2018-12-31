package tech.lacambra.downloader.server.view;

import javax.json.Json;
import javax.json.JsonObject;

public class DownloadResult {

  private Integer exitCode;
  private boolean done;
  private Float progress;
  private String status;

  public DownloadResult(Integer exitCode, boolean done, Float progress, String status) {
    this.exitCode = exitCode;
    this.done = done;
    this.progress = progress;
    this.status = status;
  }

  public Integer getExitCode() {
    return exitCode;
  }

  public JsonObject getJson() {

    if (exitCode == null) exitCode = -9999;

    return Json.createObjectBuilder()
        .add("exitCode", exitCode)
        .add("done", done)
        .add("downloadProgress", progress)
        .add("status", status)
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
        ", progress=" + progress +
        ", status='" + status + '\'' +
        '}';
  }
}
