package tech.lacambra.downloader.server.view;


import tech.lacambra.downloader.server.TransferProperties;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

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


  public String getExtractAudio() {
    return extractAudio;
  }

  public boolean isAudio() {
    return "on".equals(extractAudio);
  }

  public void setExtractAudio(String extractAudio) {
    this.extractAudio = extractAudio;
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

  @Override
  public String toString() {
    return "DownloadJobInfo{" +
        "transferProperties=" + transferProperties +
        ", url='" + url + '\'' +
        ", extractAudio='" + extractAudio + '\'' +
        ", owner='" + owner + '\'' +
        '}';
  }
}
