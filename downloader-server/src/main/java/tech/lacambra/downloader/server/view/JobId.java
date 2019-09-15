package tech.lacambra.downloader.server.view;

public class JobId {

  private String id;
  private String location;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "JobId{" +
        "id='" + id + '\'' +
        ", location='" + location + '\'' +
        '}';
  }
}
