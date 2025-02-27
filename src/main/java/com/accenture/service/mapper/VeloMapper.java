package com.accenture.service.mapper;

import com.accenture.repository.entity.Velo;
import com.accenture.service.dto.VeloRequestDto;
import com.accenture.service.dto.VeloResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeloMapper {
    Velo toVelo(VeloRequestDto veloRequestDto);
    VeloResponseDto toVeloResponseDto(Velo velo);
}