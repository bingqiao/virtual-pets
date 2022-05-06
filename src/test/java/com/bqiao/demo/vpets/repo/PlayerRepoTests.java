package com.bqiao.demo.vpets.repo;

import com.bqiao.demo.vpets.domain.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
@DataMongoTest
public class PlayerRepoTests {
    @Autowired
    private PlayerRepo underTest;

    @Test
    public void givenPlayer_whenSave_thenSaved() {
        Player player = Player.builder()
                .id("100")
                .name("hundred")
                .build();
        Publisher<Player> setup = underTest.deleteAll().thenMany(underTest.save(player));
        StepVerifier.create(setup)
                .consumeNextWith(o -> {
                    Assertions.assertEquals("hundred", o.getName());
                })
                .verifyComplete();
    }
}
