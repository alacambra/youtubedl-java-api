package tech.lacambra.downloader.server;

import javax.ejb.Stateless;

@Stateless
public class GreetingService {

  public String message() {
    return "Hello " + System.currentTimeMillis();
  }

}
