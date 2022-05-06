package com.bqiao.demo.vpets.api;

import com.bqiao.demo.vpets.api.handler.MetadataRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MetadataRoute {
    @Autowired
    private MetadataRequestHandler handler;

    @Bean
    RouterFunction<ServerResponse> metadataRoutes() {
        return route(GET("/metadata"), handler::all)
                .andRoute(POST("/metadata"), handler::create)
                .andRoute(GET("/metadata/{id}"), handler::get)
                .andRoute(PUT("/metadata/{id}"), handler::update)
                .andRoute(DELETE("/metadata/{id}"), handler::delete);
    }
}
