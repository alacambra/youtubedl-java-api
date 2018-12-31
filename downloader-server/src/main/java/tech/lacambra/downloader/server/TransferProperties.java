package tech.lacambra.downloader.server;

public class TransferProperties {

  private String source;
  private String targetAudio;
  private String targetVideo;

  public TransferProperties(String source, String targetAudio, String targetVideo) {
    this.source = source;
    this.targetAudio = targetAudio;
    this.targetVideo = targetVideo;
  }

  public String getSource() {
    return source;
  }

  public String getTargetAudio() {
    return targetAudio;
  }

  public String getTargetVideo() {
    return targetVideo;
  }

  @Override
  public String toString() {
    return "TransferProperties{" +
        "source='" + source + '\'' +
        ", targetAudio='" + targetAudio + '\'' +
        ", targetVideo='" + targetVideo + '\'' +
        '}';
  }
}