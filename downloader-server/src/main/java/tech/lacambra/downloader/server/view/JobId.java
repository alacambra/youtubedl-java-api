package tech.lacambra.downloader.server.view;

import javax.enterprise.inject.Model;

@Model
public class JobId {

  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
