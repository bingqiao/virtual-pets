package com.bqiao.demo.vpets.api;

import com.bqiao.demo.vpets.api.handler.PlayerRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PlayerRoute {
    @Autowired
    private PlayerRequestHandler handler;

    @Bean
    RouterFunction<ServerResponse> playerRoutes() {
        return route(GET("/players"), handler::all)
                .andRoute(POST("/players"), handler::create)
                .andRoute(GET("/players/{id}"), handler::get)
                .andRoute(PUT("/players/{id}"), handler::update)
                .andRoute(DELETE("/players/{id}"), handler::delete)
                .andRoute(GET("/players/{id}/pets"), handler::pets);
    }
}
