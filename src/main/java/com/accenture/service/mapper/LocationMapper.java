package com.accenture.service.mapper;

import com.accenture.repository.entity.Location;
import com.accenture.service.dto.LocationRequestDto;
import com.accenture.service.dto.LocationResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {


    Location toLocation (LocationRequestDto locationRequestDto);

    LocationResponseDto toLocationResponseDto (Location location);
}
