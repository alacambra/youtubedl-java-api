package downloader.tech.lacambra.downloader.client;


public interface ProgressCallback {
  void onProgressUpdate(String line, float progress, long etaInSeconds);
}
