package com.bqiao.demo.vpets.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Action {
    private String code;
    private String target;
    private int effect;
}
