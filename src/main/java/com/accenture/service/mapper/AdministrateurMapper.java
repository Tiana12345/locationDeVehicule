package com.accenture.service.mapper;

import com.accenture.repository.entity.Administrateur;
import com.accenture.service.dto.AdministrateurRequestDto;
import com.accenture.service.dto.AdministrateurResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministrateurMapper{

    Administrateur toAdministrateur (AdministrateurRequestDto administrateurRequestDto);
    AdministrateurResponseDto toAdministrateurResponseDto (Administrateur administrateur);
}
