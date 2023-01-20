package com.github.andrejpetras.todo.bff.rs;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;


@Path("rollout")
@Produces(MediaType.APPLICATION_JSON)
public class RolloutRestController {

    @ConfigProperty(name = "app.rollout.smoke-test.ok", defaultValue = "true")
    boolean smokeTestOk;


    @GET
    @Path("smoke-test")
    public Response smokeTest() {
        return Response.ok(Map.of("data", Map.of("ok", smokeTestOk))).build();
    }

}
