package tech.lacambra.downloader.server.view;


import tech.lacambra.downloader.server.TransferProperties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

public class DownloadJobInfo {

  TransferProperties transferProperties;

  @NotNull
  private String url;

  @NotNull
  private Boolean extractAudio;


  @NotNull
  private String owner;

  public DownloadJobInfo() {
    extractAudio = false;
  }

  public Boolean getExtractAudio() {
    return Optional.ofNullable(extractAudio).orElse(false);
  }

  public boolean isAudio() {
    return extractAudio;
  }

  public void setExtractAudio(Boolean extractAudio) {
    this.extractAudio = Objects.requireNonNull(extractAudio);
  }

  public void setTransferProperties(TransferProperties transferProperties) {
    this.transferProperties = transferProperties;
  }

  public String getUrl() {

    if (url == null) {
      return "";
    }

    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getOwner() {

    if (owner == null) {
      return "";
    }

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
        .add("url", getUrl())
        .add("extractAudio", getExtractAudio())
        .add("owner", getOwner())
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
