package downloader.tech.lacambra.downloader.client;

import io.reactivex.Flowable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OptionBuilder {

  private List<CmdOption> options;
  private YoutubeDLClient client;

  public OptionBuilder(YoutubeDLClient client) {
    this.client = client;
    options = new ArrayList<>();
  }

  public OptionBuilder extractAudio(boolean extract) {

    if(extract){
      options.add(new CmdOption("--extract-audio", ""));
    }

    return this;
  }

  public OptionBuilder audioFormat(AudioOption.Format format) {
    options.add(new CmdOption("--audio-format", format.name()));
    return this;
  }

  public OptionBuilder audioQuality(AudioOption.Quality quality) {
    options.add(new CmdOption("--audio-quality", quality.getValue()));
    return this;
  }

  public OptionBuilder afterDate(LocalDate date) {
    options.add(new CmdOption("--dateafter", date.toString()));
    return this;
  }

  public String build() {
    return options.stream().map(CmdOption::toString).collect(Collectors.joining(" "));
  }

  public Flowable<ProgressStep> execute(String target) {
    String opts = options.stream().map(CmdOption::toString).collect(Collectors.joining(" "));
    return client.execute(opts, target);
  }
}