package com.bqiao.demo.vpets.service;

import com.bqiao.demo.vpets.domain.Pet;
import reactor.core.publisher.Mono;

public interface ActionService {
    Pet apply(Pet pet, String action);
}
