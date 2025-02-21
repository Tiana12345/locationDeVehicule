package com.accenture.service.mapper;

import com.accenture.repository.entity.Adresse;
import com.accenture.service.dto.AdresseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdresseMapper {
    Adresse toAdresse (AdresseDto adresseDto);
    AdresseDto toAdresseDto (Adresse adresse);
}
