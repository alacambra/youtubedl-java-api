package downloader.tech.lacambra.downloader.client;

import java.time.format.DateTimeFormatter;

public class AudioOption {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  public enum Format {
    best,
    aac,
    flac,
    mp3,
    m4a,
    opus,
    vorbis,
    wav;
  }

  public enum Quality {
    q0("0"),
    q1("1"),
    q2("2"),
    q3("3"),
    q4("4"),
    q5("5"),
    q6("6"),
    q7("7"),
    q8("8"),
    q9("9"),
    q32K("32K"),
    q64K("64K"),
    q128K("128K"),
    q192K("192K"),
    q256K("256K"),
    q320K("320K");

    private String value;

    Quality(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
