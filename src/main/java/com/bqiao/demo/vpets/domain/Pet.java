package com.bqiao.demo.vpets.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    private String id;
    private String playerId;
    private PetType type;
    private String name;
    private Map<String, String> properties;
    // TODO not used currently
    private String version;
    private Date lastActionPlayed;
}
