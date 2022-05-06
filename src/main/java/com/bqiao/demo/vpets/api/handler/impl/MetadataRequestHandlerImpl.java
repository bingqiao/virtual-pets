package com.bqiao.demo.vpets.api.handler.impl;

import com.bqiao.demo.vpets.api.handler.MetadataRequestHandler;
import com.bqiao.demo.vpets.domain.Metadata;
import com.bqiao.demo.vpets.dto.MetadataDto;
import com.bqiao.demo.vpets.dto.PlayerDto;
import com.bqiao.demo.vpets.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class MetadataRequestHandlerImpl implements MetadataRequestHandler {
    @Autowired
    private RepoService<MetadataDto> service;

    @Override
    public Mono<ServerResponse> all(ServerRequest request) {
        return ok().body(service.all(), Metadata.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(MetadataDto.class)
                .flatMap(dto -> service.create(dto))
                .flatMap(p -> ServerResponse.created(URI.create("/metadata/" + p.getId())).build());
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        return service.get(request.pathVariable("id"))
                .flatMap(dto -> ServerResponse.ok().body(Mono.just(dto), PlayerDto.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(MetadataDto.class)
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
