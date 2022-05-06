package com.bqiao.demo.vpets.api.handler.impl;

import com.bqiao.demo.vpets.api.handler.PetRequestHandler;
import com.bqiao.demo.vpets.domain.Pet;
import com.bqiao.demo.vpets.dto.PetDto;
import com.bqiao.demo.vpets.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class PetRequestHandlerImpl implements PetRequestHandler {
    @Autowired
    private PetService service;

    @Override
    public Mono<ServerResponse> all(ServerRequest request) {
        return ok().body(service.all(), Pet.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(PetDto.class)
                .flatMap(dto -> service.create(dto))
                .flatMap(p -> ServerResponse.created(URI.create("/pets/" + p.getId())).body(Mono.just(p), PetDto.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        return service.get(request.pathVariable("id"))
                .flatMap(dto -> ServerResponse.ok().body(Mono.just(dto), PetDto.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(PetDto.class)
                .map(dto -> {
                    String id = request.pathVariable("id");
                    dto.setId(id);
                    return dto;
                })
                .flatMap(service::update)
                .flatMap(p -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        return ServerResponse.noContent().build(service.delete(request.pathVariable("id")));
    }

    @Override
    public Mono<ServerResponse> action(ServerRequest request) {
        return service.play(request.pathVariable("id"), request.pathVariable("code"))
                .flatMap(dto -> ServerResponse.ok().body(Mono.just(dto), PetDto.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
