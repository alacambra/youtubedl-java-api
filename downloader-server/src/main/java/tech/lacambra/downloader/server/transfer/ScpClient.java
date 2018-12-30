package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Properties;

public class ScpClient {

  private JSch jsch;
  private String user;
  private String host;
  private String privateKeyPath;
  private String privateKeyPassphrase;

  private int port;
  private static String lfile = "/Users/albertlacambra/dev/servers/wildfly-14.0.1.Final/standalone/configuration/standalone.xml";
  private static String rfile = "/var/services/homes/alacambra/removeme.xml";

  public static void main(String[] args) throws IOException {
    Properties p = new Properties();
    p.load(ScpClient.class.getClassLoader().getResourceAsStream("ssh-params.properties"));

    ScpClient scpClient = new ScpClient(
        new JSch(),
        p.getProperty("username"),
        p.getProperty("host"),
        Integer.parseInt(p.getProperty("port")),
        p.getProperty("privateKeyPath"),
        p.getProperty("privateKeyPassphrase")
    );

    scpClient.copy(lfile, rfile);
  }

  public ScpClient(JSch jsch, String user, String host, int port, String privateKeyPath, String privateKeyPassphrase) {
    this.jsch = jsch;
    this.user = user;
    this.host = host;
    this.privateKeyPath = privateKeyPath;
    this.privateKeyPassphrase = privateKeyPassphrase;
    this.port = port;
  }

  public void copy(String localSource, String remoteTarget) {
    Session session = createSession(jsch);
    ChannelExec channel = createChannel(session);
    String cmd = createScpCommand(prepareRemotePath(remoteTarget));
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
      session.disconnect();
    }

    System.out.println("Done");
  }

  private Session createSession(JSch jsch) {
    Session session = null;
    try {
      jsch.addIdentity(privateKeyPath, privateKeyPassphrase);
      session = jsch.getSession(user, host, port);

//      session.setPassword(password);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();
      System.out.println("Connection established.");

    } catch (JSchException e) {
      throw new RuntimeException(e);
    }


    return session;
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
    long filesize = localFile.length();
    String fileCommingFlag = "C";
    String targetPermissions = "0644";
    String command = fileCommingFlag + targetPermissions + " " + filesize + " ";
    if (this.lfile.lastIndexOf('/') > 0) {
      command += this.lfile.substring(this.lfile.lastIndexOf('/') + 1);
    } else {
      command += this.lfile;
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
