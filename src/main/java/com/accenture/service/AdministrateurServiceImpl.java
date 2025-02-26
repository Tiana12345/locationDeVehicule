package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.repository.AdministrateurDao;
import com.accenture.repository.entity.Administrateur;
import com.accenture.service.dto.AdministrateurRequestDto;
import com.accenture.service.dto.AdministrateurResponseDto;
import com.accenture.service.mapper.AdministrateurMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe d'implémentation du service de gestion des administrateurs.
 * Cette classe fournit des méthodes pour ajouter, trouver, modifier, et supprimer des administrateurs,
 * ainsi que pour rechercher des administrateurs en fonction de plusieurs critères.
 */
@Service
public class AdministrateurServiceImpl implements AdministrateurService {
    private final AdministrateurDao administrateurDao;
    private final AdministrateurMapper administrateurMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String REGEX_PW = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[&#@-_§])[A-Za-z\\d&%$_]{8,16}$";

    /**
     * Constructeur de la classe AdministrateurServiceImpl.
     *
     * @param administrateurDao    l'objet DAO pour accéder aux données des administrateurs
     * @param administrateurMapper l'objet Mapper pour convertir entre les entités Administrateur et les DTO
     * @param passwordEncoder      l'objet pour encoder les mots de passe des administrateurs
     */
    public AdministrateurServiceImpl(AdministrateurDao administrateurDao, AdministrateurMapper administrateurMapper, PasswordEncoder passwordEncoder) {
        this.administrateurDao = administrateurDao;
        this.administrateurMapper = administrateurMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Méthode pour ajouter un administrateur.
     * Cette méthode vérifie la validité des informations de l'administrateur, encode le mot de passe,
     * enregistre l'administrateur dans la base de données, puis retourne un objet AdministrateurResponseDto
     * contenant les informations de l'administrateur ajouté.
     *
     * @param administrateurRequestDto l'objet contenant les informations de l'administrateur à ajouter
     * @return un objet AdministrateurResponseDto contenant les informations de l'administrateur ajouté
     * @throws UtilisateurException si une erreur survient lors de l'ajout de l'administrateur
     */

    @Override
    public AdministrateurResponseDto ajouter(AdministrateurRequestDto administrateurRequestDto) throws UtilisateurException {
        verifAdmin(administrateurRequestDto);

        Administrateur admin = administrateurMapper.toAdministrateur(administrateurRequestDto);

        admin.setFonction("FONCTION_ADMIN");
        String passwordChiffre = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(passwordChiffre);

        Administrateur adminEnreg = administrateurDao.save(admin);
        return administrateurMapper.toAdministrateurResponseDto(adminEnreg);
    }

    /**
     * Méthode pour supprimer un administrateur.
     * Cette méthode vérifie si un administrateur existe dans la base de données avec l'adresse email fournie.
     * Si l'administrateur existe et qu'il n'est pas le dernier administrateur, il est supprimé de la base de données.
     *
     * @param mail l'adresse email de l'administrateur à supprimer
     * @throws EntityNotFoundException si aucun administrateur n'est trouvé avec cette adresse email
     * @throws IllegalStateException   si l'administrateur à supprimer est le dernier administrateur
     */

    @Override
    public void supprimer(String mail) throws EntityNotFoundException {
        if (!administrateurDao.existsById(mail)) {
            throw new EntityNotFoundException("Aucun compte trouvé avec cette adresse mail");
        }

        long count = administrateurDao.count();
        if (count <= 1) {
            throw new IllegalStateException("Impossible de supprimer le dernier administrateur");
        }

        administrateurDao.deleteById(mail);
    }

    /**
     * Méthode pour rechercher des administrateurs en fonction de plusieurs critères.
     *
     * @param mail     l'adresse email de l'administrateur (String)
     * @param prenom   le prénom de l'administrateur (String)
     * @param nom      le nom de l'administrateur (String)
     * @param fonction la fonction de l'administrateur (String)
     * @return une liste d'objets AdministrateurResponseDto correspondant aux critères de recherche
     * @throws UtilisateurException si un critère de recherche est obligatoire
     */
    @Override
    public List<AdministrateurResponseDto> rechercher(String mail, String prenom, String nom, String fonction) throws UtilisateurException {
        List<Administrateur> liste = administrateurDao.findAll();

        if (liste == null) {
            throw new UtilisateurException("La méthode findAll a retourné null !");
        }
        if (administrateurDao == null) {
            throw new UtilisateurException("administrateurDao n'est pas initialisé !");
        }
        if (administrateurMapper == null) {
            throw new UtilisateurException("administrateurMapper n'est pas initialisé !");
        }

        liste = rechercheAdministrateur(mail, prenom, nom, fonction, liste);

        return liste.stream()
                .map(administrateurMapper::toAdministrateurResponseDto)
                .collect(Collectors.toList());
    }


    /**
     * Méthode pour modifier un ou plusieurs paramètres d'un compte administrateur.
     *
     * @param mail                     l'adresse email de l'administrateur à modifier
     * @param administrateurRequestDto l'objet contenant les nouvelles informations de l'administrateur
     * @return un objet AdministrateurResponseDto contenant les informations de l'administrateur modifié
     * @throws UtilisateurException    si une erreur survient lors de la modification de l'administrateur
     * @throws EntityNotFoundException si aucun administrateur n'est trouvé avec cette adresse email
     */
    @Override
    public AdministrateurResponseDto modifierPartiellement(String mail, AdministrateurRequestDto administrateurRequestDto) throws UtilisateurException, EntityNotFoundException {

        Optional<Administrateur> optAdmin = administrateurDao.findById(mail);
        if (optAdmin.isEmpty())
            throw new UtilisateurException("Erreur, l'identifiant ne correspond pas");
        Administrateur adminExistant = optAdmin.get();

        Administrateur nouveau = administrateurMapper.toAdministrateur(administrateurRequestDto);
        remplacer(nouveau, adminExistant);

        Administrateur adminEnreg = administrateurDao.save(adminExistant);
        return administrateurMapper.toAdministrateurResponseDto(adminEnreg);

    }


// ______________________________________________________________________________________________________________________
//    METHODES PRIVEES
//_______________________________________________________________________________________________________________________

    private static void verifAdmin(AdministrateurRequestDto administrateurRequestDto) {
        if (administrateurRequestDto == null)
            throw new UtilisateurException("l'administrateurRequestDto est null");
        if (administrateurRequestDto.nom() == null || administrateurRequestDto.nom().isBlank())
            throw new UtilisateurException("Le nom est obligatoire");
        if (administrateurRequestDto.prenom() == null || administrateurRequestDto.prenom().isBlank())
            throw new UtilisateurException("Le prenom est obligatoire");
        if (administrateurRequestDto.mail() == null || administrateurRequestDto.mail().isBlank())
            throw new UtilisateurException("Le mail est obligatoire");
        if (!administrateurRequestDto.password().matches(REGEX_PW) || administrateurRequestDto.password().isBlank())
            throw new UtilisateurException("Le mot de passe est obligatoire");
        if (administrateurRequestDto.fonction() == null || administrateurRequestDto.fonction().isBlank())
            throw new UtilisateurException("La fonction est obligatoire");
    }


    private static void remplacer(Administrateur administrateur, Administrateur administrateurExistant) {
        if (administrateur.getMail() != null && !administrateur.getMail().isBlank())
            administrateurExistant.setMail(administrateur.getMail());
        if (administrateur.getNom() != null && !administrateur.getNom().isBlank())
            administrateurExistant.setNom(administrateur.getNom());
        if (administrateur.getPrenom() != null && !administrateur.getPrenom().isBlank())
            administrateurExistant.setPrenom(administrateur.getPrenom());
        if (administrateur.getFonction() != null && !administrateur.getFonction().isBlank())
            administrateurExistant.setFonction(administrateur.getFonction());
    }

    private static List<Administrateur> rechercheAdministrateur(String mail, String prenom, String nom, String fonction, List<Administrateur> liste) {
        if (mail != null) {
            liste = liste.stream()
                    .filter(admin -> admin.getMail().contains(mail))
                    .collect(Collectors.toList());
        }
        if (prenom != null) {
            liste = liste.stream()
                    .filter(admin -> admin.getPrenom().contains(prenom))
                    .collect(Collectors.toList());
        }
        if (nom != null) {
            liste = liste.stream()
                    .filter(admin -> admin.getNom().contains(nom))
                    .collect(Collectors.toList());
        }
        if (fonction != null) {
            liste = liste.stream()
                    .filter(admin -> admin.getFonction().contains(fonction))
                    .collect(Collectors.toList());
        }
        if (liste == null) {
            throw new UtilisateurException("Un critère de recherche est obligatoire !");
        }
        return liste;
    }
}

