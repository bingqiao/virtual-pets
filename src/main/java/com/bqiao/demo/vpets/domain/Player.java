package com.bqiao.demo.vpets.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
public class Player {
    @Id
    private String id;
    private String name;
}
