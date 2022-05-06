package com.bqiao.demo.vpets.api.handler.impl;

import com.bqiao.demo.vpets.api.handler.PlayerRequestHandler;
import com.bqiao.demo.vpets.domain.Player;
import com.bqiao.demo.vpets.dto.PlayerDto;
import com.bqiao.demo.vpets.dto.PetDto;
import com.bqiao.demo.vpets.service.PetService;
import com.bqiao.demo.vpets.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class PlayerRequestHandlerImpl implements PlayerRequestHandler {
    @Autowired
    private RepoService<PlayerDto> service;
    @Autowired
    private PetService petService;

    @Override
    public Mono<ServerResponse> all(ServerRequest request) {
        return ok().body(service.all(), Player.class);
    }

    @Override
    public Mono<ServerResponse> pets(ServerRequest request) {
        return ok().body(petService.findByPlayerId(request.pathVariable("id")), PetDto.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(PlayerDto.class)
                .flatMap(dto -> service.create(dto))
                .flatMap(p -> ServerResponse.created(URI.create("/players/" + p.getId())).build());
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        return service.get(request.pathVariable("id"))
                .flatMap(dto -> {
                    return ServerResponse.ok().body(Mono.just(dto), PlayerDto.class);
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(PlayerDto.class)
                .map(dto -> {
                    String id = request.pathVariable("id");
                    dto.setId(id);
                    return dto;
                })
                .flatMap(dto -> service.update(dto))
                .flatMap(p -> ServerResponse.noContent().build());
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        return ServerResponse.noContent().build(service.delete(request.pathVariable("id")));
    }
}
