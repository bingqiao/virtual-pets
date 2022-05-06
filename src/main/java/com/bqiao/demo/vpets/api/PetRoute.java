package com.bqiao.demo.vpets.api;

import com.bqiao.demo.vpets.api.handler.PetRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PetRoute {
    @Autowired
    private PetRequestHandler handler;

    @Bean
    RouterFunction<ServerResponse> petRoutes() {
        return route(GET("/pets"), handler::all)
                .andRoute(POST("/pets"), handler::create)
                .andRoute(GET("/pets/{id}"), handler::get)
                .andRoute(PUT("/pets/{id}"), handler::update)
                .andRoute(DELETE("/pets/{id}"), handler::delete)
                .andRoute(PUT("/pets/{id}/action/{code}"), handler::action);
    }
}
