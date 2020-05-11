package tech.lacambra.downloader.server;

import downloader.tech.lacambra.downloader.client.ProgressStep;
import downloader.tech.lacambra.downloader.client.YoutubeDLClient;
import io.reactivex.disposables.Disposable;
import tech.lacambra.downloader.server.transfer.SftpClient;
import tech.lacambra.downloader.server.view.DownloadJobInfo;
import tech.lacambra.downloader.server.view.DownloadResult;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DownloadService {

  private static final Logger LOGGER = Logger.getLogger(DownloadService.class.getName());

  private Map<String, DownloadJob> downloads;

  public DownloadService() {
    downloads = new ConcurrentHashMap<>();
  }

  @Inject
  YoutubeDLClient dlClient;

  @Inject
  SftpClient sftpClient;

  public String beginDownloadJob(DownloadJobInfo downloadJobInfo) {


    String id = UUID.randomUUID().toString();
    Path path = Paths.get(downloadJobInfo.getTransferProperties().getSource()).resolve(id);
    try {
      path = Files.createDirectory(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Disposable d = dlClient
        .options()
        .extractAudio(downloadJobInfo.isAudio())
        .execute(downloadJobInfo.getUrl(), path.toString())
        .subscribe(
            progressStep -> processNotification(progressStep, id),
            e -> LOGGER.log(Level.WARNING, e.getMessage(), e),
            () -> copyDownloadedFile(downloadJobInfo, id)
        );

    DownloadJob job = new DownloadJob(id, null, d, path.toString(), downloadJobInfo);
    downloads.put(id, job);
    return id;
  }

  private void processNotification(ProgressStep progressStep, String id) {
    DownloadJob job = downloads.get(id);
    String message = Optional.ofNullable(job).map(DownloadJob::getResult).map(DownloadResult::getShellNotification).orElse("") + "\n" + progressStep.getShellNotification();
    progressStep
        .getExitCode()
        .map(code -> downloads.put(id, cloneForDone(code, job)))
        .orElseGet(() -> downloads.put(id, cloneForDLInProgress(-9999, progressStep.getProgress(), message, job)));

  }

  private void copyDownloadedFile(DownloadJobInfo downloadJobInfo, String id) {
    DownloadJob job = downloads.get(id);

    if (job.getResult().getExitCode() != 0) {
      downloads.put(id, cloneForError(job.getResult().getExitCode(), job));
      return;
    }

    LOGGER.info("[copyDownloadedFile] Target folder is " + job.getTargetFolder());

    File f = new File(job.getTargetFolder());
    File[] files = f.listFiles();

    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        File file = files[i];
        downloads.put(id, cloneForTXInProgress(-9999, (float) i / files.length, job));
        String target = downloadJobInfo.isAudio() ? downloadJobInfo.getTransferProperties().getTargetAudio(downloadJobInfo.getOwner()) : downloadJobInfo.getTransferProperties().getTargetVideo(downloadJobInfo.getOwner());
        target = target + file.getName();

        LOGGER.info("[copyDownloadedFile] copy. " + file.getAbsolutePath() + " -> " + target);

        sftpClient.uploadFile(file.getAbsolutePath(), target + file.getName());
      }
    }

    downloads.put(id, cloneForDone(0, job));
    Disposable disposable = job.getDisposable();
    disposable.dispose();
  }

  private DownloadJob cloneForDone(int code, DownloadJob job) {
    return new DownloadJob(job.getId(),
        new DownloadResult(job.getId(), code, true, 100.0f, "DONE", job.getResult().getShellNotification(), job.getDownloadJobInfo()),
        job.getDisposable(),
        job.getTargetFolder(),
        job.getDownloadJobInfo()
    );
  }

  private DownloadJob cloneForError(int code, DownloadJob job) {
    return new DownloadJob(job.getId(),
        new DownloadResult(job.getId(), code, true, -1f, "ERROR", "", job.getDownloadJobInfo()),
        job.getDisposable(),
        job.getTargetFolder(),
        job.getDownloadJobInfo()
    );
  }

  private DownloadJob cloneForDLInProgress(int code, float progress, String line, DownloadJob job) {
    return new DownloadJob(job.getId(),
        new DownloadResult(job.getId(), code, false, progress, "DL_IN_PROGRESS", line, job.getDownloadJobInfo()),
        job.getDisposable(),
        job.getTargetFolder(),
        job.getDownloadJobInfo()
    );
  }

  private DownloadJob cloneForTXInProgress(int code, float progress, DownloadJob job) {
    return new DownloadJob(job.getId(),
        new DownloadResult(job.getId(), code, false, progress, "TR_IN_PROGRESS", job.getResult().getShellNotification() + "\nCopying....", job.getDownloadJobInfo()),
        job.getDisposable(),
        job.getTargetFolder(),
        job.getDownloadJobInfo()
    );
  }

  public Optional<DownloadJob> getDownloadJob(String id) {
    return Optional.ofNullable(downloads.get(id));
  }

  public List<DownloadJob> getDownloadJobs() {
    return new ArrayList<>(downloads.values());
  }

  public void clearJobs() {
    for (Map.Entry<String, DownloadJob> entry : downloads.entrySet()) {
      if (entry.getValue().getResult().isDone()) {
        downloads.remove(entry.getKey());
      }
    }
  }

  public List<String> updateYoutubeDL() {
    LOGGER.info("[updateYoutubeDL] updating....");
    return dlClient.updateYoutubeDL().map(ProgressStep::getShellNotification).toList().blockingGet();
  }
}
