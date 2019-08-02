package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class SshToolsProducer {

  private static final Logger LOGGER = Logger.getLogger(ScpClient.class.getName());

  @Inject
  JSchSessionFactory sessionFactory;


  @Produces
  public ScpClient getScpClient(Session session) throws IOException {
    return new ScpClient(session);
  }

  @Produces
  public Session getSession() throws IOException {

    LOGGER.info("[getSession] LOading session data from " + System.getenv("DOWNLOADER_SSH_PROPERTIES"));

    BufferedReader reader = Files.newBufferedReader(Paths.get(System.getenv("DOWNLOADER_SSH_PROPERTIES")));
    Properties p = new Properties();
    p.load(reader);

    JSch jSch = new JSch();
    Endpoint endpoint = new Endpoint(p.getProperty("host"), Integer.parseInt(p.getProperty("port")));
    CertCredentials certCredentials = new CertCredentials(p.getProperty("username"), p.getProperty("privateKeyPath"), p.getProperty("privateKeyPassphrase"));

    Session session = sessionFactory.createSession(jSch, endpoint, certCredentials);

    return session;
  }
}
