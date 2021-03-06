package downloader.tech.lacambra.downloader.client;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDLClient {

  private static final Logger LOGGER = Logger.getLogger(YoutubeDLClient.class.getName());

  public static final String TARGET_FOLDER = "/Users/albertlacambra/Downloads/esther/";
  private Pattern p = Pattern.compile("\\[download\\]\\s+(?<percent>\\d+\\.\\d)% .* ETA (?<minutes>\\d+):(?<seconds>\\d+)");
  private static final String GROUP_PERCENT = "percent";
  private static final String GROUP_MINUTES = "minutes";
  private static final String GROUP_SECONDS = "seconds";


  public YoutubeDLClient() {
  }

  public OptionBuilder options() {
    return new OptionBuilder(this);
  }

//  String o = " -o " + TARGET_FOLDER + "%(title)s.%(ext)s ";

  public Flowable<ProgressStep> execute(String opts, String targetUrl, String localDest) {
    String command = String.join("", "youtube-dl ", " -o " + localDest + "/" + "%(title)s.%(ext)s ", opts, targetUrl);
    return Flowable
        .create((FlowableEmitter<ProgressStep> e) -> emit(e, command), BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.io())
        .share()
        ;
  }

  public Flowable<ProgressStep> updateYoutubeDL() {
    String command = "youtube-dl -U";
    return Flowable
        .create((FlowableEmitter<ProgressStep> e) -> emit(e, command), BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.io())
        .share()
        ;
  }

  private void emit(FlowableEmitter<ProgressStep> emitter, String command) {
    try {
      LOGGER.info("[emit] Running command:" + command);
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
        if (nextChar == '\n' && callback != null) {
          callback.onProgressUpdate(currentLine.toString(), 0, 0);
          LOGGER.info("[run] " + currentLine);
          currentLine.setLength(0);
          continue;
        }

        currentLine.append((char) nextChar);
      }
      callback.onProgressUpdate(currentLine.toString(), -1, 0);
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
