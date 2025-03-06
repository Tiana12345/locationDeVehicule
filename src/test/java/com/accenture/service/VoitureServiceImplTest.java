package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoitureServiceImplTest {
    @Mock
    VoitureDao daoMock = Mockito.mock(VoitureDao.class);
    @Mock
    VoitureMapper mapperMock;
    @InjectMocks
    VoitureServiceImpl service;

    /*
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
 /\_/\                                                          /\_/\
( o.o )      __  __        _    _                 _            ( o.o )
 > ^ <      |  \/  |  ___ | |_ | |__    ___    __| |  ___       > ^ <
 /\_/\      | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \      /\_/\
( o.o )     | |  | ||  __/| |_ | | | || (_) || (_| ||  __/     ( o.o )
 > ^ <      |_|  |_| ____| \__||_| |_|_\___/  \__,_| \___|      > ^ <
 /\_/\        __ _  (_)  ___   _   _ | |_  ___  _ __            /\_/\
( o.o )      / _` | | | / _ \ | | | || __|/ _ \| '__|          ( o.o )
 > ^ <      | (_| | | || (_) || |_| || |_|  __/| |              > ^ <
 /\_/\       \__,_|_/ | \___/  \__,_| \__|\___||_|              /\_/\
( o.o )           |__/                                         ( o.o )
 > ^ <                                                          > ^ <
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
      */
    @DisplayName("Si ajouter (null) exception levée")
    @Test
    void testAjouterSiNull() {
        assertThrows(VehiculeException.class, () -> service.ajouter(null));
    }

    @Test
    void testAjouterSiMarqueNull() {
        VoitureRequestDto dto = new VoitureRequestDto(null,
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiMarqueBlank() {
        VoitureRequestDto dto = new VoitureRequestDto("      \t   ",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiModeleNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                null,
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiModeleBlank() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "    \t  ",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiCouleurNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                null,
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiCouleurBlank() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "    \t  ",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiTypeNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                null,
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiTypeBlank() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "   \t  ",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombrePlaceNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                null,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombrePlaceInf0() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                -10,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombrePlaceEgal0() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                0,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombrePorteNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                null,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombrePortesEgal0() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                0,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombrePortesInf0() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                -10,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSTransmissionNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                null,
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSTransmissionBlank() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "   \t ",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }


    @Test
    void testAjouterSiClimNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                null,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombreBagagesNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                null,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiNombreBagagesInf0() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                -10,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }


    @Test
    void testAjouterSiCarburantNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                null,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiTarifJournalierNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                0,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiTarifJournalierInf0() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                -100,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiKilometrageInf0() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                -10,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiActifNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                null,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiRetireDuParcNull() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                null);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterOk() {
        Voiture voitureAvantEnreg = creerVoiture();
        voitureAvantEnreg.setId(1);
        VoitureRequestDto requestDto = getVoitureRequestDto();

        Voiture voitureApresEnreg = creerVoiture();
        VoitureResponseDto responseDto = creerVoitureResponseDto();

        when(mapperMock.toVoiture(requestDto)).thenReturn(voitureAvantEnreg);
        when(daoMock.save(voitureAvantEnreg)).thenReturn(voitureApresEnreg);
        when(mapperMock.toVoitureResponseDto(voitureApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestDto));
        verify(daoMock, Mockito.times(1)).save(voitureAvantEnreg);
    }

 /*

  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
 ( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
  /\_/\       __  __        _    _                 _             /\_/\
 ( o.o )     |  \/  |  ___ | |_ | |__    ___    __| |  ___      ( o.o )
  > ^ <      | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \      > ^ <
  /\_/\      | |  | ||  __/| |_ | | | || (_) || (_| ||  __/      /\_/\
 ( o.o )     |_|  |_| \___| \__||_| |_| \___/  \__,_| \___|     ( o.o )
  > ^ <      | |_  _ __  ___   _   _ __   __ ___  _ __           > ^ <
  /\_/\      | __|| '__|/ _ \ | | | |\ \ / // _ \| '__|          /\_/\
 ( o.o )     | |_ | |  | (_) || |_| | \ V /|  __/| |            ( o.o )
  > ^ <       \__||_|   \___/  \__,_|  \_/  \___||_|             > ^ <
  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
 ( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
  */
    @Test
    void testTrouverExistePas() {
        when(daoMock.findById(56L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver(56L));
        assertEquals("Erreur, aucune voiture trouvée avec cet id", ex.getMessage());

    }

    @Test
    void testTrouverExiste() {
        Voiture v = creerVoiture();
        Optional<Voiture> optionalVoiture = Optional.of(v);
        when(daoMock.findById(1L)).thenReturn(optionalVoiture);

        VoitureResponseDto dto = creerVoitureResponseDto();
        when(mapperMock.toVoitureResponseDto(v)).thenReturn(dto);

        assertSame(dto, service.trouver(1L));
    }

    @Test
    void testTrouverToutes() {
        Voiture voiture1 = creerVoiture();
        Voiture voiture2 = creerVoiture2();

        List<Voiture> voitures = List.of(creerVoiture(), creerVoiture2());
        List<VoitureResponseDto> dtos = List.of(creerVoitureResponseDto(), creerVoiture2ResponseDto());

        when(daoMock.findAll()).thenReturn(voitures);
        when(mapperMock.toVoitureResponseDto(voiture1)).thenReturn(creerVoitureResponseDto());
        when(mapperMock.toVoitureResponseDto(voiture2)).thenReturn(creerVoiture2ResponseDto());

        assertEquals(dtos, service.trouverToutes());
    }
/*
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
 /\_/\      __  __        _    _                 _              /\_/\
( o.o )    |  \/  |  ___ | |_ | |__    ___    __| |  ___       ( o.o )
 > ^ <     | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \       > ^ <
 /\_/\     | |  | ||  __/| |_ | | | || (_) || (_| ||  __/       /\_/\
( o.o )    |_|  |_| \___| \__||_| __|_\____  ___,_| \___|      ( o.o )
 > ^ <      _ __ ___    ___    __| |(_) / _|(_)  ___  _ __      > ^ <
 /\_/\     | '_ ` _ \  / _ \  / _` || || |_ | | / _ \| '__|     /\_/\
( o.o )    | | | | | || (_) || (_| || ||  _|| ||  __/| |       ( o.o )
 > ^ <     |_| |_| |_| \___/  \__,_||_||_|  |_| \___||_|        > ^ <
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
 */

    @Test
    void modifierPartiellementOk() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = getVoitureRequestDto();
        assertNotNull(requestDto, "Le VoitureRequestDto ne doit pas être null");

        Voiture voitureExistante = creerVoiture();
        Voiture voitureModifiee = creerVoiture2();
        VoitureResponseDto responseDto = creerVoitureResponseDto();

        when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistante));
        when(mapperMock.toVoiture(requestDto)).thenReturn(voitureModifiee);
        when(daoMock.save(voitureExistante)).thenReturn(voitureExistante);
        when(mapperMock.toVoitureResponseDto(voitureExistante)).thenReturn(responseDto);

        VoitureResponseDto result = service.modifierPartiellement(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(daoMock).findById(1L);
        verify(mapperMock).toVoiture(requestDto);
        verify(daoMock).save(voitureExistante);
        verify(mapperMock).toVoitureResponseDto(voitureExistante);
    }

    @Test
    void modifierPartiellemenOk1ParamRempli() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = new VoitureRequestDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                3,
                null,
                0,
                0,
                null,
                null);

        assertNotNull(requestDto, "Le VoitureRequestDto ne doit pas être null");

        Voiture voitureExistante = creerVoiture();
        Voiture voitureModifiee = creerVoiture2();
        VoitureResponseDto responseDto = creerVoitureResponseDto();

        when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistante));
        when(mapperMock.toVoiture(requestDto)).thenReturn(voitureModifiee);
        when(daoMock.save(voitureExistante)).thenReturn(voitureExistante);
        when(mapperMock.toVoitureResponseDto(voitureExistante)).thenReturn(responseDto);

        VoitureResponseDto result = service.modifierPartiellement(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(daoMock).findById(1L);
        verify(mapperMock).toVoiture(requestDto);
        verify(daoMock).save(voitureExistante);
        verify(mapperMock).toVoitureResponseDto(voitureExistante);

    }

    @Test
    void modifierPartiellementNok() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = getVoitureRequestDto();

        when(daoMock.findById(1L)).thenReturn(Optional.empty());

        VehiculeException exception = assertThrows(VehiculeException.class, () -> {
            service.modifierPartiellement(1L, requestDto);
        });

        assertEquals("Erreur, l'identifiant ne correspond à aucune voiture en base", exception.getMessage());

        verify(daoMock).findById(1L);
        verify(daoMock, never()).save(any(Voiture.class));
        verify(mapperMock, never()).toVoiture(any(VoitureRequestDto.class));
        verify(mapperMock, never()).toVoitureResponseDto(any(Voiture.class));
    }

    @Test
    void modifierPartiellementTousParamRemplis() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = new VoitureRequestDto(
                "MarqueTest",
                "ModeleTest",
                "CouleurTest",
                "TypeTest",
                5,
                4,
                "Automatique",
                true,
                3,
                Carburant.ESSENCE,
                100,
                1000,
                true,
                false
        );

        assertNotNull(requestDto, "Le VoitureRequestDto ne doit pas être null");

        Voiture voitureExistante = creerVoiture();
        Voiture voitureModifiee = creerVoiture2();
        VoitureResponseDto responseDto = creerVoitureResponseDto();

        when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistante));
        when(mapperMock.toVoiture(requestDto)).thenReturn(voitureModifiee);
        when(daoMock.save(voitureExistante)).thenReturn(voitureExistante);
        when(mapperMock.toVoitureResponseDto(voitureExistante)).thenReturn(responseDto);

        VoitureResponseDto result = service.modifierPartiellement(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(daoMock).findById(1L);
        verify(mapperMock).toVoiture(requestDto);
        verify(daoMock).save(voitureExistante);
        verify(mapperMock).toVoitureResponseDto(voitureExistante);
    }

    @Test
    void modifierPartiellementValeursNulles() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = new VoitureRequestDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                null,
                null
        );

        assertNotNull(requestDto, "Le VoitureRequestDto ne doit pas être null");

        Voiture voitureExistante = creerVoiture();
        Voiture voitureModifiee = creerVoiture2();
        VoitureResponseDto responseDto = creerVoitureResponseDto();

        when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistante));
        when(mapperMock.toVoiture(requestDto)).thenReturn(voitureModifiee);
        when(daoMock.save(voitureExistante)).thenReturn(voitureExistante);
        when(mapperMock.toVoitureResponseDto(voitureExistante)).thenReturn(responseDto);

        VoitureResponseDto result = service.modifierPartiellement(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(daoMock).findById(1L);
        verify(mapperMock).toVoiture(requestDto);
        verify(daoMock).save(voitureExistante);
        verify(mapperMock).toVoitureResponseDto(voitureExistante);
    }

    @Test
    void modifierPartiellementNokValeursInvalides() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = new VoitureRequestDto(
                "",
                "",
                "", "",
                -1,
                -1,
                null,
                null,
                null,
                null,
                -100,
                -1L,
                null,
                null
        );

        assertNotNull(requestDto, "Le VoitureRequestDto ne doit pas être null");

        Voiture voitureExistante = creerVoiture();
        Voiture voitureModifiee = creerVoiture2();
        VoitureResponseDto responseDto = creerVoitureResponseDto();

        when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistante));
        when(mapperMock.toVoiture(requestDto)).thenReturn(voitureModifiee);
        when(daoMock.save(voitureExistante)).thenReturn(voitureExistante);
        when(mapperMock.toVoitureResponseDto(voitureExistante)).thenReturn(responseDto);

        VoitureResponseDto result = service.modifierPartiellement(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(daoMock).findById(1L);
        verify(mapperMock).toVoiture(requestDto);
        verify(daoMock).save(voitureExistante);
        verify(mapperMock).toVoitureResponseDto(voitureExistante);
    }

 /*
  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
 ( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
  /\_/\    __  __        _    _                 _                       /\_/\
 ( o.o )  |  \/  |  ___ | |_ | |__    ___    __| |  ___                ( o.o )
  > ^ <   | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \                > ^ <
  /\_/\   | |  | ||  __/| |_ | | | || (_) |_ (_| ||  __/                /\_/\
 ( o.o )  |___ |_| \____ ___||_|___| ____/(_)__,__ ____|  ___  _ __    ( o.o )
  > ^ <   / __|| | | || '_ \ | '_ \ | '__|| || '_ ` _ \  / _ \| '__|    > ^ <
  /\_/\   \__ \| |_| || |_) || |_) || |   | || | | | | ||  __/| |       /\_/\
 ( o.o )  |___/ \__,_|| .__/ | .__/ |_|   |_||_| |_| |_| \___||_|      ( o.o )
  > ^ <               |_|    |_|                                        > ^ <
  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
 ( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
  */
    @DisplayName("Test pour supprimer voiture / ok")
    @Test
    void supprimerVoitureOk() {
        long id = 1;
        when(daoMock.existsById(id)).thenReturn(true);
        service.supprimer(id);
        verify(daoMock).deleteById(id);
    }

    @DisplayName("Test pour supprimer  voiture/ Nok_id non trouvé")
    @Test
    void supprimerVoitureNotOkid() {
        long id = 56;
        when(daoMock.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.supprimer(id));

    }

    /*
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
 /\_/\       __  __        _    _                 _                           /\_/\
( o.o )     |  \/  |  ___ | |_ | |__    ___    __| |  ___                    ( o.o )
 > ^ <      | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \                    > ^ <
 /\_/\      | |  | ||  __/| |_ | | | || (_) || (_| ||  __/                    /\_/\
( o.o )     |_|  |_| \___| \___|_| |_| \___/  \__,_| \___|                   ( o.o )
 > ^ <       _ __  ___   ___ | |__    ___  _ __  ___ | |__    ___  _ __       > ^ <
 /\_/\      | '__|/ _ \ / __|| '_ \  / _ \| '__|/ __|| '_ \  / _ \| '__|      /\_/\
( o.o )     | |  |  __/| (__ | | | ||  __/| |  | (__ | | | ||  __/| |        ( o.o )
 > ^ <      |_|   \___| \___||_| |_| \___||_|   \___||_| |_| \___||_|         > ^ <
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <

     */
    @DisplayName("Test rechercher voitures")
    @Test
    void testRechercherVoitures() {
        // Préparer les données de test
        Voiture voiture1 = creerVoiture();
        Voiture voiture2 = creerVoiture2();
        List<Voiture> voitures = Arrays.asList(voiture1, voiture2);

        VoitureResponseDto voitureResponseDto1 = creerVoitureResponseDto();
        VoitureResponseDto voitureResponseDto2 = creerVoiture2ResponseDto();

        // Simuler les appels de méthodes
        when(daoMock.findAll()).thenReturn(voitures);
        when(mapperMock.toVoitureResponseDto(voiture1)).thenReturn(voitureResponseDto1);
        when(mapperMock.toVoitureResponseDto(voiture2)).thenReturn(voitureResponseDto2);

        // Appeler la méthode à tester
        List<VoitureResponseDto> result = service.rechercher(null, "Toyota", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).marque());
        assertEquals("Toyota", result.get(1).marque());

        // Vérifier que les méthodes simulées ont été appelées
        verify(daoMock).findAll();
        verify(mapperMock).toVoitureResponseDto(voiture1);
        verify(mapperMock).toVoitureResponseDto(voiture2);
    }



/*
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
 /\_/\                                                          /\_/\
( o.o )      __  __        _    _                 _            ( o.o )
 > ^ <      |  \/  |  ___ | |_ | |__    ___    __| |  ___       > ^ <
 /\_/\      | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \      /\_/\
( o.o )     | |  | ||  __/| |_ | | | || (_) || (_| ||  __/     ( o.o )
 > ^ <      |_|  |_| \___|_\__||_| |___\___/  \__,_| \___|      > ^ <
 /\_/\       _ __   _ __ (_)__   __ /_/   ___  ___              /\_/\
( o.o )     | '_ \ | '__|| |\ \ / // _ \ / _ \/ __|            ( o.o )
 > ^ <      | |_) || |   | | \ V /|  __/|  __/\__ \             > ^ <
 /\_/\      | .__/ |_|   |_|  \_/  \___| \___||___/             /\_/\
( o.o )     |_|                                                ( o.o )
 > ^ <                                                          > ^ <
 /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\  /\_/\
( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )( o.o )
 > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <  > ^ <
 */

    private static Voiture creerVoiture() {
        Voiture v = new Voiture();
        v.setMarque("Toyota");
        v.setModele("Aygo");
        v.setCouleur("Gris");
        v.setNombreDePlaces(5);
        v.setCarburant(Carburant.ESSENCE);
        v.setNombreDePortes(3);
        v.setTransmission("auto");
        v.setClim(true);
        v.setNombreDeBagages(5);
        v.setType("Voiture de luxe");
        v.setTarifJournalier(1000);
        v.setKilometrage(100000);
        v.setActif(true);
        v.setRetireDuParc(false);
        return v;
    }

    private static Voiture creerVoiture2() {
        Voiture v = new Voiture();
        v.setMarque("Toyota");
        v.setModele("Aygo");
        v.setCouleur("Or");
        v.setNombreDePlaces(5);
        v.setCarburant(Carburant.DIESEL);
        v.setNombreDePortes(5);
        v.setTransmission("auto");
        v.setClim(true);
        v.setNombreDeBagages(5);
        v.setType("Voiture de luxe");
        v.setTarifJournalier(1000);
        v.setKilometrage(100000);
        v.setActif(true);
        v.setRetireDuParc(false);
        return v;
    }

    private static VoitureResponseDto creerVoitureResponseDto() {
        return new VoitureResponseDto(
                1L,
                "Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Permis.B,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
    }

    private static VoitureResponseDto creerVoiture2ResponseDto() {
        return new VoitureResponseDto(
                2L,
                "Toyota", "Aygo", "Or", "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Permis.D1,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
    }

    private static VoitureRequestDto getVoitureRequestDto() {
        return new VoitureRequestDto(
                "Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
    }
}



