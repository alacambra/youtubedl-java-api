package tech.lacambra.downloader.server.view;


import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

public class DownloadJobInfo {

  @FormParam("url")
  @NotNull
  private String url;

  @FormParam("extractAudio")
  @NotNull
  private boolean extractAudio;

  private boolean isExtractAudio() {
    return extractAudio;
  }

  public void setExtractAudio(boolean extractAudio) {
    this.extractAudio = extractAudio;
  }

  public boolean getExtractAudio() {
    return extractAudio;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "DownloadJobInfo{" +
        "url='" + url + '\'' +
        ", extractAudio=" + extractAudio +
        '}';
  }
}
