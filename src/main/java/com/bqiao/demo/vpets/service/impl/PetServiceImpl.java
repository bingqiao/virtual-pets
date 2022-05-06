package com.bqiao.demo.vpets.service.impl;

import com.bqiao.demo.vpets.domain.Pet;
import com.bqiao.demo.vpets.domain.Property;
import com.bqiao.demo.vpets.dto.MetadataDto;
import com.bqiao.demo.vpets.dto.PlayerDto;
import com.bqiao.demo.vpets.dto.PetDto;
import com.bqiao.demo.vpets.repo.PetRepo;
import com.bqiao.demo.vpets.service.ActionService;
import com.bqiao.demo.vpets.service.PetService;
import com.bqiao.demo.vpets.service.RepoService;
import com.bqiao.demo.vpets.util.PetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class PetServiceImpl implements PetService {
    private final PetRepo repo;
    private final ActionService actionService;

    @Value("${metadata:1}")
    private String metadataId;

    private final RepoService<MetadataDto> metadataService;
    private final RepoService<PlayerDto> playerService;

    public PetServiceImpl(PetRepo repo, ActionService actionService,
                          RepoService<MetadataDto> metadataService,
                          RepoService<PlayerDto> playerService) {
        this.repo = repo;
        this.actionService = actionService;
        this.metadataService = metadataService;
        this.playerService = playerService;
    }

    @Override
    public Flux<PetDto> all() {
        return repo.findAll()
                .map(
                        domain -> {
                            return actionService.apply(domain, null);
                        }
                ).map(PetMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<PetDto> create(PetDto dto) {
        Pet domain = PetMapper.INSTANCE.fromDTO(dto);
        return playerService.get(dto.getPlayerId())
                .flatMap(playerDto -> metadataService.get(metadataId))
                        .mapNotNull(metadata -> {
                                return metadata.getPetTypes().get(dto.getType().getCode());
                        }).map(type -> {
                            domain.setType(type);
                            domain.setLastActionPlayed(new Date());
                            Map<String, Property> properties = type.getProperties();
                            Map<String, String> props = new HashMap<>();
                            properties.forEach((k, v) -> {
                                props.put(k, String.valueOf((v.getMax() - v.getMin())/2));
                            });
                            domain.setProperties(props);
                            return domain;})
                        .flatMap(repo::save).map(PetMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<PetDto> get(String id) {
        return repo.findById(id)
                .map(
                        domain -> actionService.apply(domain, null)
                ).map(PetMapper.INSTANCE::toDTO);
    }

    /**
     * Update pet properties by player.
     * Only name of pet can be updated via this method.
     * @param dto the pet to be updated
     * @return Mono of updated pet
     */
    @Override
    public Mono<PetDto> update(PetDto dto) {
        return repo.findById(dto.getId()).map(
                    domain -> {
                        domain.setName(dto.getName());
                        return domain;
                    }
                ).flatMap(domain -> repo.save(domain))
                .map(PetMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.repo.deleteById(id);
    }

    @Override
    public Mono<PetDto> play(String id, String code) {
        return repo.findById(id).map(
                    domain -> actionService.apply(domain, code)
                ).flatMap(domain -> repo.save(domain))
                .map(PetMapper.INSTANCE::toDTO);
    }

    @Override
    public Flux<PetDto> findByPlayerId(String playerId) {
        return repo.findByPlayerId(playerId).map(
                        domain -> {
                            return actionService.apply(domain, null);
                        }
                ).map(PetMapper.INSTANCE::toDTO);
    }
}
