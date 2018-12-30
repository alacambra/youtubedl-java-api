package tech.lacambra.downloader.server;

import io.reactivex.disposables.Disposable;
import tech.lacambra.downloader.server.view.DownloadResult;

public class DownloadJob {

  private String id;
  private DownloadResult result;
  private String target;

  private Disposable disposable;

  public DownloadJob(String id, DownloadResult result, Disposable disposable) {
    this.id = id;
    this.result = result;
    this.disposable = disposable;
  }

  public String getId() {
    return id;
  }

  public DownloadResult getResult() {
    return result;
  }


  public Disposable getDisposable() {
    return disposable;
  }
}
