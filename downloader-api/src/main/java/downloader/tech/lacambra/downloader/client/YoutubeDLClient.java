package downloader.tech.lacambra.downloader.client;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDLClient {

  private String command;
  private Pattern p = Pattern.compile("\\[download\\]\\s+(?<percent>\\d+\\.\\d)% .* ETA (?<minutes>\\d+):(?<seconds>\\d+)");
  private static final String GROUP_PERCENT = "percent";
  private static final String GROUP_MINUTES = "minutes";
  private static final String GROUP_SECONDS = "seconds";


  public YoutubeDLClient() {
  }

  public OptionBuilder options() {
    return new OptionBuilder(this);
  }

  public Flowable<ProgressStep> execute(String opts, String target) {
    command = String.join("", "youtube-dl ", opts, target);
    return Flowable
        .create(this::emit, BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.io())
        .share()
        ;
  }

  private void emit(Emitter<ProgressStep> emitter) {
    try {
      System.out.println("Running command:" + command);
      Process p = Runtime.getRuntime().exec(command);

      InputStream is = p.getInputStream();
      run(is, (line, progress, etaInSeconds) -> emitter.onNext(new ProgressStep(line, progress, etaInSeconds, null)));
      run(p.getErrorStream(), (line, progress, etaInSeconds) -> emitter.onNext(new ProgressStep(line, progress, etaInSeconds, null)));
      try {
        p.waitFor();
      } catch (InterruptedException e) {
        emitter.onError(e);
        return;
      }
      emitter.onNext(new ProgressStep("done", 100, 0, p.exitValue()));
      emitter.onComplete();

    } catch (IOException e) {
      emitter.onError(e);
    }
  }

  private void run(InputStream stream, ProgressCallback callback) {


    try {
      StringBuilder currentLine = new StringBuilder();
      int nextChar;
      while ((nextChar = stream.read()) != -1) {
        if (nextChar == '\r' && callback != null) {
          processOutputLine(currentLine.toString(), callback);
          currentLine.setLength(0);
          continue;
        }
        currentLine.append((char) nextChar);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void processOutputLine(String line, ProgressCallback callback) {
    Matcher m = p.matcher(line);
    if (m.matches()) {
      float progress = Float.parseFloat(m.group(GROUP_PERCENT));
      long eta = convertToSeconds(m.group(GROUP_MINUTES), m.group(GROUP_SECONDS));
      callback.onProgressUpdate(line, progress, eta);
    }
  }

  private int convertToSeconds(String minutes, String seconds) {
    return Integer.parseInt(minutes) * 60 + Integer.parseInt(seconds);
  }
}
