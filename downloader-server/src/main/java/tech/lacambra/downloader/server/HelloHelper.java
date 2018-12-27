package tech.lacambra.downloader.server;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class HelloHelper {

  @Inject
  GreetingService service;


  public String getMessage() {
    return service.message();
  }
}
