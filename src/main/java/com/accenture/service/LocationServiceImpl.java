package com.accenture.service;

import com.accenture.exception.LocationException;
import com.accenture.repository.LocationDao;
import com.accenture.repository.entity.Location;
import com.accenture.service.dto.LocationRequestDto;
import com.accenture.service.dto.LocationResponseDto;
import com.accenture.service.mapper.LocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationDao locationDao;
    private final LocationMapper locationMapper;


    public LocationServiceImpl(LocationDao locationDao, LocationMapper locationMapper) {
        this.locationDao = locationDao;
        this.locationMapper = locationMapper;
    }

    @Override
    public LocationResponseDto ajouter(LocationRequestDto locationRequestDto) throws LocationException {

        verifLocation(locationRequestDto);
        Location location = locationMapper.toLocation(locationRequestDto);

        Location locationEnreg = locationDao.save(location);
        return locationMapper.toLocationResponseDto(locationEnreg);
    }

    @Override
    public List<LocationResponseDto> trouverToutes() {
        return locationDao.findAll().stream()
                .map(locationMapper::toLocationResponseDto)
                .toList();
    }



    private static void verifLocation(LocationRequestDto locationRequestDto) {
        if (locationRequestDto == null)
            throw new LocationException("La locationRequestDto est null");
        if (locationRequestDto.clientMail() == null )
            throw new LocationException("Vous devez ajouter l'email du client");
        if (locationRequestDto.vehiculeId() == null)
            throw new LocationException("Vous devez ajouter l'id du  véhicule");
        if (locationRequestDto.dateDebut() == null)
            throw new LocationException("Vous devez ajouter la date de début de la location");
        if (locationRequestDto.dateFin() == null)
            throw new LocationException("Vous devez ajouter la date de fin de la location");
        if (locationRequestDto.kilometresParcourus() < 0)
            throw new LocationException("Vous devez ajouter les kilomètres parcourus");
        if (locationRequestDto.montantTotal() <= 0)
            throw new LocationException("Vous devez ajouter le montant total de la location");
        if (locationRequestDto.etat() == null)
            throw new LocationException("Vous devez ajouter l'état de la location");
    }


}
