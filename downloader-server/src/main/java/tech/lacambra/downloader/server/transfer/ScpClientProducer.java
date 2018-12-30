package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.JSch;

import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.util.Properties;

public class ScpClientProducer {


  @Produces
  public ScpClient getScpClient() throws IOException {
    Properties p = new Properties();
    p.load(ScpClient.class.getClassLoader().getResourceAsStream("ssh-params.properties"));

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
