package com.accenture.service;

import com.accenture.model.Carburant;
import com.accenture.model.Permis;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Vehicule;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.VoitureResponseDto;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VoitureServiceImplTest {
    @Mock
    VoitureDao daoMock = Mockito.mock(VoitureDao.class);
    @Mock
    VoitureMapper mapperMock;
    @InjectMocks
    VoitureServiceImpl service;

    @Test
    void ajouter() {
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
    void trouverToutes() {
    }

    @Test
    void modifier() {
    }

    @Test
    void modifierPartiellement() {
    }

    @Test
    void getVoitureResponseDto() {
    }

    @Test
    void supprimer() {
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
}
