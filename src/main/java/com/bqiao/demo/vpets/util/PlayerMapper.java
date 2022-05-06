package com.bqiao.demo.vpets.util;

import com.bqiao.demo.vpets.domain.Player;
import com.bqiao.demo.vpets.dto.PlayerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerDto toDTO(final Player domain);

    Player fromDTO(final PlayerDto dto);
}
