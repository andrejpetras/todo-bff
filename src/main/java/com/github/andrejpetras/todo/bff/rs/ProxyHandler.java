package com.github.andrejpetras.todo.bff.rs;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;


public class ProxyHandler implements Handler<RoutingContext> {

    private static final Logger log = LoggerFactory.getLogger(ProxyHandler.class);

    private final String baseUrl;
    private final Function<String, String> urlProcessor;
    private HttpClient httpClient;

    private ProxyHandler(HttpClient client, String baseUrl, Function<String, String> urlProcessor) {
        this.httpClient = client;
        this.baseUrl = baseUrl;
        this.urlProcessor = urlProcessor;
    }

    public static ProxyHandler create(HttpClient httpClient, String baseUrl, Function<String, String> urlProcessor) {
        return new ProxyHandler(httpClient, baseUrl, urlProcessor);
    }


    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerRequest serverRequest = routingContext.request();
        String proxyRequestUri = urlProcessor != null ? urlProcessor.apply(serverRequest.uri()) : serverRequest.uri();
        log.info("Proxying request {} {} to : {}", serverRequest.method().name(), serverRequest.uri(), this.baseUrl + proxyRequestUri);
        HttpServerResponse serverResponse = serverRequest.response();
        httpClient.request(new RequestOptions()
                        .setAbsoluteURI(this.baseUrl + proxyRequestUri)
                        .setMethod(serverRequest.method()))
                .onSuccess(clientRequest -> {
                    clientRequest.headers().setAll(serverRequest.headers().remove("Host"));
                    clientRequest.send(serverRequest).onSuccess(clientResponse -> {
                        log.info("Proxying response from {}: {}",clientRequest.absoluteURI() , clientResponse.statusCode());
                        serverResponse.setStatusCode(clientResponse.statusCode());
                        serverResponse.headers().setAll(clientResponse.headers());
                        serverResponse.send(clientResponse);
                    }).onFailure(err -> {
                        log.error("Back end failure", err);
                        serverResponse.setStatusCode(500).end();
                    });
                }).onFailure(err -> {
                    log.error("Could not connect to server {}", baseUrl, err);
                    serverResponse.setStatusCode(500).end();
                });

    }
}

