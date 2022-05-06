package com.bqiao.demo.vpets.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepoService<T> {
    Flux<T> all();
    Mono<T> create(T dto);
    Mono<T> get(String id);
    Mono<T> update(T dto);
    Mono<Void> delete(String id);
}
