package tech.lacambra.downloader.server.transfer;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpClient {

  private Session session;

  public SftpClient(Session session) {
    this.session = session;
  }

  public void createDir(String path) {
    ChannelSftp channelSftp = createChannelSftp();

    try {
      channelSftp.mkdir(path);
    } catch (SftpException e) {
      throw new RuntimeException(e);
    }
  }

  private ChannelSftp createChannelSftp() {

    ChannelSftp channelSftp;

    try {
      channelSftp = (ChannelSftp) session.openChannel("sftp");
      channelSftp.connect();
    } catch (JSchException e) {
      throw new RuntimeException(e);
    }

    return channelSftp;
  }
}
