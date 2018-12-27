package downloader.tech.lacambra.downloader.client;

public class CmdOption {
  private String option;
  private String value;

  public CmdOption(String option, String value) {
    this.option = option;
    this.value = value;
  }

  @Override
  public String toString() {
    return option + " " + value;
  }
}
