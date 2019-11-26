package tech.lacambra.downloader.server.view;

import tech.lacambra.downloader.server.DownloadJob;
import tech.lacambra.downloader.server.DownloadService;
import tech.lacambra.downloader.server.TransferProperties;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("download")
public class DownloadResource {

  private static final Logger LOGGER = Logger.getLogger(DownloadResource.class.getName());

  @Inject
  DownloadService downloadService;

  @Inject
  TransferProperties transferProperties;

  @Inject
  JobId jobId;

  @Context
  UriInfo uriInfo;

  @POST
  @Path("job")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public JobId createJob(DownloadJobInfo jobInfo) {

    LOGGER.info("[createJob] Received jobInfo: " + jobInfo);

    jobInfo.setTransferProperties(transferProperties);
    downloadService.updateYoutubeDL();

    String id = downloadService.beginDownloadJob(jobInfo);

    jobId.setId(id);
    String l = uriInfo.getRequestUriBuilder().path("{id}").resolveTemplate("id", id).build().toString();

    if (l.contains("lacambra.tech") && !l.contains("https")) {
      l = l.replace("http", "https");
    }

    jobId.setLocation(l);
    LOGGER.info("[createJob] Job created=" + jobId);

    return jobId;
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