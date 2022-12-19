package com.github.andrejpetras.todo.bff.rs;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.ext.web.Router;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class ProxyRestController {

    private final String baseUrl;
    private final HttpClient httpClient;

    ProxyRestController(Vertx vertx, @ConfigProperty(name = "todo.core.url") String url ) {
        this.httpClient = vertx.createHttpClient(new HttpClientOptions());
        this.baseUrl = url;
    }

    public void init(@Observes Router router) {
        router.route("/api/*").handler(ProxyHandler.create(httpClient, baseUrl, (url) ->  url.replaceFirst("/api/", "")));
    }
}
