package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.model.Permis;
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

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientDao clientDao;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;


    public ClientServiceImpl(ClientDao clientDao, ClientMapper clientMapper, PasswordEncoder passwordEncoder) {
        this.clientDao = clientDao;
        this.clientMapper = clientMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * <p>méthode servant a ajouter un client</p>
     * @param clientRequestDto
     * @return toClientResponseDto(clientEnreg)
     * @throws UtilisateurException
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
     *
     * @param mail
     * @return toClientResponseDto(client)
     * @throws EntityNotFoundException
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
     *  <p>Méthode pour afficher tout les clients</p>
     * @return clientDao.findAll()
     */

    @Override
    public List<ClientResponseDto> trouverTous() {
        return clientDao.findAll().stream()
                .map(clientMapper::toClientResponseDto)
                .toList();
    }

    /**
     *<p>Méthode pour modifier tous les paramètres d'un client</p>
     * @param mail
     * @param clientRequestDto
     * @return toClientResponseDto(clientEnreg)
     * @throws UtilisateurException
     * @throws EntityNotFoundException
     */

    @Override
    public ClientResponseDto modifier(String mail, ClientRequestDto clientRequestDto) throws UtilisateurException, EntityNotFoundException {
        if (!clientDao.existsById(mail))
            throw new UtilisateurException("Erreur, l'identifiant ne correspond pas");
        verifClient(clientRequestDto);

        Client client = clientMapper.toClient(clientRequestDto);
        client.setMail(mail);
        Client clientEnreg = clientDao.save(client);
        //return tacheDao.save(tacheRequestDto);
        return clientMapper.toClientResponseDto(clientEnreg);
    }

    /**
     *<p>Méthode pour modifier un ou plusieurs paramètres d'un compte client</p>
     * @param mail
     * @param clientRequestDto
     * @return toClientResponseDto(clientEnreg)
     * @throws UtilisateurException
     * @throws EntityNotFoundException
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
     * <p>Méthode servant à supprimer un client</p>
     * @param mail
     * @throws EntityNotFoundException
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
     * <p>Méthode permettant de retorouver un client selon l'un des paramètres choisi</p>
     * @param mail
     * @param prenom
     * @param nom
     * @param dateNaissance
     * @param rue
     * @param codePostal
     * @param ville
     * @param desactive
     * @param listePermis
     * @param dateInscription
     * @return toClientResponseDto
     */
    @Override
    public List<ClientResponseDto> rechercher(String mail, String prenom, String nom, LocalDate dateNaissance, String rue, String codePostal, String ville, Boolean desactive, List<Permis> listePermis, LocalDate dateInscription) {
        List<Client> liste = null;


        if (mail != null)
            liste = clientDao.findByMailContaining(mail);
        else if (prenom != null)
            liste = clientDao.findByPrenomContaining(prenom);
        else if (nom != null)
            liste = clientDao.findBynomContaining(nom);
        else if (dateNaissance != null)
            liste = clientDao.findByDateNaissance(dateNaissance);
        else if (rue != null)
            liste = clientDao.findByAdresse_RueContaining(rue);
        else if (codePostal != null)
            liste = clientDao.findByAdresse_CodePostalContaining(codePostal);
        else if (ville != null)
            liste = clientDao.findByAdresse_VilleContaining(ville);
        else if (desactive != null)
            liste = clientDao.findByDesactive(desactive);
        else if (dateInscription != null)
            liste = clientDao.findByDateInscription(dateInscription);
        else if (listePermis != null && !listePermis.isEmpty()) {
            Permis permis = listePermis.get(0);
            liste = clientDao.findByListePermisContaining(permis);
        }
        if (liste == null)
            throw new UtilisateurException("Un critère de recherche est obligatoire ! ");
        return liste.stream()
                .map(clientMapper::toClientResponseDto)
                .toList();
    }
//______________________________________________________________________________________________________________________
//    METHODES PRIVEES
//_______________________________________________________________________________________________________________________
    static void verifClient(ClientRequestDto clientRequestDto) {
        if (clientRequestDto == null)
            throw new UtilisateurException("Le clientRequestDto est null");
        if (clientRequestDto.nom() == null || clientRequestDto.nom().isBlank())
            throw new UtilisateurException("Le nom est obligatoire");
        if (clientRequestDto.prenom() == null || clientRequestDto.prenom().isBlank())
            throw new UtilisateurException("Le prenom est obligatoire");
        if (clientRequestDto.adresse().rue() == null || clientRequestDto.adresse().rue().isBlank())
            throw new UtilisateurException("Vous devez renseigner le nom de votre rue");
        if (clientRequestDto.adresse().codePostal() == null || clientRequestDto.adresse().codePostal().isBlank())
            throw new UtilisateurException("Vous devez renseigner votre code postal");
        if (clientRequestDto.adresse().ville() == null || clientRequestDto.adresse().ville().isBlank())
            throw new UtilisateurException("Vous devez renseigner le nom de votre ville");
        if (clientRequestDto.mail() == null || clientRequestDto.mail().isBlank())
            throw new UtilisateurException("L'adresse mail est obligatoire");
        if (clientRequestDto.password() == null || clientRequestDto.password().isBlank())
            throw new UtilisateurException("Le mot de passe est obligatoire");
        if (clientRequestDto.dateNaissance() == null)
            throw new UtilisateurException("La date de naissance est obligatoire");
        if (clientRequestDto.dateNaissance().isAfter(LocalDate.now().minusYears(18)))
            throw new UtilisateurException("Vous devez avoir au moins 18 ans pour vous inscrire");
        LocalDate dateDeReference = LocalDate.now().minusYears(18);
//        if (clientRequestDto.dateDeNaissance().isBefore(dateDeReference) || clientRequestDto.dateDeNaissance().isEqual(dateDeReference))
//            throw new UtilisateurException("Vous devez avoir plus de 18 ans pour vous incrire");
    }

    private static void remplacer(Client clientRequestDto, Client clientRequestDtoExistant) {
        if (clientRequestDto.getNom() != null)
            clientRequestDtoExistant.setNom(clientRequestDto.getNom());
        if (clientRequestDto.getPrenom() != null)
            clientRequestDtoExistant.setPrenom(clientRequestDto.getPrenom());

        if (clientRequestDto.getAdresse().getRue() != null)
            clientRequestDtoExistant.getAdresse().setRue(clientRequestDto.getAdresse().getRue());
        if (clientRequestDto.getAdresse().getCodePostal() != null)
            clientRequestDtoExistant.getAdresse().setCodePostal(clientRequestDto.getAdresse().getCodePostal());
        if (clientRequestDto.getAdresse().getVille() != null)
            clientRequestDtoExistant.getAdresse().setVille(clientRequestDto.getAdresse().getVille());

        if (clientRequestDto.getMail() != null)
            clientRequestDtoExistant.setMail(clientRequestDto.getMail());
        if (clientRequestDto.getPassword() != null)
            clientRequestDtoExistant.setPassword(clientRequestDto.getPassword());
        if (clientRequestDto.getDateNaissance() != null)
            clientRequestDtoExistant.setDateNaissance(clientRequestDto.getDateNaissance());
    }
}
