package com.bqiao.demo.vpets.service;

import com.bqiao.demo.vpets.dto.PetDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetService extends RepoService<PetDto> {
    Mono<PetDto> play(String id, String code);
    Flux<PetDto> findByPlayerId(String userId);
}
