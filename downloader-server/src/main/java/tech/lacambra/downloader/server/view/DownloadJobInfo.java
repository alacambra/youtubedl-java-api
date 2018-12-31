package tech.lacambra.downloader.server.view;


import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

public class DownloadJobInfo {

  @FormParam("url")
  @NotNull
  private String url;

  @FormParam("extractAudio")
  @NotNull
  private String extractAudio;


  public String getExtractAudio() {
    return extractAudio;
  }

  public boolean isAudio(){
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

  @Override
  public String toString() {
    return "DownloadJobInfo{" +
        "url='" + url + '\'' +
        ", extractAudio=" + extractAudio +
        '}';
  }
}
