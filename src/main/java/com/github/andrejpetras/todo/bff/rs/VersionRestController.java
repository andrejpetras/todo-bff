package com.github.andrejpetras.todo.bff.rs;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("version")
public class VersionRestController {

    @ConfigProperty(name = "quarkus.application.version")
    String version;

    @ConfigProperty(name = "quarkus.application.name")
    String name;

    @GET
    public Response getVersion() {
        return Response.ok(name + ":" + version).build();
    }
}
