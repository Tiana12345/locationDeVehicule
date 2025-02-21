package com.accenture.service;

import com.accenture.exception.UtilisateurException;
import com.accenture.repository.AdministrateurDao;
import com.accenture.repository.entity.Administrateur;
import com.accenture.service.dto.AdministrateurRequestDto;
import com.accenture.service.dto.AdministrateurResponseDto;
import com.accenture.service.mapper.AdministrateurMapper;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {
    private final AdministrateurDao administrateurDao;
    private final AdministrateurMapper administrateurMapper;
    private final PasswordEncoder passwordEncoder;

    public AdministrateurServiceImpl(AdministrateurDao administrateurDao, AdministrateurMapper administrateurMapper, PasswordEncoder passwordEncoder) {
        this.administrateurDao = administrateurDao;
        this.administrateurMapper = administrateurMapper;
        this.passwordEncoder = passwordEncoder;
    }

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

    @Override
    public void supprimer(String mail) throws EntityActionVetoException{
        if (administrateurDao.existsById(mail))
            administrateurDao.deleteById(mail);
        else
            throw new EntityNotFoundException("Aucun compte trouvé avec cette adresse mail");
    }


    private static void verifAdmin(AdministrateurRequestDto administrateurRequestDto) {
        if (administrateurRequestDto == null)
            throw new UtilisateurException("l'administrateurRequestDto est null");
        if (administrateurRequestDto.nom() == null || administrateurRequestDto.nom().isBlank())
            throw new UtilisateurException("Le nom est obligatoire");
        if (administrateurRequestDto.prenom() == null || administrateurRequestDto.prenom().isBlank())
            throw new UtilisateurException("Le prenom est obligatoire");
        if (administrateurRequestDto.mail() == null || administrateurRequestDto.mail().isBlank())
            throw new UtilisateurException("Le mail est obligatoire");
        if (administrateurRequestDto.password() == null || administrateurRequestDto.password().isBlank())
            throw new UtilisateurException("Le mot de passe est obligatoire");
        if (administrateurRequestDto.fonction() == null || administrateurRequestDto.fonction().isBlank())
            throw new UtilisateurException("La fonction est obligatoire");
    }


}
