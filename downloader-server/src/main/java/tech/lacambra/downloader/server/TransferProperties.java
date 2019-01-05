package tech.lacambra.downloader.server;

import java.util.Properties;

public class TransferProperties {

  private Properties properties;

  public TransferProperties(Properties properties) {
    this.properties = properties;
  }

  public String getSource() {
    return properties.getProperty("source");
  }

  public String getTargetAudio(String owner) {
    return properties.getProperty("target-audio-" + owner);
  }

  public String getTargetVideo(String owner) {
    return properties.getProperty("target-video-" + owner);
  }

  @Override
  public String toString() {
    return "TransferProperties{" +
        "properties=" + properties +
        '}';
  }
}