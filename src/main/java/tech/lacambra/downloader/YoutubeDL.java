package tech.lacambra.downloader;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YoutubeDL {
  private static final Logger logger = Logger.getLogger(YoutubeDL.class.getName());
  private static final String YOUTUBE_DL = "youtube-dl";
  private ExecuteShellCommand cmd;
  private String options;


  public YoutubeDL() {
    cmd = new ExecuteShellCommand();
  }

  public YoutubeDL setOptions(String options) {
    this.options = options;
    return this;
  }

  public String execute(String url) {
    String c = Stream.of(YOUTUBE_DL, options, url).collect(Collectors.joining(" "));
    logger.info("[execute] executing cmd:" + c);
    Consumer<InputStream> c1 = is -> {
      new StreamProcessExtractor(new StringBuffer(), is, (line, progress, etaInSeconds) -> {
        System.out.println(progress);
      }).start();
    };

    return cmd.executeCommand(c, c1);
  }
}
