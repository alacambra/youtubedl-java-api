package tech.lacambra.downloader;


public interface ProgressCallback {
  void onProgressUpdate(String line, float progress, long etaInSeconds);
}
