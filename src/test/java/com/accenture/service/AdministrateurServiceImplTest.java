package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.repository.AdministrateurDao;
import com.accenture.repository.entity.Administrateur;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

/*
 ****************************************************
 *   __  __        _    _                 _         *
 *  |  \/  |  ___ | |_ | |__    ___    __| |  ___   *
 *  | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \  *
 *  | |  | ||  __/| |_ | | | || (_) || (_| ||  __/  *
 *  |_|  |_| ____| \__||_| |_|_\___/  \__,_| \___|  *
 *    __ _  (_)  ___   _   _ | |_  ___  _ __        *
 *   / _` | | | / _ \ | | | || __|/ _ \| '__|       *
 *  | (_| | | || (_) || |_| || |_|  __/| |          *
 *   \__,_|_/ | \___/  \__,_| \__|\___||_|          *
 *        |__/                                      *
 ****************************************************
 */
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

        when(mapperMock.toAdministrateur(requestDto)).thenReturn(adminAvantEnreg);
        when(daoMock.save(adminAvantEnreg)).thenReturn(adminApresEnreg);
        when(mapperMock.toAdministrateurResponseDto(adminApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestDto));
        verify(daoMock, Mockito.times(1)).save(adminAvantEnreg);
    }

    /*
     ****************************************************************
     *   __  __        _    _                 _                     *
     *  |  \/  |  ___ | |_ | |__    ___    __| |  ___               *
     *  | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \              *
     *  | |  | ||  __/| |_ | | | || (_) |_ (_| ||  __/              *
     *  |___ |_| \____ ___||_|___| ____/(_)__,__ ____|  ___  _ __   *
     *  / __|| | | || '_ \ | '_ \ | '__|| || '_ ` _ \  / _ \| '__|  *
     *  \__ \| |_| || |_) || |_) || |   | || | | | | ||  __/| |     *
     *  |___/ \__,_|| .__/ | .__/ |_|   |_||_| |_| |_| \___||_|     *
     *              |_|    |_|                                      *
     ****************************************************************
     */

@DisplayName("Test pour supprimer un admin / ok")
    @Test
    void supprimerAdminOk() {
    String mail = "test@test.com";
    when(daoMock.existsById(mail)).thenReturn(true);
    when(daoMock.count()).thenReturn(14L);
    service.supprimer(mail);
    verify(daoMock).deleteById(mail);
    verify(daoMock).count();
}

    @DisplayName("Test pour supprimer un admin / Nok_mail non trouvé")
    @Test
    void supprimerAdminNotOkMail() {
        String mail = "test@test.com";
        when(daoMock.existsById(mail)).thenReturn(false);
       assertThrows(EntityNotFoundException.class, ()-> service.supprimer(mail) );

    }
    @DisplayName("Test pour supprimer un admin / Nok_dernier admin")
    @Test
    void supprimerAdminNotOkDernier() {
        String mail = "test@test.com";
        when(daoMock.existsById(mail)).thenReturn(true);
        when(daoMock.count()).thenReturn(1L);
        assertThrows(IllegalStateException.class, () -> service.supprimer(mail));

    }
/*
 ******************************************************************
 *   __  __        _    _                 _                       *
 *  |  \/  |  ___ | |_ | |__    ___    __| |  ___                 *
 *  | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \                *
 *  | |  | ||  __/| |_ | | | || (_) || (_| ||  __/                *
 *  |_|  |_| \___| \___|_| |_| \___/  \__,_| \___|                *
 *   _ __  ___   ___ | |__    ___  _ __  ___ | |__    ___  _ __   *
 *  | '__|/ _ \ / __|| '_ \  / _ \| '__|/ __|| '_ \  / _ \| '__|  *
 *  | |  |  __/| (__ | | | ||  __/| |  | (__ | | | ||  __/| |     *
 *  |_|   \___| \___||_| |_| \___||_|   \___||_| |_| \___||_|     *
 ******************************************************************
 */
@DisplayName("Test rechercher administrateurs")
@Test
void testRechercherAdministrateurs() {
    // Préparer les données de test
    Administrateur admin1 = creerAdmin();
    Administrateur admin2 = creerAdmin2();
    List<Administrateur> administrateurs = Arrays.asList(admin1, admin2);

    AdministrateurResponseDto adminResponseDto1 = creerAdminResponseDto();
    AdministrateurResponseDto adminResponseDto2 = creerAdminResponseDto2();

    // Simuler les appels de méthodes
    when(daoMock.findAll()).thenReturn(administrateurs);
    when(mapperMock.toAdministrateurResponseDto(admin1)).thenReturn(adminResponseDto1);
    when(mapperMock.toAdministrateurResponseDto(admin2)).thenReturn(adminResponseDto2);

    // Appeler la méthode à tester
    List<AdministrateurResponseDto> result = service.rechercher(null, "test", null, null);

    // Vérifier les résultats
    assertNotNull(result);
    assertEquals(2, result.size(), "La taille de la liste résultante ne correspond pas à la taille attendue.");
    assertEquals("test", result.get(0).prenom());
    assertEquals("test2", result.get(1).prenom());

    // Vérifier que les méthodes simulées ont été appelées
    verify(daoMock).findAll();
    verify(mapperMock).toAdministrateurResponseDto(admin1);
    verify(mapperMock).toAdministrateurResponseDto(admin2);
}
/*
 ******************************************************
 *   __  __        _    _                 _           *
 *  |  \/  |  ___ | |_ | |__    ___    __| |  ___     *
 *  | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \    *
 *  | |  | ||  __/| |_ | | | || (_) || (_| ||  __/    *
 *  |_|  |_| \___| \__||_| __|_\____  ___,_| \___|    *
 *   _ __ ___    ___    __| |(_) / _|(_)  ___  _ __   *
 *  | '_ ` _ \  / _ \  / _` || || |_ | | / _ \| '__|  *
 *  | | | | | || (_) || (_| || ||  _|| ||  __/| |     *
 *  |_| |_| |_| \___/  \__,_||_||_|  |_| \___||_|     *
 ******************************************************
 */

    @DisplayName("Test modifier partiellement un administrateur / ok")
    @Test
    void testModifierPartiellementOk() throws UtilisateurException, EntityNotFoundException {
        // Préparer les données de test
        String mail = "test@test.com";
        Administrateur adminExistant = creerAdmin();
        AdministrateurRequestDto adminRequestDto = new AdministrateurRequestDto("test@test.com", "Test1234!", "test", "test", "ADMIN");
        Administrateur nouveauAdmin = creerAdmin2();
        AdministrateurResponseDto adminResponseDto = creerAdminResponseDto2();

        // Simuler les appels de méthodes
        when(daoMock.findById(mail)).thenReturn(Optional.of(adminExistant));
        when(mapperMock.toAdministrateur(adminRequestDto)).thenReturn(nouveauAdmin);
        when(daoMock.save(adminExistant)).thenReturn(adminExistant);
        when(mapperMock.toAdministrateurResponseDto(adminExistant)).thenReturn(adminResponseDto);

        // Appeler la méthode à tester
        AdministrateurResponseDto result = service.modifierPartiellement(mail, adminRequestDto);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(adminResponseDto, result);

        // Vérifier que les méthodes simulées ont été appelées
        verify(daoMock).findById(mail);
        verify(mapperMock).toAdministrateur(adminRequestDto);
        verify(daoMock).save(adminExistant);
        verify(mapperMock).toAdministrateurResponseDto(adminExistant);
    }

    @DisplayName("Test modifier partiellement un administrateur / administrateur non trouvé")
    @Test
    void testModifierPartiellementAdminNonTrouve() {
        // Préparer les données de test
        String mail = "test@test.com";
        AdministrateurRequestDto adminRequestDto = new AdministrateurRequestDto("test@test.com", "Test1234!", "test", "test", "ADMIN");

        // Simuler les appels de méthodes
        when(daoMock.findById(mail)).thenReturn(Optional.empty());

        // Vérifier que l'exception est levée
        assertThrows(UtilisateurException.class, () -> service.modifierPartiellement(mail, adminRequestDto));

        // Vérifier que les méthodes simulées ont été appelées
        verify(daoMock).findById(mail);
        verify(mapperMock, never()).toAdministrateur(any());
        verify(daoMock, never()).save(any());
        verify(mapperMock, never()).toAdministrateurResponseDto(any());
    }


/*
 ****************************************************
 *   __  __        _    _                 _         *
 *  |  \/  |  ___ | |_ | |__    ___    __| |  ___   *
 *  | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \  *
 *  | |  | ||  __/| |_ | | | || (_) || (_| ||  __/  *
 *  |_|  |_| \___|_\__||_| |___\___/  \__,_| \___|  *
 *   _ __   _ __ (_)__   __ /_/   ___  ___          *
 *  | '_ \ | '__|| |\ \ / // _ \ / _ \/ __|         *
 *  | |_) || |   | | \ V /|  __/|  __/\__ \         *
 *  | .__/ |_|   |_|  \_/  \___| \___||___/         *
 *  |_|                                             *
 ****************************************************
 */

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