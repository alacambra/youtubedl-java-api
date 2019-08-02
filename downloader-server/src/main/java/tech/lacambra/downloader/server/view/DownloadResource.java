package tech.lacambra.downloader.server.view;

import tech.lacambra.downloader.server.DownloadJob;
import tech.lacambra.downloader.server.DownloadService;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

  @Inject
  Instance<DownloadJobInfo> infoInstances;

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

    downloadService.updateYoutubeDL();
    String id = downloadService.beginDownloadJob(jobInfo);

    jobId.setId(id);
    String l = uriInfo.getRequestUriBuilder().path("job/{id}").resolveTemplate("id", id).build().toString();

    if (l.contains("lacambra.tech") && !l.contains("https")) {
      l = l.replace("http", "https");
    }

    jobId.setLocation(l);
    LOGGER.info("[getVideo] jobId");

    return "/app/download.jsp";
  }

  @POST
  @Path("job")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createJob(JsonObject jobInfo) {
    DownloadJobInfo ji = infoInstances.get();
    ji.setUrl(jobInfo.getString("url"));
    ji.setExtractAudio(jobInfo.getBoolean("extractAudio") ? "on" : "");
    ji.setOwner(jobInfo.getString("owner"));

    downloadService.updateYoutubeDL();
    String id = downloadService.beginDownloadJob(ji);

    jobId.setId(id);
    String l = uriInfo.getRequestUriBuilder().path("{id}").resolveTemplate("id", id).build().toString();

    if (l.contains("lacambra.tech") && !l.contains("https")) {
      l = l.replace("http", "https");
    }

    jobId.setLocation(l);
    LOGGER.info("[createJob] jobId");

    return Response.status(201).entity(jobId.getLocation()).build();
  }

  @GET
  @Path("job/{id}")
  public JsonObject getJob(@PathParam("id") String id) {
    DownloadResult result = downloadService.getDownloadJob(id)
        .map(DownloadJob::getResult)
        .orElse(new DownloadResult(-9999, false, 0f, "NOT_FOUND", "", null));
    LOGGER.info("[getJob] got job:" + result.getJson());
    return result.getJson();
  }

  @GET
  @Path("job")
  public JsonArray getJobs() {
    return downloadService
        .getDownloadJobs()
        .stream()
        .map(DownloadJob::getResult)
        .map(DownloadResult::getJson)
        .collect(JsonCollectors.toJsonArray());
  }

  @GET
  @Path("update")
  public JsonArray update() {
    return
        downloadService.updateYoutubeDL()
            .stream()
            .map(Json::createValue)
            .collect(JsonCollectors.toJsonArray());
  }
}