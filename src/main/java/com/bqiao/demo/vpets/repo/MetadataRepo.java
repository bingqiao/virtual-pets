package com.bqiao.demo.vpets.repo;

import com.bqiao.demo.vpets.domain.Metadata;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepo extends ReactiveMongoRepository<Metadata, String> {
}
