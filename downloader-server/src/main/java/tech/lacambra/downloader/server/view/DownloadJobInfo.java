package tech.lacambra.downloader.server.view;


import tech.lacambra.downloader.server.TransferProperties;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import java.util.Objects;
import java.util.Optional;

public class DownloadJobInfo {

  @Inject
  TransferProperties transferProperties;

  @FormParam("url")
  @NotNull
  private String url;

  @FormParam("extractAudio")
  @NotNull
  private String extractAudio;


  @FormParam("owner")
  @NotNull
  private String owner;

  public DownloadJobInfo(@NotNull String url, @NotNull String extractAudio, @NotNull String owner) {
    this.url = url;
    this.extractAudio = Objects.requireNonNull(extractAudio);
    this.owner = owner;
  }

  public DownloadJobInfo() {
    extractAudio = "false";
  }

  public String getExtractAudio() {
    return Optional.ofNullable(extractAudio).orElse("no");
  }

  public boolean isAudio() {
    return "on".equals(extractAudio);
  }

  public void setExtractAudio(String extractAudio) {
    this.extractAudio = Objects.requireNonNull(extractAudio);
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public TransferProperties getTransferProperties() {
    return transferProperties;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("url", url)
        .add("extractAudio", getExtractAudio())
        .add("owner", owner)
        .build();
  }

  @Override
  public String toString() {
    return "DownloadJobInfo{" +
        "transferProperties=" + transferProperties +
        ", url='" + url + '\'' +
        ", extractAudio='" + getExtractAudio() + '\'' +
        ", owner='" + owner + '\'' +
        '}';
  }
}
