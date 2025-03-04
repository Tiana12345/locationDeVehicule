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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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


@DisplayName("Test de la méthode trouver(String mail) qui doit renvoyer une exception lorsque le client n'existe pas")
@Test
void testTrouverExistePas(){
    Mockito.when(daoMock.findById("testnul@test.com")).thenReturn(Optional.empty());
    EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver("testnul@test.com"));
    assertEquals("Erreur, aucun client trouvé avec cette adresse mail", ex.getMessage());
}

@DisplayName("Test de la méthode trouver (String mail) qui doit renvoyer un client reesopnseDto lorsque le client existe ")
@Test
void testTrouverExiste(){
    Client c = creerClient();
    Optional<Client> optionalClient = Optional.of(c);
    Mockito.when(daoMock.findById("test@test.com")).thenReturn(optionalClient);

    ClientResponseDto dto = creerClientResponseDtoClient();
    Mockito.when(mappermock.toClientResponseDto(c)).thenReturn(dto);

    assertSame(dto, service.trouver("test@test.com"));
}

@DisplayName("Test de la méthode trouverTous qui doit renvoyer une liste de clientResponseDto corresponsant aux clients existants en base")
    @Test
void testTrouverToutes(){
    Client client1 = creerClient();
    Client client2 = creerClient2();

    List<Client> clients = List.of(creerClient(), creerClient2());
    List<ClientResponseDto> dtos = List.of(creerClientResponseDtoClient(), creerClientResponseDtoClient2());
    ClientResponseDto clientResponseDto = creerClientResponseDtoClient();
    ClientResponseDto clientResponseDto2 = creerClientResponseDtoClient2();

    Mockito.when(daoMock.findAll()).thenReturn(clients);
    Mockito.when(mappermock.toClientResponseDto(client1)).thenReturn(creerClientResponseDtoClient());
    Mockito.when(mappermock.toClientResponseDto(client2)).thenReturn(creerClientResponseDtoClient2());

    assertEquals(dtos, service.trouverTous());
}

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

        Mockito.when(mappermock.toClient(requestdto)).thenReturn(clientAvantEnreg);
        Mockito.when(daoMock.save(clientAvantEnreg)).thenReturn(clientApresEnreg);
        Mockito.when(mappermock.toClientResponseDto(clientApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestdto));
        Mockito.verify(daoMock, Mockito.times(1)).save(clientAvantEnreg);
    }

@DisplayName("Test pour supprimer un client / ok")
@Test
void supprimerClientOk() {
    String mail = "test@test.com";
    Mockito.when(daoMock.existsById(mail)).thenReturn(true);
    service.supprimer(mail);

}

@DisplayName("Test pour supprimer un client / Nok_mail non trouvé")
@Test
void supprimerClientNotOkMail() {
    String mail = "test@test.com";
    Mockito.when(daoMock.existsById(mail)).thenReturn(false);
    assertThrows(EntityNotFoundException.class, ()-> service.supprimer(mail) );

}
//@DisplayName("Test pour supprimer un client / Nok_reservation")
//@Test
//void supprimerAdminNotOkReservation() {
//    String mail = "test@test.com";
//    Mockito.when(daoMock.existsById(mail)).thenReturn(true);
//    Mockito.when(daoMock.count()).thenReturn(1L);
//    assertThrows(IllegalStateException.class, () -> service.supprimer(mail));
//}



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