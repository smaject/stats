package com.smaject.stats.control;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.lang.management.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of simple server watch pattern to expose some server data.
 *
 * @author smaject.com
 */
@Singleton
@Startup
public class ServerWatch {
    private ZonedDateTime serverStartTime;
    private MemoryUsage memoryUsageAtStartTime;
    private InetAddress hostAddress;
    private MemoryMXBean memoryMXBean;

    @PostConstruct
    public void init() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();

        this.serverStartTime = ZonedDateTime.now();
        this.memoryUsageAtStartTime = this.memoryMXBean.getHeapMemoryUsage();
        this.hostAddress = lookupHost();
    }

    private InetAddress lookupHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public String getMemoryUsageAtStartTime() {
        return memoryUsageAtStartTime.toString();
    }

    public ZonedDateTime getServerStartTime() {
        return serverStartTime;
    }

    public double getCurrentMemoryUsageInMb() {
        final MemoryUsage currentMemoryUsage = this.memoryMXBean.getHeapMemoryUsage();
        return toMb(currentMemoryUsage.getUsed());
    }

    public double getAvailableMemoryInMb() {
        final MemoryUsage currentMemoryUsage = this.memoryMXBean.getHeapMemoryUsage();
        final long availableMemory = currentMemoryUsage.getCommitted() - currentMemoryUsage.getUsed();
        return toMb(availableMemory);
    }

    public JsonObject getHostInfo() {
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        if (hostAddress == null) {
            return objectBuilder.add("host", "<unknown>").build();
        }
        return objectBuilder.add("host name", this.hostAddress.getHostName())
                .add("canonical host name", this.hostAddress.getCanonicalHostName())
                .add("host address", this.hostAddress.getHostAddress())
                .build();
    }

    public String getServerUpTimeInHms() {
        return toHms(ChronoUnit.MILLIS.between(this.getServerStartTime(), ZonedDateTime.now()));
    }

    public JsonObject getOsInfo() {
        final OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        return Json.createObjectBuilder()
                .add("OS Name", osMXBean.getName())
                .add("Version", osMXBean.getVersion())
                .add("Architecture", osMXBean.getArch())
                .add("Available CPUs", osMXBean.getAvailableProcessors())
                .add("System Load Average", osMXBean.getSystemLoadAverage())
                .build();
    }

    public JsonObject getVmInfo() {
        final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Json.createObjectBuilder()
                .add("VM Name", runtimeMXBean.getVmName())
                .add("VM Vendor", runtimeMXBean.getVmVendor())
                .add("VM Version", runtimeMXBean.getVmVersion())
                .build();
    }

    private String toHms(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private double toMb(long bytes) {
        return bytes / 1024 / 1024;
    }
}
