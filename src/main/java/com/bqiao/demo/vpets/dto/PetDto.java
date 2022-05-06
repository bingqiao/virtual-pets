package com.bqiao.demo.vpets.dto;

import com.bqiao.demo.vpets.domain.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private String id;
    private String playerId;
    private PetType type;
    private String name;
    private Map<String, String> properties;
    private Date lastActionPlayed;
}
