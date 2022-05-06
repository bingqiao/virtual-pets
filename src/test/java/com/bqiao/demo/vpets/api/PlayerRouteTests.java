package com.bqiao.demo.vpets.api;

import com.bqiao.demo.vpets.api.handler.impl.PlayerRequestHandlerImpl;
import com.bqiao.demo.vpets.domain.Player;
import com.bqiao.demo.vpets.dto.PlayerDto;
import com.bqiao.demo.vpets.repo.PlayerRepo;
import com.bqiao.demo.vpets.service.PetService;
import com.bqiao.demo.vpets.service.impl.PlayerServiceImpl;
import com.bqiao.demo.vpets.util.PlayerMapper;
import com.bqiao.demo.vpets.util.PlayerMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PlayerRoute.class, PlayerRequestHandlerImpl.class,
        PlayerServiceImpl.class, PlayerMapperImpl.class})
public class PlayerRouteTests {
    @Autowired
    private PlayerRoute config;

    @MockBean
    private PlayerRepo repo;

    @MockBean
    private PetService petService;

    @Test
    public void givenPlayerId_whenGetPlayerById_thenCorrectPlayer() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.playerRoutes())
                .build();

        Player player = new Player("1", "Player 1");
        PlayerDto dto = PlayerMapper.INSTANCE.toDTO(player);

        when(repo.findById("1")).thenReturn(Mono.just(player));

        client.get()
                .uri("/players/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlayerDto.class).isEqualTo(dto);
    }

    @Test
    public void whenGetAllPlayers_thenCorrectPlayers() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.playerRoutes())
                .build();

        List<Player> players = Arrays.asList(
                new Player("1", "Player 1"),
                new Player("2", "Player 2")
        );
        List<PlayerDto> dtos = players.stream().map(PlayerMapper.INSTANCE::toDTO).collect(Collectors.toList());

        Flux<Player> domains = Flux.fromIterable(players);
        when(repo.findAll()).thenReturn(domains);

        client.get()
                .uri("/players")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlayerDto.class).isEqualTo(dtos);
    }

    @Test
    public void whenUpdatePlayer_thenPlayerUpdated() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(config.playerRoutes())
                .build();

        PlayerDto dto = new PlayerDto("1", "Player 1 Updated");
        Player domain = new Player("1", "Player 1");
        Player updated = PlayerMapper.INSTANCE.fromDTO(dto);

        when(repo.findById("1")).thenReturn(Mono.just(domain));
        when(repo.save(updated)).thenReturn(Mono.just(updated));

        client.put()
                .uri("/players/1")
                .body(Mono.just(dto), PlayerDto.class)
                .exchange()
                .expectStatus().isNoContent();

        verify(repo).save(updated);
    }
}
