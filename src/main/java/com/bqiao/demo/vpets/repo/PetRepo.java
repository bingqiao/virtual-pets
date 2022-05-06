package com.bqiao.demo.vpets.repo;

import com.bqiao.demo.vpets.domain.Pet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PetRepo extends ReactiveMongoRepository<Pet, String> {
    Flux<Pet> findByPlayerId(String playerId);
}
