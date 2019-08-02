package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.logging.Logger;

public class ScpClient {

  private final Session session;
  private static final Logger LOGGER = Logger.getLogger(ScpClient.class.getName());

  public static void main(String[] args) throws IOException {

    String lfile = "/Users/albertlacambra/dev/servers/wildfly-14.0.1.Final/standalone/configuration/standalone.xml";
    String rfile = "/var/services/homes/alacambra/test-folder/removeme.xml";

    SshToolsProducer sshToolsProducer = new SshToolsProducer();
    sshToolsProducer.sessionFactory = new JSchSessionFactory();
    Session session = sshToolsProducer.getSession();
    ScpClient scpClient = sshToolsProducer.getScpClient(session);
//    scpClient.copy(lfile, rfile);
//    scpClient.copy(lfile, rfile + 1);
//    scpClient.copy(lfile, rfile + 2);
//    scpClient.copy(lfile, rfile + 3);

    SftpClient sftpClient = new SftpClient(session);
    sftpClient.createDir("/var/services/homes/alacambra/test-folder/test1");

    scpClient.disconnect();
    session.disconnect();
  }

  public ScpClient(Session session) {
    this.session = session;
  }

  public void copy(String localSource, String remoteTarget) {
    ChannelExec channel = createChannel(session);
    String cmd = createScpCommand(prepareRemotePath(remoteTarget));
    Logger.getLogger("Setting command to channel: " + cmd);
    channel.setCommand(cmd);
    try {
      channel.connect();
    } catch (JSchException e) {
      throw new RuntimeException(e);
    }

    try (OutputStream out = channel.getOutputStream(); InputStream in = channel.getInputStream()) {
      checkAck(in);
      File f = new File(localSource);
      cmd = createSendFileCommand(f);
      out.write(cmd.getBytes());
      out.flush();
      sendFile(in, out, f);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      channel.disconnect();
    }

    System.out.println("Done");
  }

  public void disconnect() {
    session.disconnect();
  }

  private ChannelExec createChannel(Session session) {
    System.out.println("Creating SFTP Channel.");
    ChannelExec channel = null;

    try {
      channel = (ChannelExec) session.openChannel("exec");
    } catch (JSchException e) {
      throw new RuntimeException(e);
    }

    return channel;
  }

  private String prepareRemotePath(String rPath) {
    rPath = rPath.replace("'", "'\"'\"'");
    rPath = "'" + rPath + "'";
    return rPath;
  }

  private String createScpCommand(String rPath) {

    boolean ptimestamp = false;
    String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rPath;
    System.out.println("command: " + command);
    return command;

  }

  private String createSendFileCommand(File localFile) {
    String fileName = localFile.getName();
    long filesize = localFile.length();
    String fileCommingFlag = "C";
    String targetPermissions = "0644";
    String command = fileCommingFlag + targetPermissions + " " + filesize + " ";
    if (fileName.lastIndexOf('/') > 0) {
      command += fileName.substring(fileName.lastIndexOf('/') + 1);
    } else {
      command += fileName;
    }
    command += "\n";

    System.out.println("command: " + command);
    return command;
  }

  private void sendFile(InputStream in, OutputStream out, File localFile) throws IOException {
    FileInputStream fis = new FileInputStream(localFile);
    byte[] buf = new byte[1024];
    while (true) {
      int len = fis.read(buf, 0, buf.length);
      if (len <= 0) break;
      out.write(buf, 0, len); //out.flush();
    }
    fis.close();
    fis = null;
    // send '\0'
    buf[0] = 0;
    out.write(buf, 0, 1);
    out.flush();
    checkAck(in);
  }


  private void checkAck(InputStream in) throws IOException {
    int b = in.read();
    // b may be 0 for success,
    //          1 for error,
    //          2 for fatal error,
    //          -1
    if (b == 1 || b == 2) {
      StringBuffer sb = new StringBuffer();
      int c;
      do {
        c = in.read();
        sb.append((char) c);
      }
      while (c != '\n');
      if (b == 1) { // error
        System.out.print(sb.toString());
      }
      if (b == 2) { // fatal error
        System.out.print(sb.toString());
      }
    }
    if (b != 0) {
      throw new RuntimeException("invalid return code: " + b);
    }
  }
}
