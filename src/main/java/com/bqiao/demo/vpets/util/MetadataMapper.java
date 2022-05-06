package com.bqiao.demo.vpets.util;

import com.bqiao.demo.vpets.domain.Metadata;
import com.bqiao.demo.vpets.dto.MetadataDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MetadataMapper {
    MetadataMapper INSTANCE = Mappers.getMapper(MetadataMapper.class);

    MetadataDto toDTO(final Metadata domain);

    Metadata fromDTO(final MetadataDto dto);
}
