package com.bqiao.demo.vpets.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Property {
    private String code;
    // TODO not used currently
    private String type;
    private long max;
    private long min;
    private long rate;
    private String rateUnit;
}
