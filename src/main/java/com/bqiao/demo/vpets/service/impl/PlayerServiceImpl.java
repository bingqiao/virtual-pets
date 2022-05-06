package com.bqiao.demo.vpets.service.impl;

import com.bqiao.demo.vpets.domain.Player;
import com.bqiao.demo.vpets.dto.PlayerDto;
import com.bqiao.demo.vpets.repo.PlayerRepo;
import com.bqiao.demo.vpets.service.RepoService;
import com.bqiao.demo.vpets.util.PlayerMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PlayerServiceImpl implements RepoService<PlayerDto> {
    private final PlayerRepo repo;

    public PlayerServiceImpl(PlayerRepo repo) {
        this.repo = repo;
    }

    @Override
    public Flux<PlayerDto> all() {
        return repo.findAll()
                .map(PlayerMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<PlayerDto> create(PlayerDto toCreate) {
        Player toSave = Player.builder().id(toCreate.getId()).name(toCreate.getName()).build();
        return repo.save(toSave)
                .map(PlayerMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<PlayerDto> get(String id) {
        return repo.findById(id)
                .map(PlayerMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<PlayerDto> update(PlayerDto toUpdate) {
        return repo.findById(toUpdate.getId()).map(
                domain -> {
                    domain.setName(toUpdate.getName());
                    return domain;
                }
        ).flatMap(domain -> repo.save(domain))
                .map(PlayerMapper.INSTANCE::toDTO);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.repo.deleteById(id);
    }
}
