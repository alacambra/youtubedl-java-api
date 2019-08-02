package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.logging.Logger;

public class JSchSessionFactory {

  private static final Logger LOGGER = Logger.getLogger(JSchSessionFactory.class.getName());

  public Session createSession(JSch jSch, Endpoint endpoint, CertCredentials certCredentials) {
    try {
      jSch.addIdentity(certCredentials.getPrivateKeyPath(), certCredentials.getPrivateKeyPassphrase());
      Session session = jSch.getSession(certCredentials.getUserName(), endpoint.getHost(), endpoint.getPort());

      session.setConfig("StrictHostKeyChecking", "no");

      LOGGER.info("[createSession] Trying to connect. Endpoint=" + endpoint + ", username=" + certCredentials.getUserName());

      session.connect();
      LOGGER.info("[createSession] Connection established. SessionConnected=" + session.isConnected());

      return session;

    } catch (JSchException e) {
      throw new RuntimeException(e);
    }
  }
}
