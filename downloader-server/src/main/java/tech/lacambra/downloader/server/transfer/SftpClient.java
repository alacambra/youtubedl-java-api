package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class SftpClient {

  private Session session;

  public SftpClient(Session session) {
    this.session = session;
  }

  public void createDir(String path) {
    ChannelSftp channelSftp = createChannelSftp();

    try {
//      channelSftp.mkdir("/music/downloader/removeme");
      Vector v = channelSftp.ls("/downloader-albert");
      for (Object o : v) {
        System.out.println(o);
      }

//      channelSftp.put(
//          Files.newInputStream(Paths.get("/Users/albertlacambra/git/lacambra.tech/downloader/Hora 25 (20_12_2019 - Tramo de 21_00 a 22_00).mp3")),
//          "/music/downloader/hannah/test.mp3");
    } catch (SftpException
//        |
//        IOException
        e) {
      throw new RuntimeException(e);
    } finally {
      if (channelSftp != null && !channelSftp.isClosed()) {
        channelSftp.disconnect();
      }
    }
  }

  public void uploadFile(String localSource, String remoteTarget) {
    ChannelSftp channelSftp = createChannelSftp();

    try {
      channelSftp.put(
          Files.newInputStream(Paths.get(localSource)),
          remoteTarget);
    } catch (SftpException
        |
        IOException
        e) {
      throw new RuntimeException(e);
    } finally {
      if (channelSftp != null && channelSftp.isConnected()) {
        channelSftp.disconnect();
      }
    }
  }

  private ChannelSftp createChannelSftp() {

    ChannelSftp channelSftp = null;

    try {
      channelSftp = (ChannelSftp) session.openChannel("sftp");
      System.out.println(session.isConnected());
      channelSftp.connect();
    } catch (JSchException e) {
      if (channelSftp != null && !channelSftp.isClosed()) {
        channelSftp.disconnect();
      }

      session.disconnect();
      throw new RuntimeException(e);
    }
    return channelSftp;
  }
}
