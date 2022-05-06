package com.bqiao.demo.vpets.repo;

import com.bqiao.demo.vpets.domain.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends ReactiveMongoRepository<Player, String> {
}
