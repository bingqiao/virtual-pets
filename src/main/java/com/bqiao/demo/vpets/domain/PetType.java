package com.bqiao.demo.vpets.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class PetType {
    private String code;
    private String description;
    private Map<String, Property> properties;
    private Map<String, Action> actions;
    // TODO not used currently
    private String version;
}
