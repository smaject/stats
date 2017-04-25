package com.smaject.stats.boundary;

import com.smaject.stats.control.JsonCollectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Resource that exposes health information provided by the environment.
 *
 * @author smaject.com
 */
@Path("health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheckResource {

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "OK - " + System.currentTimeMillis();
    }

    @GET
    @Path("/echo/{echo}")
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(@PathParam("echo") String input) {
        return input;
    }

    @GET
    @Path("/system-properties")
    public JsonObject systemProperties() {
        final Properties properties = System.getProperties();
        final Set<Map.Entry<Object, Object>> entries = properties.entrySet();

        return entries.stream().collect(JsonCollectors.toJsonBuilder()).build();
    }

    @GET
    @Path("/env-variables")
    public JsonObject environmentVariables() {
        Map<String, String> environment = System.getenv();
        return environment.entrySet().stream().collect(JsonCollectors.toJsonBuilder()).build();
    }

    @GET
    @Path("/jndi/{namespace}")
    public JsonObject jndi(@PathParam("namespace") String namespace) throws NamingException {
        InitialContext context = new InitialContext();
        NamingEnumeration<NameClassPair> jndiEntries = context.list(namespace);

        JsonObjectBuilder builder = Json.createObjectBuilder();
        while (jndiEntries.hasMoreElements()) {
            NameClassPair nameClassPair = jndiEntries.nextElement();
            String name = nameClassPair.getName();
            String type = nameClassPair.getClassName();
            builder.add(name, type);
        }
        return builder.build();
    }
}
