package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.JSch;

import javax.enterprise.inject.Produces;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ScpClientProducer {


  @Produces
  public ScpClient getScpClient() throws IOException {
    BufferedReader reader = Files.newBufferedReader(Paths.get(System.getenv("ssh-params.properties")));

    Properties p = new Properties();
    p.load(reader);

    return new ScpClient(
        new JSch(),
        p.getProperty("username"),
        p.getProperty("host"),
        Integer.parseInt(p.getProperty("port")),
        p.getProperty("privateKeyPath"),
        p.getProperty("privateKeyPassphrase")
    );
  }
}
