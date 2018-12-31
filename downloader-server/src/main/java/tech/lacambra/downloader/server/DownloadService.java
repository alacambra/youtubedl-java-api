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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

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
    Path path = Paths.get(YoutubeDLClient.TARGET_FOLDER).resolve(id);
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
          Disposable disposable = job.getDisposable();
          progressStep.getExitCode().ifPresent(code -> downloads.put(id, new DownloadJob(id, new DownloadResult(code), disposable, job.getTargetFolder())));

        }, System.err::println, () -> {

          DownloadJob job = downloads.get(id);

          if (job.getResult().getExitCode() != 0) {
            System.out.println("Error on download - " + job.getResult());
            return;
          }

          File f = new File(job.getTargetFolder());
          Stream.of(Objects.requireNonNull(f.listFiles())).forEach(file -> {
            scpClient.copy(file.getAbsolutePath(), "/var/services/homes/alacambra/test-folder/" + file.getName());
          });

          Disposable disposable = job.getDisposable();
          disposable.dispose();
        });

    DownloadJob job = new DownloadJob(id, null, d, path.toString());
    downloads.put(id, job);
    return id;
  }

  public Optional<DownloadJob> getDownloadJob(String id) {
    return Optional.ofNullable(downloads.get(id));
  }
}
