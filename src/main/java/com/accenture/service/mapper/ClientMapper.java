package com.accenture.service.mapper;

import com.accenture.repository.entity.Adresse;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.AdresseDto;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses= {AdresseMapper.class})
public interface ClientMapper {
    Client toClient (ClientRequestDto clientRequestDto);
    ClientResponseDto toClientResponseDto (Client client);


}
