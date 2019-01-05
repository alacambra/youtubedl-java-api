package tech.lacambra.downloader.server;

import io.reactivex.disposables.Disposable;
import tech.lacambra.downloader.server.view.DownloadJobInfo;
import tech.lacambra.downloader.server.view.DownloadResult;

public class DownloadJob {

  private String id;
  private DownloadResult result;
  private String targetFolder;
  private DownloadJobInfo downloadJobInfo;

  private Disposable disposable;

  public DownloadJob(String id, DownloadResult result, Disposable disposable, String targetFolder, DownloadJobInfo downloadJobInfo) {
    this.id = id;
    this.result = result;
    this.disposable = disposable;
    this.targetFolder = targetFolder;
    this.downloadJobInfo = downloadJobInfo;
  }

  public String getId() {
    return id;
  }

  public DownloadResult getResult() {
    return result;
  }

  public String getTargetFolder() {
    return targetFolder;
  }

  public Disposable getDisposable() {
    return disposable;
  }

  public DownloadJobInfo getDownloadJobInfo() {
    return downloadJobInfo;
  }

  @Override
  public String toString() {
    return "DownloadJob{" +
        "id='" + id + '\'' +
        ", result=" + result +
        ", targetFolder='" + targetFolder + '\'' +
        ", downloadJobInfo=" + downloadJobInfo +
        ", disposable=" + disposable +
        '}';
  }
}
