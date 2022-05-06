package com.bqiao.demo.vpets.util;

import com.bqiao.demo.vpets.domain.Pet;
import com.bqiao.demo.vpets.dto.PetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PetMapper {
    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);


    PetDto toDTO(final Pet domain);

    @Mapping(target = "version", ignore = true)
    Pet fromDTO(final PetDto dto);
}
