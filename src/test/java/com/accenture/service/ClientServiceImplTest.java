package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.model.param.Permis;
import com.accenture.repository.ClientDao;
import com.accenture.repository.entity.Adresse;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.AdresseDto;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ClientServiceImplTest {

@Mock
    ClientDao daoMock= Mockito.mock(ClientDao.class);
@Mock
ClientMapper mappermock;
@Mock
    PasswordEncoder passwordEncoder;
@InjectMocks
ClientServiceImpl service;

/*
 ****************************************************
 *   __  __        _    _                 _         *
 *  |  \/  |  ___ | |_ | |__    ___    __| |  ___   *
 *  | |\/| | / _ \| __|| '_ \  / _ \  / _` | / _ \  *
 *  | |  | ||  __/| |_ | | | || (_) || (_| ||  __/  *
 *  |_|  |_| \___| \__||_| |_| \___/  \__,_| \___|  *
 *  | |_  _ __  ___   _   _ __   __ ___  _ __       *
 *  | __|| '__|/ _ \ | | | |\ \ / // _ \| '__|      *
 *  | |_ | |  | (_) || |_| | \ V /|  __/| |         *
 *   \__||_|   \___/  \__,_|  \_/  \___||_|         *
 ****************************************************

 */
@DisplayName("Test de la méthode trouver(String mail) qui doit renvoyer une exception lorsque le client n'existe pas")
@Test
void testTrouverExistePas(){
    when(daoMock.findById("testnul@test.com")).thenReturn(Optional.empty());
    EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver("testnul@test.com"));
    assertEquals("Erreur, aucun client trouvé avec cette adresse mail", ex.getMessage());
}

@DisplayName("Test de la méthode trouver (String mail) qui doit renvoyer un client reesopnseDto lorsque le client existe ")
@Test
void testTrouverExiste(){
    Client c = creerClient();
    Optional<Client> optionalClient = Optional.of(c);
    when(daoMock.findById("test@test.com")).thenReturn(optionalClient);

    ClientResponseDto dto = creerClientResponseDtoClient();
    when(mappermock.toClientResponseDto(c)).thenReturn(dto);

    assertSame(dto, service.trouver("test@test.com"));
}

@DisplayName("Test de la méthode trouverTous qui doit renvoyer une liste de clientResponseDto corresponsant aux clients existants en base")
    @Test
void testTrouverToutes(){
    Client client1 = creerClient();
    Client client2 = creerClient2();

    List<Client> clients = List.of(creerClient(), creerClient2());
    List<ClientResponseDto> dtos = List.of(creerClientResponseDtoClient(), creerClientResponseDtoClient2());

    when(daoMock.findAll()).thenReturn(clients);
    when(mappermock.toClientResponseDto(client1)).thenReturn(creerClientResponseDtoClient());
    when(mappermock.toClientResponseDto(client2)).thenReturn(creerClientResponseDtoClient2());

    assertEquals(dtos, service.trouverTous());
}
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

@DisplayName("Si ajouter clientRequestDto (mail null), exception levée")
    @Test
    void testAjouterSiMailNull() {
    ClientRequestDto dto = new ClientRequestDto(null, "Test1234!", "nom", "prenom",new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12),  List.of());
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (mail blank), exception levée")
    @Test
    void testAjouterSiMailBlank() {
    ClientRequestDto dto = new ClientRequestDto("   \t ", "Test1234!", "nom", "prenom",new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));}

@DisplayName("Si ajouter clientRequestDto (password inadapté), exception levée")
    @Test
    void testAjouterSiPasswordInadapte() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "pass", "nom", "prenom",new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (password blank), exception levée")
    @Test
    void testAjouterSiPasswordBlank() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "   \t ", "nom", "prenom",new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (nom null), exception levée")
    @Test
    void testAjouterSiNomNull() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", null, "prenom",new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12),List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (nom blank), exception levée")
    @Test
    void testAjouterSiNomBlank() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "   \t ", "prenom",new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (prenom null), exception levée")
    @Test
    void testAjouterSiPrenomNull() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", null,new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (prenom blank), exception levée")
    @Test
    void testAjouterSiPrenomBlank() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "   \t ",new AdresseDto("ahjkjg", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of()  );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (Adresse.rue null), exception levée")
    @Test
    void testAjouterSiAdresse_RueNull() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom",new AdresseDto(null, "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (Adresse.rue blank), exception levée")
    @Test
    void testAjouterSiAdresse_RueBlank() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom",new AdresseDto("    \t ", "12345","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}


    @Test
    void testAjouterSiAdresse_CodePostalNull() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom",new AdresseDto("rue", null,"Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

@DisplayName("Si ajouter clientRequestDto (Adresse.CP blank), exception levée")
    @Test
    void testAjouterSiAdresse_CodePostalBlank() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom",new AdresseDto("rue", "    \t ","Gdtryfguh"), LocalDate.of(1999, 12, 12), List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

    @Test
    void testAjouterSiAdresse_VilleNull() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom",new AdresseDto("rue", "12345",null), LocalDate.of(1999, 12, 12),List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}


    @Test
    void testAjouterSiAdresse_VilleBlank() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom",new AdresseDto("rue", "12345","    \t "), LocalDate.of(1999, 12, 12),  List.of() );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));}

    @Test
    void testAjouterSiDateNaissance_JourNull() {
    ClientRequestDto dto = new ClientRequestDto("adresse@mail.com", "Test1234!", "nom", "prenom",new AdresseDto("rue", "12345","ville"), null , List.of()  );
    assertThrows(UtilisateurException.class, ()->service.ajouter(dto));
}

    @Test
    void testAjouterOk(){
        Client clientAvantEnreg = creerClient();
        clientAvantEnreg.setMail("test@test.com");
        ClientRequestDto requestdto = new ClientRequestDto("adresse@mail.com", "Test1234&", "nom", "prenom",new AdresseDto("rue", "12345","ville"), LocalDate.of(1999, 12, 12) , List.of() );

        Client clientApresEnreg = creerClient();
        ClientResponseDto responseDto= creerClientResponseDtoClient();

        when(mappermock.toClient(requestdto)).thenReturn(clientAvantEnreg);
        when(daoMock.save(clientAvantEnreg)).thenReturn(clientApresEnreg);
        when(mappermock.toClientResponseDto(clientApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestdto));
        verify(daoMock, Mockito.times(1)).save(clientAvantEnreg);
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
@DisplayName("Test pour supprimer un client / ok")
@Test
void supprimerClientOk() {
    String mail = "test@test.com";
    when(daoMock.existsById(mail)).thenReturn(true);
    service.supprimer(mail);
    verify(daoMock).deleteById(mail);

}

@DisplayName("Test pour supprimer un client / Nok_mail non trouvé")
@Test
void supprimerClientNotOkMail() {
    String mail = "test@test.com";
    when(daoMock.existsById(mail)).thenReturn(false);
    assertThrows(EntityNotFoundException.class, ()-> service.supprimer(mail) );

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
    @DisplayName("Test modifier partiellement un client / ok")
    @Test
    void testModifierPartiellementOk() throws UtilisateurException, EntityNotFoundException {
        // Préparer les données de test
        String mail = "test@test.com";
        Client clientExistant = creerClient();
        ClientRequestDto clientRequestDto = new ClientRequestDto("adresse@mail.com", "Test1234&", "nom", "prenom",new AdresseDto("rue", "12345","ville"), LocalDate.of(1999, 12, 12) , List.of() );
        Client nouveauClient = creerClient2();
        ClientResponseDto clientResponseDto = creerClientResponseDtoClient2();

        // Simuler les appels de méthodes
        when(daoMock.findById(mail)).thenReturn(Optional.of(clientExistant));
        when(mappermock.toClient(clientRequestDto)).thenReturn(nouveauClient);
        when(daoMock.save(clientExistant)).thenReturn(clientExistant);
        when(mappermock.toClientResponseDto(clientExistant)).thenReturn(clientResponseDto);

        // Appeler la méthode à tester
        ClientResponseDto result = service.modifierPartiellement(mail, clientRequestDto);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(clientResponseDto, result);

        // Vérifier que les méthodes simulées ont été appelées
        verify(daoMock).findById(mail);
        verify(mappermock).toClient(clientRequestDto);
        verify(daoMock).save(clientExistant);
        verify(mappermock).toClientResponseDto(clientExistant);
    }

    @DisplayName("Test modifier partiellement un client / client non trouvé")
    @Test
    void testModifierPartiellementClientNonTrouve() {
        // Préparer les données de test
        String mail = "test@test.com";
        ClientRequestDto clientRequestDto = new ClientRequestDto("adresse@mail.com", "Test1234&", "nom", "prenom",new AdresseDto("rue", "12345","ville"), LocalDate.of(1999, 12, 12) , List.of() );

        // Simuler les appels de méthodes
        when(daoMock.findById(mail)).thenReturn(Optional.empty());

        // Vérifier que l'exception est levée
        assertThrows(UtilisateurException.class, () -> service.modifierPartiellement(mail, clientRequestDto));

        // Vérifier que les méthodes simulées ont été appelées
        verify(daoMock).findById(mail);
        verify(mappermock, never()).toClient(any());
        verify(daoMock, never()).save(any());
        verify(mappermock, never()).toClientResponseDto(any());
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

    @DisplayName("Test rechercher clients")
    @Test
    void testRechercherClients() {
        // Préparer les données de test
        Client client1 = creerClient();
        Client client2 = creerClient2();
        List<Client> clients = Arrays.asList(client1, client2);

        ClientResponseDto clientResponseDto1 = creerClientResponseDtoClient();
        ClientResponseDto clientResponseDto2 = creerClientResponseDtoClient2();

        // Simuler les appels de méthodes
        when(daoMock.findAll()).thenReturn(clients);
        when(mappermock.toClientResponseDto(client1)).thenReturn(clientResponseDto1);
        when(
                mappermock.toClientResponseDto(client2)).thenReturn(clientResponseDto2);

        // Appeler la méthode à tester
        List<ClientResponseDto> result = service.rechercher(null, "test", null, null, null, null, null, null, null, null);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test", result.get(0).prenom());
        assertEquals("test", result.get(1).prenom());

        // Vérifier que les méthodes simulées ont été appelées
        verify(daoMock).findAll();
        verify(mappermock).toClientResponseDto(client1);
        verify(mappermock).toClientResponseDto(client2);
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
private static Client creerClient(){
    Client c = new Client();
    c.setMail("test@test.com");
    c.setPassword("Test1234!");
    c.setNom("test");
    c.setPrenom("test");
    c.setDateNaissance(LocalDate.of(1999, 4, 1));
    c.setAdresse(new Adresse("rue", "12345", "Ville"));
    c.setDateInscription(LocalDate.now());
    c.setDesactive(false);
    c.setListePermis(List.of(Permis.B));
    return c;
    }

    private static Client creerClient2(){
    Client c2 = new Client();
    c2.setMail("test2@test.com");
    c2.setPassword("Test1234!");
    c2.setNom("test");
    c2.setPrenom("test");
    c2.setDateNaissance(LocalDate.of(1999, 4, 30));
    c2.setAdresse(new Adresse("rue", "12345", "Ville"));
    c2.setDateInscription(LocalDate.now());
    c2.setDesactive(false);
    c2.setListePermis(List.of(Permis.A2));
    return c2;
    }

    private static ClientResponseDto creerClientResponseDtoClient(){
    return new ClientResponseDto(
            "test@test.com",
            "test",
            "test",
            "test",
            new Adresse("rue", "12345", "Ville"),
            LocalDate.of(1999, 4, 1),
            LocalDate.now(),
            List.of(Permis.B),
            false);
    }
    private static ClientResponseDto creerClientResponseDtoClient2(){
    return new ClientResponseDto(
            "test2@test.com",
            "test",
            "test",
            "test",
            new Adresse("rue", "12345", "Ville"),
            LocalDate.of(1999, 4, 30),
            LocalDate.now(),
            List.of(Permis.A2),
            false);
    }
}