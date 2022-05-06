package com.bqiao.demo.vpets.service.impl;

import com.bqiao.demo.vpets.domain.Player;
import com.bqiao.demo.vpets.dto.PlayerDto;
import com.bqiao.demo.vpets.repo.PlayerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PlayerServiceImplTests {
    @Mock
    private PlayerRepo repo;
    @InjectMocks
    private PlayerServiceImpl underTest;

    @Test
    public void givenPlayerPresent_whenGet_thenFound() {
        Player found = Player.builder()
                .id("100")
                .name("hundred")
                .build();
        when(repo.findById("100")).thenReturn(Mono.just(found));
        Mono<PlayerDto> setup = underTest.get("100");
        StepVerifier.create(setup)
                .consumeNextWith(o -> {
                    Assertions.assertEquals(o.getName(),"hundred");
                })
                .verifyComplete();
    }

    @Test
    public void givenPlayerAbsent_whenGet_thenNotFound() {
        when(repo.findById("100")).thenReturn(Mono.empty());
        Mono<PlayerDto> setup = underTest.get("100");
        StepVerifier
                .create(setup)
                .verifyComplete();
    }

    @Test
    public void shouldNotUpdateIfNotFound() {
        PlayerDto missing = PlayerDto.builder()
                .id("100")
                .name("hundred")
                .build();
        when(repo.findById("100")).thenReturn(Mono.empty());
        Mono<PlayerDto> setup = underTest.update(missing);
        StepVerifier
                .create(setup)
                .verifyComplete();
    }
}
