package tech.lacambra.downloader.server;

import downloader.tech.lacambra.downloader.client.YoutubeDLClient;
import io.reactivex.disposables.Disposable;
import tech.lacambra.downloader.server.transfer.ScpClient;
import tech.lacambra.downloader.server.view.DownloadJobInfo;
import tech.lacambra.downloader.server.view.DownloadResult;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DownloadService {

  private Map<String, DownloadJob> downloads;

  public DownloadService() {
    downloads = new ConcurrentHashMap<>();
  }

  @Inject
  YoutubeDLClient dlClient;

  @Inject
  ScpClient scpClient;

  public String beginDownloadJob(DownloadJobInfo jobInfo) {


    String id = UUID.randomUUID().toString();

    Disposable d = dlClient.options().extractAudio(jobInfo.getExtractAudio())
        .execute(jobInfo.getUrl())
        .subscribe(progressStep -> {

          DownloadJob job = downloads.get(id);
          Disposable disposable = job.getDisposable();
          progressStep.getExitCode().ifPresent(code -> downloads.put(id, new DownloadJob(id, new DownloadResult(code), disposable)));

        }, System.err::println, () -> {
          DownloadJob job = downloads.get(id);
          Disposable disposable = job.getDisposable();
          disposable.dispose();
        });

    DownloadJob job = new DownloadJob(id, null, d);

    downloads.put(id, job);
    return id;
  }

  public Optional<DownloadJob> getDownloadJob(String id) {
    return Optional.ofNullable(downloads.get(id));
  }
}
