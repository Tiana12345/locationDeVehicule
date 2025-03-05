package com.accenture.service;

import com.accenture.exception.LocationException;
import com.accenture.service.dto.LocationRequestDto;
import com.accenture.service.dto.LocationResponseDto;

import java.util.List;

public interface LocationService {
    LocationResponseDto ajouter(LocationRequestDto locationRequestDto) throws LocationException;

    List<LocationResponseDto> trouverToutes();
}
