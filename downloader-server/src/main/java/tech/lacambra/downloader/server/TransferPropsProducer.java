package tech.lacambra.downloader.server;

import javax.enterprise.inject.Produces;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class TransferPropsProducer {

  @Produces
  public TransferProperties getTransferProperties() throws IOException {

    BufferedReader reader = Files.newBufferedReader(Paths.get(System.getenv("DOWNLOADER_TRANSFER_PROPERTIES")));

    Properties properties = new Properties();
    properties.load(reader);

    return new TransferProperties(properties);

  }
}
