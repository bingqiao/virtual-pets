package com.bqiao.demo.vpets.service.impl;

import com.bqiao.demo.vpets.domain.Metadata;
import com.bqiao.demo.vpets.dto.MetadataDto;
import com.bqiao.demo.vpets.repo.MetadataRepo;
import com.bqiao.demo.vpets.service.RepoService;
import com.bqiao.demo.vpets.util.MetadataMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MetadatarServiceImpl implements RepoService<MetadataDto> {
    private final MetadataRepo repo;

    public MetadatarServiceImpl(MetadataRepo repo) {
        this.repo = repo;
    }

    @Override
    public Flux<MetadataDto> all() {
        return repo.findAll()
                .map(MetadataMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<MetadataDto> create(MetadataDto toCreate) {
        Metadata toSave = MetadataMapper.INSTANCE.fromDTO(toCreate);
        return repo.save(toSave)
                .map(MetadataMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<MetadataDto> get(String id) {
        return repo.findById(id)
                .map(MetadataMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<MetadataDto> update(MetadataDto toUpdate) {
        return repo.findById(toUpdate.getId()).map(
                metadata -> {
                    metadata.setPetTypes(toUpdate.getPetTypes());
                    return metadata;
                }
        ).flatMap(domain -> repo.save(domain))
                .map(MetadataMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.repo.deleteById(id);
    }
}
