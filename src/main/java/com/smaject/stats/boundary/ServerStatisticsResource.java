package com.smaject.stats.boundary;

import com.smaject.stats.control.ServerWatch;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Resource that exposes runtime information provided by the server.
 *
 * @author smaject.com
 */
@Path("stats")
@Produces(MediaType.APPLICATION_JSON)
public class ServerStatisticsResource {
    @Inject
    ServerWatch serverWatch;

    @GET
    @Path("/start-time")
    @Produces(MediaType.TEXT_PLAIN)
    public String startTime() {
        return this.serverWatch.getServerStartTime().toString();
    }

    @GET
    @Path("/up-time")
    @Produces(MediaType.TEXT_PLAIN)
    public String upTime() {
        return this.serverWatch.getServerUpTimeInHms();
    }

    @GET
    @Path("/memory-usage")
    public JsonObject currentMemoryUsage() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Memory-Usage at start time in MB", this.serverWatch.getMemoryUsageAtStartTime())
                .add("Max. available memory in MB", this.serverWatch.getAvailableMemoryInMb())
                .add("Currently used memory in MB", this.serverWatch.getCurrentMemoryUsageInMb());
        return builder.build();
    }

    @GET
    @Path("/os-info")
    public JsonObject osInfo() {
        return this.serverWatch.getOsInfo();
    }

    @GET
    @Path("vm-info")
    public JsonObject vmInfo() {
        return this.serverWatch.getVmInfo();
    }

    @GET
    @Path("/host-info")
    public JsonObject hostIfo() {
        return this.serverWatch.getHostInfo();
    }
}
