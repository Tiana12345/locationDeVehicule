package com.accenture.service.mapper;

import com.accenture.repository.entity.Utilitaire;
import com.accenture.service.dto.UtilitaireRequestDto;
import com.accenture.service.dto.UtilitaireResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilitaireMapper {
    Utilitaire toUtilitaire(UtilitaireRequestDto utilitaireRequestDto);
    UtilitaireResponseDto toUtilitaireResponseDto(Utilitaire utilitaire);
}