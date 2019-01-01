package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSchSessionFactory {


  public Session createSession(JSch jSch, Endpoint endpoint, CertCredentials certCredentials) {
    try {
      jSch.addIdentity(certCredentials.getPrivateKeyPath(), certCredentials.getPrivateKeyPassphrase());
      Session session = jSch.getSession(certCredentials.getUserName(), endpoint.getHost(), endpoint.getPort());

      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();
      System.out.println("Connection established.");

      return session;

    } catch (JSchException e) {
      throw new RuntimeException(e);
    }
  }
}
