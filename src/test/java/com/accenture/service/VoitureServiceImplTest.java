package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.AdresseDto;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class VoitureServiceImplTest {
    @Mock
    VoitureDao daoMock = Mockito.mock(VoitureDao.class);
    @Mock
    VoitureMapper mapperMock;
    @InjectMocks
    VoitureServiceImpl service;


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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }

    @Test
    void testAjouterSiListePermisNull() {
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
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        assertThrows(VehiculeException.class, () -> service.ajouter(dto));
    }


    @Test
    void testAjouterSiListePermisBlank() {
        VoitureRequestDto dto = new VoitureRequestDto("Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                List.of(),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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
                List.of(Permis.A2),
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

        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(voitureAvantEnreg);
        Mockito.when(daoMock.save(voitureAvantEnreg)).thenReturn(voitureApresEnreg);
        Mockito.when(mapperMock.toVoitureResponseDto(voitureApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestDto));
        Mockito.verify(daoMock, Mockito.times(1)).save(voitureAvantEnreg);
    }


    @Test
    void testTrouverExistePas() {
        Mockito.when(daoMock.findById(56L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver(56L));
        assertEquals("Erreur, aucune voiture trouvée avec cet id", ex.getMessage());

    }

    @Test
    void testTrouverExiste() {
        Voiture v = creerVoiture();
        Optional<Voiture> optionalVoiture = Optional.of(v);
        Mockito.when(daoMock.findById(1L)).thenReturn(optionalVoiture);

        VoitureResponseDto dto = creerVoitureResponseDto();
        Mockito.when(mapperMock.toVoitureResponseDto(v)).thenReturn(dto);

        assertSame(dto, service.trouver(1L));
    }

    @Test
    void testTrouverToutes() {
        Voiture voiture1 = creerVoiture();
        Voiture voiture2 = creerVoiture2();

        List<Voiture> voitures = List.of(creerVoiture(), creerVoiture2());
        List<VoitureResponseDto> dtos = List.of(creerVoitureResponseDto(), creerVoiture2ResponseDto());
        VoitureResponseDto voitureResponseDto = creerVoitureResponseDto();
        VoitureResponseDto voitureResponseDto2 = creerVoiture2ResponseDto();

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        Mockito.when(mapperMock.toVoitureResponseDto(voiture1)).thenReturn(creerVoitureResponseDto());
        Mockito.when(mapperMock.toVoitureResponseDto(voiture2)).thenReturn(creerVoiture2ResponseDto());

        assertEquals(dtos, service.trouverToutes());
    }


    @Test
    void modifierPartiellementOk() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = getVoitureRequestDto();
        assertNotNull(requestDto, "Le VoitureRequestDto ne doit pas être null");

        Voiture voitureExistante = creerVoiture();
        Voiture voitureModifiee = creerVoiture2();
        VoitureResponseDto responseDto = creerVoitureResponseDto();

        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.of(voitureExistante));
        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(voitureModifiee);
        Mockito.when(daoMock.save(voitureExistante)).thenReturn(voitureExistante);
        Mockito.when(mapperMock.toVoitureResponseDto(voitureExistante)).thenReturn(responseDto);

        Mockito.when(mapperMock.toVoitureRequestDto(voitureExistante)).thenReturn(requestDto);
        VoitureResponseDto result = service.modifierPartiellement(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        Mockito.verify(daoMock).findById(1L);
        Mockito.verify(mapperMock).toVoiture(requestDto);
        Mockito.verify(daoMock).save(voitureExistante);
        Mockito.verify(mapperMock).toVoitureResponseDto(voitureExistante);
    }

    @Test
    void modifierPartiellementNok() throws VehiculeException, EntityNotFoundException {
        VoitureRequestDto requestDto = getVoitureRequestDto();

        Mockito.when(daoMock.findById(1L)).thenReturn(Optional.empty());

        VehiculeException exception = assertThrows(VehiculeException.class, () -> {
            service.modifierPartiellement(1L, requestDto);
        });

        assertEquals("Erreur, l'identifiant ne correspond à aucune voiture en base", exception.getMessage());

        Mockito.verify(daoMock).findById(1L);
        Mockito.verify(daoMock, never()).save(any(Voiture.class));
        Mockito.verify(mapperMock, never()).toVoiture(any(VoitureRequestDto.class));
        Mockito.verify(mapperMock, never()).toVoitureResponseDto(any(Voiture.class));
    }

    @Test
    void getVoitureResponseDto() {
    }

    @DisplayName("Test pour supprimer voiture / ok")
    @Test
    void supprimerVoitureOk() {
        long id = 1;
        Mockito.when(daoMock.existsById(id)).thenReturn(true);
        service.supprimer(id);

    }

    @DisplayName("Test pour supprimer  voiture/ Nok_id non trouvé")
    @Test
    void supprimerVoitureNotOkid() {
        long id = 56;
        Mockito.when(daoMock.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, ()-> service.supprimer(id) );

    }
    @Test
    void rechercher() {
    }

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
        v.setListePermis(List.of(Permis.B));
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
        v.setListePermis(List.of(Permis.B));
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
                List.of(Permis.B),
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
                List.of(Permis.B),
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
    }

    private static VoitureRequestDto getVoitureRequestDto() {
        VoitureRequestDto requestDto = new VoitureRequestDto(
                "Toyota",
                "Aygo",
                "Gris",
                "Voiture de luxe",
                5,
                5,
                "auto",
                true,
                3,
                List.of(Permis.A2),
                Carburant.ESSENCE,
                1000,
                100000,
                false,
                false);
        return requestDto;
    }

}


