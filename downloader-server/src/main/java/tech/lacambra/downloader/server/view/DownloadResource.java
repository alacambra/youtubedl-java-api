package tech.lacambra.downloader.server.view;

import tech.lacambra.downloader.server.DownloadJob;
import tech.lacambra.downloader.server.DownloadService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("download")
public class DownloadResource {

  private static final Logger LOGGER = Logger.getLogger(DownloadResource.class.getName());

  @Inject
  DownloadService downloadService;

  @Inject
  JobId jobId;

  @Context
  UriInfo uriInfo;

  @GET
  @Controller
  public String downloadVideo() {
    LOGGER.info("[downloadVideo] dlClient=" + downloadService);

    return "/app/download.jsp";
  }

  @POST
  @Controller
  public String getVideo(@BeanParam DownloadJobInfo jobInfo) {
    LOGGER.info("[getVideo] received job info " + jobInfo);

    String id = downloadService.beginDownloadJob(jobInfo);

    jobId.setId(id);
    String l = uriInfo.getRequestUriBuilder().path("job/{id}").resolveTemplate("id", id).build().toString();
    jobId.setLocation(l);
    System.out.println(jobId);

    return "/app/download.jsp";
  }

  @GET
  @Path("job/{id}")
  public JsonObject getJob(@PathParam("id") String id) {
    DownloadResult result = downloadService.getDownloadJob(id).map(DownloadJob::getResult).orElse(new DownloadResult(-9999, false, 0f, "NOT_FOUND"));
    System.out.println("got job:" + result.getJson());
    return result.getJson();
  }
}