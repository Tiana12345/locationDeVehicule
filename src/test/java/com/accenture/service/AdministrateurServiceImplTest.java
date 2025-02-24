package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.model.Permis;
import com.accenture.repository.AdministrateurDao;
import com.accenture.repository.entity.Administrateur;
import com.accenture.repository.entity.Adresse;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.*;
import com.accenture.service.mapper.AdministrateurMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AdministrateurServiceImplTest {

    @Mock
    AdministrateurDao daoMock = Mockito.mock(AdministrateurDao.class);
    @Mock
    AdministrateurMapper mapperMock;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    AdministrateurServiceImpl service;


    @DisplayName("Si ajouter (null) exception levée")
    @Test
    void testAjouterSiNull() { assertThrows(UtilisateurException.class, ()-> service.ajouter(null));
    }

    @DisplayName("Si ajouter AdministrateurRequestDto (mail null), exception levée")
    @Test
    void testAjouterSiMailNull() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto(null, "Test1234!", "nom", "prenom", "ADMIN");
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }

    @DisplayName("Si ajouter AdministrateurRequestDto (mail blank), exception levée")
    @Test
    void testAjouterSiMailBlank() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("   \t ", "Test1234!", "nom", "prenom", "ADMIN");
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));}

    @DisplayName("Si ajouter AdministrateurRequestDto (password null), exception levée")
    @Test
    void testAjouterSiPasswordNull() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", null, "nom", "prenom", "ADMIN");
    }

    @DisplayName("Si ajouter AdministrateurRequestDto (password blank), exception levée")
    @Test
    void testAjouterSiPasswordBlank() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", "   \t ", "nom", "prenom", "ADMIN") ;
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }

    @DisplayName("Si ajouter AdministrateurRequestDto (nom null), exception levée")
    @Test
    void testAjouterSiNomNull() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", "Test1234!", null, "prenom", "ADMIN");
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }

    @DisplayName("Si ajouter AdministrateurRequestDto (nom blank), exception levée")
    @Test
    void testAjouterSiNomBlank() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", "Test1234!", "   \t ", "prenom", "ADMIN");
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }

    @DisplayName("Si ajouter AdministrateurRequestDto (prenom null), exception levée")
    @Test
    void testAjouterSiPrenomNull() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", "Test1234!", "nom", null, "ADMIN");
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }

    @DisplayName("Si ajouter AdministrateurRequestDto (prenom blank), exception levée")
    @Test
    void testAjouterSiPrenomBlank() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", "Test1234!", "nom", "   \t ", "ADMIN");
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }
    @DisplayName("Si ajouter AdministrateurRequestDto (fonction null), exception levée")
    @Test
    void testAjouterSiFonctionNull() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom", null);
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }
        @DisplayName("Si ajouter AdministrateurRequestDto (fonction blank), exception levée")
    @Test
    void testAjouterSiFonctionBlank() {
        AdministrateurRequestDto dto = new AdministrateurRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom", "   \t ");
        assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
    }

    @Test
    void testAjouterOk(){
        Administrateur adminAvantEnreg = creerAdmin();
        adminAvantEnreg.setMail("test@test.com");
        AdministrateurRequestDto requestDto = new AdministrateurRequestDto( "test@test.com",
                "Test1234&",
                "test",
                "test",
                "ADMIN");

        Administrateur adminApresEnreg = creerAdmin();
        AdministrateurResponseDto responseDto=creerAdminResponseDto();

        Mockito.when(mapperMock.toAdministrateur(requestDto)).thenReturn(adminAvantEnreg);
        Mockito.when(daoMock.save(adminAvantEnreg)).thenReturn(adminApresEnreg);
        Mockito.when(mapperMock.toAdministrateurResponseDto(adminApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestDto));
        Mockito.verify(daoMock, Mockito.times(1)).save(adminAvantEnreg);
    }


@DisplayName("Test pour supprimer un admin / ok")
    @Test
    void supprimerAdminOk() {
    String mail = "test@test.com";
    Mockito.when(daoMock.existsById(mail)).thenReturn(true);
    Mockito.when(daoMock.count()).thenReturn(14L);
    service.supprimer(mail);

}

    @DisplayName("Test pour supprimer un admin / Nok_mail non trouvé")
    @Test
    void supprimerAdminNotOkMail() {
        String mail = "test@test.com";
        Mockito.when(daoMock.existsById(mail)).thenReturn(false);
       assertThrows(EntityNotFoundException.class, ()-> service.supprimer(mail) );

    }
    @DisplayName("Test pour supprimer un admin / Nok_dernier admin")
    @Test
    void supprimerAdminNotOkDernier() {
        String mail = "test@test.com";
        Mockito.when(daoMock.existsById(mail)).thenReturn(true);
        Mockito.when(daoMock.count()).thenReturn(1L);
        assertThrows(IllegalStateException.class, () -> service.supprimer(mail));

    }


    private static Administrateur creerAdmin(){
        Administrateur a1 = new Administrateur();
        a1.setMail("test@test.com");
        a1.setPassword("Test1234!");
        a1.setNom("test");
        a1.setPrenom("test");
        a1.setFonction("ADMIN");
        return a1;
    }

    private static Administrateur creerAdmin2(){
        Administrateur a2 = new Administrateur();
        a2.setMail("test2@test.com");
        a2.setPassword("Test1234!");
        a2.setNom("test2");
        a2.setPrenom("test2");
        a2.setFonction("ADMIN2");
        return a2;
    }

    private static AdministrateurResponseDto creerAdminResponseDto(){
        return new AdministrateurResponseDto(
                "test@test.com",
                "Test1234!",
                "test",
                "test",
                "ADMIN"
        );
    }  private static AdministrateurResponseDto creerAdminResponseDto2(){
        return new AdministrateurResponseDto(
                "test2@test.com",
                "Test1234!",
                "test2",
                "test2",
                "ADMIN2"
        );
    }
}