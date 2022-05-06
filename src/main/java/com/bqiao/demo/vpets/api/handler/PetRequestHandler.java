package com.bqiao.demo.vpets.api.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PetRequestHandler {
    Mono<ServerResponse> all(ServerRequest request);
    Mono<ServerResponse> create(ServerRequest request);
    Mono<ServerResponse> get(ServerRequest request);
    Mono<ServerResponse> update(ServerRequest request);
    Mono<ServerResponse> delete(ServerRequest request);
    Mono<ServerResponse> action(ServerRequest request);
}
