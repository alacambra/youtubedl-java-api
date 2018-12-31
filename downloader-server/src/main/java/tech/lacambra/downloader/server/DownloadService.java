package tech.lacambra.downloader.server;

import downloader.tech.lacambra.downloader.client.YoutubeDLClient;
import io.reactivex.disposables.Disposable;
import tech.lacambra.downloader.server.transfer.ScpClient;
import tech.lacambra.downloader.server.view.DownloadJobInfo;
import tech.lacambra.downloader.server.view.DownloadResult;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    Path path = Paths.get(jobInfo.getTransferProperties().getSource()).resolve(id);
    try {
      path = Files.createDirectory(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Disposable d = dlClient
        .options()
        .extractAudio(jobInfo.isAudio())
        .execute(jobInfo.getUrl(), path.toString())
        .subscribe(progressStep -> {

          DownloadJob job = downloads.get(id);
          progressStep
              .getExitCode()
              .map(code -> downloads.put(id, cloneForDone(code, job)))
              .orElseGet(() -> downloads.put(id, cloneForDLInProgress(-9999, progressStep.getProgress(), job)));

        }, System.err::println, () -> {

          DownloadJob job = downloads.get(id);

          if (job.getResult().getExitCode() != 0) {
            System.out.println("Error on download - " + job.getResult());
            return;
          }

          File f = new File(job.getTargetFolder());
          File[] files = f.listFiles();

          if (files != null) {
            for (int i = 0; i < files.length; i++) {
              File file = files[i];
              downloads.put(id, cloneForTXInProgress(-9999, (float) i / files.length, job));
              String t = jobInfo.isAudio() ? jobInfo.getTransferProperties().getTargetAudio() : jobInfo.getTransferProperties().getTargetVideo();
              scpClient.copy(file.getAbsolutePath(), t + file.getName());
            }
          }

          downloads.put(id, cloneForDone(0, job));
          Disposable disposable = job.getDisposable();
          disposable.dispose();
        });

    DownloadJob job = new DownloadJob(id, null, d, path.toString());
    downloads.put(id, job);
    return id;
  }

  private DownloadJob cloneForDone(int code, DownloadJob job) {
    return new DownloadJob(job.getId(),
        new DownloadResult(code, true, 100.0f, "DONE"),
        job.getDisposable(),
        job.getTargetFolder()
    );
  }

  private DownloadJob cloneForDLInProgress(int code, float progress, DownloadJob job) {
    return new DownloadJob(job.getId(),
        new DownloadResult(code, false, progress, "DL_IN_PROGRESS"),
        job.getDisposable(),
        job.getTargetFolder()
    );
  }

  private DownloadJob cloneForTXInProgress(int code, float progress, DownloadJob job) {
    return new DownloadJob(job.getId(),
        new DownloadResult(code, false, progress, "TX_IN_PROGRESS"),
        job.getDisposable(),
        job.getTargetFolder()
    );
  }

  public Optional<DownloadJob> getDownloadJob(String id) {
    return Optional.ofNullable(downloads.get(id));
  }
}
