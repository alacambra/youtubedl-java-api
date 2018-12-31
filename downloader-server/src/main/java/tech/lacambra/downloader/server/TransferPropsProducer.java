package tech.lacambra.downloader.server;

import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.util.Properties;

public class TransferPropsProducer {

  @Produces
  public TransferProperties getTransferProperties() throws IOException {

    Properties properties = new Properties();
    properties.load(getClass().getClassLoader().getResourceAsStream("transfer.properties"));

    return new TransferProperties(
        properties.getProperty("source"),
        properties.getProperty("target-audio"),
        properties.getProperty("target-video")
    );

  }
}
