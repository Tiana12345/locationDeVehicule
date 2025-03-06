package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.model.param.Permis;
import com.accenture.repository.ClientDao;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Classe d'implémentation du service de gestion des clients.
 * Cette classe fournit des méthodes pour ajouter, trouver, modifier, et supprimer des clients,
 * ainsi que pour rechercher des clients en fonction de plusieurs critères.
 */
@Service
public class ClientServiceImpl implements ClientService {
    private final ClientDao clientDao;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;


    private static final String REGEX_PW = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[&#@-_§])[A-Za-z\\d&%$_]{8,16}$";

    /**
     * Constructeur de la classe ClientServiceImpl.
     *
     * @param clientDao       l'objet DAO pour accéder aux données des clients
     * @param clientMapper    l'objet Mapper pour convertir entre les entités Client et les DTO
     * @param passwordEncoder l'objet pour encoder les mots de passe des clients
     */

    public ClientServiceImpl(ClientDao clientDao, ClientMapper clientMapper, PasswordEncoder passwordEncoder) {
        this.clientDao = clientDao;
        this.clientMapper = clientMapper;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Méthode servant à ajouter un client.
     * Cette méthode vérifie la validité des informations du client, encode le mot de passe,
     * enregistre le client dans la base de données, puis retourne un objet ClientResponseDto
     * contenant les informations du client ajouté.
     *
     * @param clientRequestDto l'objet contenant les informations du client à ajouter
     * @return un objet ClientResponseDto contenant les informations du client ajouté
     * @throws UtilisateurException si une erreur survient lors de l'ajout du client,
     *                              par exemple si les informations du client sont invalides
     */
    @Override
    public ClientResponseDto ajouter(ClientRequestDto clientRequestDto) throws UtilisateurException {
        verifClient(clientRequestDto);

        Client client = clientMapper.toClient(clientRequestDto);
        String passwordChiffre = passwordEncoder.encode(client.getPassword());
        client.setPassword(passwordChiffre);

        Client clientEnreg = clientDao.save(client);
        return clientMapper.toClientResponseDto(clientEnreg);
    }

    /**
     * Méthode servant à trouver un client par son adresse email.
     *
     * @param mail l'adresse email du client à trouver
     * @return un objet ClientResponseDto contenant les informations du client trouvé
     * @throws EntityNotFoundException si aucun client n'est trouvé avec cette adresse email
     */

    @Override
    public ClientResponseDto trouver(String mail) throws EntityNotFoundException {
        Optional<Client> optclient = clientDao.findById(mail);
        if (optclient.isEmpty())
            throw new EntityNotFoundException("Erreur, aucun client trouvé avec cette adresse mail");
        Client client = optclient.get();
        return clientMapper.toClientResponseDto(client);
    }

    /**
     * Méthode pour afficher tous les clients.
     *
     * @return une liste d'objets ClientResponseDto contenant les informations de tous les clients
     */

    @Override
    public List<ClientResponseDto> trouverTous() {
        return clientDao.findAll().stream()
                .map(clientMapper::toClientResponseDto)
                .toList();
    }


    /**
     * Méthode pour modifier un ou plusieurs paramètres d'un compte client.
     *
     * @param mail             l'adresse email du client à modifier
     * @param clientRequestDto l'objet contenant les nouvelles informations du client
     * @return un objet ClientResponseDto contenant les informations du client modifié
     * @throws UtilisateurException    si une erreur survient lors de la modification du client
     * @throws EntityNotFoundException si aucun client n'est trouvé avec cette adresse email
     */

    @Override
    public ClientResponseDto modifierPartiellement(String mail, ClientRequestDto clientRequestDto) throws UtilisateurException, EntityNotFoundException {

        Optional<Client> optClient = clientDao.findById(mail);
        if (optClient.isEmpty())
            throw new UtilisateurException("Erreur, l'identifiant ne correspond pas");
        Client clientExistant = optClient.get();

        Client nouveau = clientMapper.toClient(clientRequestDto);
        remplacer(nouveau, clientExistant);

        Client clientEnreg = clientDao.save(clientExistant);
        return clientMapper.toClientResponseDto(clientEnreg);
    }

    /**
     * Méthode servant à supprimer un client.
     *
     * @param mail l'adresse email du client à supprimer
     * @throws EntityNotFoundException si aucun client n'est trouvé avec cette adresse email
     */
    //ajouter une vérification si le client possède des locations en cours
    @Override
    public void supprimer(String mail) throws EntityNotFoundException {
        if (clientDao.existsById(mail))

            clientDao.deleteById(mail);
        else
            throw new EntityNotFoundException("Aucun utilisateur n'est enregistré sous cette adresse mail.");
    }

    /**
     * Méthode permettant de retrouver un client selon l'un des paramètres choisis.
     *
     * @param mail            l'adresse email du client (String)
     * @param prenom          le prénom du client (String)
     * @param nom             le nom du client (String)
     * @param dateNaissance   la date de naissance du client (LocalDate)
     * @param rue             la rue de l'adresse du client (String)
     * @param codePostal      le code postal de l'adresse du client (String)
     * @param ville           la ville de l'adresse du client (String)
     * @param desactive       indique si le client est désactivé (Boolean)
     * @param listePermis     la liste des permis du client (List<Permis>)
     * @param dateInscription la date d'inscription du client (LocalDate)
     * @return une liste d'objets ClientResponseDto correspondant aux critères de recherche
     * @throws UtilisateurException si un critère de recherche est obligatoire
     */
    @Override
    public List<ClientResponseDto> rechercher(String mail, String prenom, String nom, LocalDate dateNaissance, String rue,
                                              String codePostal, String ville, Boolean desactive, List<Permis> listePermis,
                                              LocalDate dateInscription) throws UtilisateurException {
        List<Client> liste = clientDao.findAll();

        liste = rechercheClient(mail, prenom, nom, dateNaissance, rue, codePostal, ville, desactive, listePermis, dateInscription, liste);

        return liste.stream()
                .map(clientMapper::toClientResponseDto)
                .toList();
    }

    //______________________________________________________________________________________________________________________
//    METHODES PRIVEES
//_______________________________________________________________________________________________________________________
    private static List<Client> rechercheClient(String mail, String prenom, String nom, LocalDate dateNaissance, String rue, String codePostal, String ville, Boolean desactive, List<Permis> listePermis, LocalDate dateInscription, List<Client> liste) {

        if (listePermis != null) {
            liste = liste.stream()
                    .filter(client -> client.getListePermis().equals(listePermis))
                    .toList();
        }
        if (liste == null)
            throw new UtilisateurException("Un critère de recherche est obligatoire ! ");

        liste = rechercheClientParProprieteCompte(desactive, dateInscription, liste);
        liste = rechercheClientParPropriete(mail, prenom, nom, dateNaissance, liste);
        liste = rechercherClientParAdresse(rue, codePostal, ville, liste);

        return liste;
    }

    private static List<Client> rechercheClientParProprieteCompte(Boolean desactive, LocalDate dateInscription, List<Client> liste) {
        if (desactive != null) {
            liste = liste.stream()
                    .filter(client -> client.getDesactive().equals(desactive))
                    .toList();
        }

        if (dateInscription != null) {
            liste = liste.stream()
                    .filter(client -> client.getDateInscription().equals(dateInscription))
                    .toList();
        }
        return liste;
    }

    private static List<Client> rechercheClientParPropriete(String mail, String prenom, String nom, LocalDate dateNaissance, List<Client> liste) {
        if (mail != null) {
            liste = liste.stream()
                    .filter(client -> client.getMail().contains(mail))
                    .toList();
        }
        if (prenom != null) {
            liste = liste.stream()
                    .filter(client -> client.getPrenom().contains(prenom))
                    .toList();
        }
        if (nom != null) {
            liste = liste.stream()
                    .filter(client -> client.getNom().contains(nom))
                    .toList();
        }
        if (dateNaissance != null) {
            liste = liste.stream()
                    .filter(client -> client.getDateNaissance().equals(dateNaissance))
                    .toList();
        }
        return liste;
    }

    private static List<Client> rechercherClientParAdresse(String rue, String codePostal, String ville, List<Client> liste) {
        if (rue != null) {
            liste = liste.stream()
                    .filter(client -> client.getAdresse().getRue().equals(rue))
                    .toList();
        }
        if (codePostal != null) {
            liste = liste.stream()
                    .filter(client -> client.getAdresse().getCodePostal().equals(codePostal))
                    .toList();
        }
        if (ville != null) {
            liste = liste.stream()
                    .filter(client -> client.getAdresse().getVille().equals(ville))
                    .toList();
        }
        return liste;
    }


    private static void verifClient(ClientRequestDto clientRequestDto) {
        if (clientRequestDto == null)
            throw new UtilisateurException("Le clientRequestDto est null");
        verifClientAuth(clientRequestDto);
        verifProprieteClient(clientRequestDto);
        verifClientAdresse(clientRequestDto);
    }

    private static void verifClientAuth(ClientRequestDto clientRequestDto) {
        if (clientRequestDto.mail() == null || clientRequestDto.mail().isBlank())
            throw new UtilisateurException("L'adresse mail est obligatoire");
        if (!clientRequestDto.password().matches(REGEX_PW) || clientRequestDto.password().isBlank())
            throw new UtilisateurException("Le mot de passe est obligatoire et doit correspondre au type demandé");
    }

    private static void verifProprieteClient(ClientRequestDto clientRequestDto) {
        if (clientRequestDto.nom() == null || clientRequestDto.nom().isBlank())
            throw new UtilisateurException("Le nom est obligatoire");
        if (clientRequestDto.prenom() == null || clientRequestDto.prenom().isBlank())
            throw new UtilisateurException("Le prenom est obligatoire");
        if (clientRequestDto.dateNaissance() == null)
            throw new UtilisateurException("La date de naissance est obligatoire");
        if (clientRequestDto.dateNaissance().isAfter(LocalDate.now().minusYears(18)))
            throw new UtilisateurException("Vous devez avoir au moins 18 ans pour vous inscrire");
    }

    private static void verifClientAdresse(ClientRequestDto clientRequestDto) {
        if (clientRequestDto.adresse().rue() == null || clientRequestDto.adresse().rue().isBlank())
            throw new UtilisateurException("Vous devez renseigner le nom de votre rue");
        if (clientRequestDto.adresse().codePostal() == null || clientRequestDto.adresse().codePostal().isBlank())
            throw new UtilisateurException("Vous devez renseigner votre code postal");
        if (clientRequestDto.adresse().ville() == null || clientRequestDto.adresse().ville().isBlank())
            throw new UtilisateurException("Vous devez renseigner le nom de votre ville");
    }

    private static void remplacer(Client client, Client clientExistant) {
        remplacerClientAdresse(client, clientExistant);
        remplacerClientIdentite(client, clientExistant);
        remplacerClientAuth(client, clientExistant);
    }

    private static void remplacerClientIdentite(Client client, Client clientExistant) {
        if (client.getNom() != null && !client.getNom().isBlank())
            clientExistant.setNom(client.getNom());
        if (client.getPrenom() != null && !client.getPrenom().isBlank())
            clientExistant.setPrenom(client.getPrenom());
        if (client.getDateNaissance() != null)
            clientExistant.setDateNaissance(client.getDateNaissance());}

    private static void remplacerClientAdresse(Client client, Client clientExistant) {
        if (client.getAdresse() != null) {
            if (client.getAdresse().getRue() != null && !client.getAdresse().getRue().isBlank())
                clientExistant.getAdresse().setRue(client.getAdresse().getRue());
            if (client.getAdresse().getCodePostal() != null && !client.getAdresse().getCodePostal().isBlank())
                clientExistant.getAdresse().setCodePostal(client.getAdresse().getCodePostal());
            if (client.getAdresse().getVille() != null && !client.getAdresse().getVille().isBlank())
                clientExistant.getAdresse().setVille(client.getAdresse().getVille());
        }
    }
    private static void remplacerClientAuth(Client client, Client clientExistant) {
        if (client.getMail() != null && !client.getMail().isBlank())
            clientExistant.setMail(client.getMail());
        if (client.getPassword() != null && !client.getPassword().isBlank())
            clientExistant.setPassword(client.getPassword());
    }
   
}
