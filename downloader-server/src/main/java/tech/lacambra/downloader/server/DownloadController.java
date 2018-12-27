package tech.lacambra.downloader.server;

import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("download")
public class DownloadController {

  @GET
  @Controller
  public String downloadVideo() {
    return "/hello.jsp";
  }
}