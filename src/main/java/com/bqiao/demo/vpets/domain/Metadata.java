package com.bqiao.demo.vpets.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Metadata {
    private String id;
    // TODO not used currently
    private String version;
    private Map<String, PetType> petTypes;
}
