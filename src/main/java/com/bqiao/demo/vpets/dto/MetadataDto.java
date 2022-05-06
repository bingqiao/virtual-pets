package com.bqiao.demo.vpets.dto;

import com.bqiao.demo.vpets.domain.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDto {
    private String id;
    private String version;
    private Map<String, PetType> petTypes;
}
