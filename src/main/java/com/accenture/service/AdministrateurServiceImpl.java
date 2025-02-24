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


@Service
public class AdministrateurServiceImpl implements AdministrateurService {
    private final AdministrateurDao administrateurDao;
    private final AdministrateurMapper administrateurMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String REGEX_PW= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[&#@-_§])[A-Za-z\\d&%$_]{8,16}$";

    public AdministrateurServiceImpl(AdministrateurDao administrateurDao, AdministrateurMapper administrateurMapper, PasswordEncoder passwordEncoder) {
        this.administrateurDao = administrateurDao;
        this.administrateurMapper = administrateurMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *<p>Méthode pour ajouter un administrateur</p>
     * @param administrateurRequestDto
     * @return toAdministrateurResponseDto(adminEnreg)
     * @throws UtilisateurException
     */

    @Override
    public AdministrateurResponseDto ajouter(AdministrateurRequestDto administrateurRequestDto)throws UtilisateurException{
        verifAdmin(administrateurRequestDto);

        Administrateur admin = administrateurMapper.toAdministrateur(administrateurRequestDto);

        admin.setFonction("FONCTION_ADMIN");
        String passwordChiffre = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(passwordChiffre);

        Administrateur adminEnreg = administrateurDao.save(admin);
        return administrateurMapper.toAdministrateurResponseDto(adminEnreg);
    }

    /**
     * <p> Méthode pour supprimer un administrateur </p>
     * @param mail
     * @throws EntityNotFoundException
     */

    @Override
    public void supprimer(String mail) throws EntityNotFoundException{
        if (!administrateurDao.existsById(mail)) {
            throw new EntityNotFoundException("Aucun compte trouvé avec cette adresse mail");
        }

        long count = administrateurDao.count();
        if (count <= 1) {
            throw new IllegalStateException("Impossible de supprimer le dernier administrateur");
        }

        administrateurDao.deleteById(mail);
    }

    public List<AdministrateurResponseDto> rechercher(String mail, String prenom, String nom, String fonction ) {
        List<Administrateur> liste = null;
        Optional<Administrateur> optional;

        if (mail != null)
            optional = administrateurDao.findByMailContaining(mail);
         else if (prenom != null)
            liste = administrateurDao.findByPrenomContaining(prenom);
         else if (nom != null)
            liste = administrateurDao.findByNomContaining(nom);
         else if (fonction != null)
            liste = administrateurDao.findByFonctionContaining(fonction);

        if (liste == null)
            throw new UtilisateurException("Un critère de recherche est obligatoire ! ");
        return liste.stream()
                .map(administrateurMapper::toAdministrateurResponseDto)
                .toList();
    }
    /**
     *<p>Méthode pour modifier tous les paramètres d'un admin</p>
     * @param mail
     * @param administrateurRequestDto
     * @return toAdminResponseDto(adminEnreg)
     * @throws UtilisateurException
     * @throws EntityNotFoundException
     */

    @Override
    public AdministrateurResponseDto modifier(String mail, AdministrateurRequestDto administrateurRequestDto) throws UtilisateurException, EntityNotFoundException {
        if (!administrateurDao.existsById(mail))
            throw new UtilisateurException("Erreur, l'identifiant ne correspond pas");
        verifAdmin(administrateurRequestDto);

        Administrateur admin = administrateurMapper.toAdministrateur(administrateurRequestDto);
        admin.setMail(mail);
        Administrateur adminEnreg = administrateurDao.save(admin);
        return administrateurMapper.toAdministrateurResponseDto(adminEnreg);

    }

    /**
     *<p>Méthode pour modifier un ou plusieurs paramètres d'un compte admin</p>
     * @param mail
     * @param administrateurRequestDto
     * @return toAdministrateurResponseDto(adminEnreg)
     * @throws UtilisateurException
     * @throws EntityNotFoundException
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


    private static void remplacer(Administrateur administrateurRequestDto, Administrateur administrateurRequestDtoExistant) {
        if (administrateurRequestDto.getMail() != null)
            administrateurRequestDtoExistant.setMail(administrateurRequestDto.getMail());
        if (administrateurRequestDto.getNom() != null)
            administrateurRequestDtoExistant.setNom(administrateurRequestDto.getNom());
        if (administrateurRequestDto.getPrenom() != null)
            administrateurRequestDtoExistant.setPrenom(administrateurRequestDto.getPrenom());
        if (administrateurRequestDto.getFonction()!= null)
            administrateurRequestDtoExistant.setFonction(administrateurRequestDto.getFonction());

    }
}
