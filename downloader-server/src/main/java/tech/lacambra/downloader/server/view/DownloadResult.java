package tech.lacambra.downloader.server.view;

import javax.json.Json;
import javax.json.JsonObject;

public class DownloadResult {

  private Integer exitCode;
  private boolean done;

  public DownloadResult() {
    this.done = false;
  }

  public DownloadResult(Integer exitCode) {
    this.exitCode = exitCode;
    done = true;
  }

  public Integer getExitCode() {
    return exitCode;
  }

  public JsonObject getJson() {

    if (exitCode == null) exitCode = -9999;

    return Json.createObjectBuilder()
        .add("exitCode", exitCode)
        .add("done", done)
        .build();
  }

  public boolean isDone() {
    return done;
  }

}
