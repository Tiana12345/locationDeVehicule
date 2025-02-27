package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.repository.VeloDao;
import com.accenture.repository.entity.Velo;
import com.accenture.service.dto.VeloRequestDto;
import com.accenture.service.dto.VeloResponseDto;
import com.accenture.service.mapper.VeloMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VeloServiceImpl implements VeloService {

    private final VeloDao veloDao;
    private final VeloMapper veloMapper;
    private static final Logger logger = LoggerFactory.getLogger(VeloServiceImpl.class);

    public VeloServiceImpl(VeloDao veloDao, VeloMapper veloMapper) {
        this.veloDao = veloDao;
        this.veloMapper = veloMapper;
    }

    @Override
    public VeloResponseDto ajouter(VeloRequestDto veloRequestDto) throws VehiculeException {
        verifVelo(veloRequestDto);

        Velo velo = veloMapper.toVelo(veloRequestDto);
        Velo veloEnreg = veloDao.save(velo);
        return veloMapper.toVeloResponseDto(veloEnreg);
    }


    @Override
    public VeloResponseDto trouver(long id) throws EntityNotFoundException {
        Optional<Velo> optVelo = veloDao.findById(id);
        if (optVelo.isEmpty())
            throw new EntityNotFoundException("Erreur, aucun vélo trouvé avec cet id");
        Velo velo = optVelo.get();
        return veloMapper.toVeloResponseDto(velo);
    }


    @Override
    public List<VeloResponseDto> trouverToutes() {
        return veloDao.findAll().stream()
                .map(veloMapper::toVeloResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public VeloResponseDto modifierPartiellement(Long id, VeloRequestDto veloRequestDto) throws VehiculeException, EntityNotFoundException {
        Optional<Velo> optVelo = veloDao.findById(id);
        if (optVelo.isEmpty())
            throw new VehiculeException("Erreur, l'identifiant ne correspond à aucun vélo en base");
        Velo veloExistante = optVelo.get();

        Velo nouvelle = veloMapper.toVelo(veloRequestDto);
        remplacer(nouvelle, veloExistante);

        Velo veloEnreg = veloDao.save(veloExistante);
        return getVeloResponseDto(veloEnreg);
    }

    @Override
    public VeloResponseDto getVeloResponseDto(Velo veloEnreg) {
        return veloMapper.toVeloResponseDto(veloEnreg);
    }

    @Override
    public void supprimer(Long id) throws EntityNotFoundException {
        if (veloDao.existsById(id))
            veloDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucun vélo n'est enregistré sous cet identifiant");
    }

    @Override
    public List<VeloResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer tailleCadre,
                                            Integer poids, Boolean electrique, Integer capaciteBatterie, Integer autonomie,
                                            Boolean freinsADisque, Long tarifJournalier, Long kilometrage, Boolean actif,
                                            Boolean retireDuParc) {

        List<Velo> liste = veloDao.findAll();

        liste = rechercheVelo(id, marque, modele, couleur, tailleCadre, poids, electrique, capaciteBatterie, autonomie,
                freinsADisque, tarifJournalier, kilometrage, actif, retireDuParc, liste);

        return liste.stream()
                .map(veloMapper::toVeloResponseDto)
                .collect(Collectors.toList());
    }

    private static void verifVelo(VeloRequestDto veloRequestDto) {
        if (veloRequestDto == null)
            throw new VehiculeException("Le veloRequestDto est null");
        if (veloRequestDto.marque() == null || veloRequestDto.marque().isBlank())
            throw new VehiculeException("Vous devez ajouter la marque du vélo");
        if (veloRequestDto.modele() == null || veloRequestDto.modele().isBlank())
            throw new VehiculeException("Vous devez ajouter le modèle du vélo");
        if (veloRequestDto.couleur() == null || veloRequestDto.couleur().isBlank())
            throw new VehiculeException("Vous devez ajouter la couleur du vélo");
        if (veloRequestDto.tailleCadre() == null || veloRequestDto.tailleCadre() <= 0)
            throw new VehiculeException("Vous devez ajouter la taille du cadre du vélo");
        if (veloRequestDto.poids() == null || veloRequestDto.poids() <= 0)
            throw new VehiculeException("Vous devez ajouter le poids du vélo");
        if (veloRequestDto.electrique() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo est électrique");
        if (veloRequestDto.capaciteBatterie() == null || veloRequestDto.capaciteBatterie() <= 0)
            throw new VehiculeException("Vous devez ajouter la capacité de la batterie du vélo");
        if (veloRequestDto.autonomie() == null || veloRequestDto.autonomie() <= 0)
            throw new VehiculeException("Vous devez ajouter l'autonomie du vélo");
        if (veloRequestDto.freinsADisque() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo a des freins à disque");
        if (veloRequestDto.tarifJournalier() <= 0)
            throw new VehiculeException("Vous devez ajouter le tarif journalier du vélo");
        if (veloRequestDto.kilometrage() < 0)
            throw new VehiculeException("Vous devez ajouter le kilométrage du vélo");
        if (veloRequestDto.actif() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo est actif");
        if (veloRequestDto.retireDuParc() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo est retiré du parc");
    }

    private static void remplacer(Velo velo, Velo veloExistante) {
        if (velo.getMarque() != null && !velo.getMarque().isBlank())
            veloExistante.setMarque(velo.getMarque());
        if (velo.getModele() != null && !velo.getModele().isBlank())
            veloExistante.setModele(velo.getModele());
        if (velo.getCouleur() != null && !velo.getCouleur().isBlank())
            veloExistante.setCouleur(velo.getCouleur());
        if (velo.getTailleCadre() != null && velo.getTailleCadre() > 0)
            veloExistante.setTailleCadre(velo.getTailleCadre());
        if (velo.getPoids() != null && velo.getPoids() > 0)
            veloExistante.setPoids(velo.getPoids());
        if (velo.getElectrique() != null)
            veloExistante.setElectrique(velo.getElectrique());
        if (velo.getCapaciteBatterie() != null && velo.getCapaciteBatterie() > 0)
            veloExistante.setCapaciteBatterie(velo.getCapaciteBatterie());
        if (velo.getAutonomie() != null && velo.getAutonomie() > 0)
            veloExistante.setAutonomie(velo.getAutonomie());
        if (velo.getFreinsADisque() != null)
            veloExistante.setFreinsADisque(velo.getFreinsADisque());
        if (velo.getTarifJournalier() > 0)
            veloExistante.setTarifJournalier(velo.getTarifJournalier());
        if (velo.getKilometrage() >= 0)
            veloExistante.setKilometrage(velo.getKilometrage());
        if (velo.getActif() != null)
            veloExistante.setActif(velo.getActif());
        if (velo.getRetireDuParc() != null)
            veloExistante.setRetireDuParc(velo.getRetireDuParc());
    }

    private static List<Velo> rechercheVelo(Long id, String marque, String modele, String couleur, Integer tailleCadre,
                                            Integer poids, Boolean electrique, Integer capaciteBatterie, Integer autonomie,
                                            Boolean freinsADisque, Long tarifJournalier, Long kilometrage, Boolean actif,
                                            Boolean retireDuParc, List<Velo> liste) throws VehiculeException {
        logger.debug("Initial list size: {}", liste.size());
        if (id != null && id != 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getId() == id)
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by ID: {}", liste.size());
        }
        if (marque != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getMarque().contains(marque))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by marque: {}", liste.size());
        }
        if (modele != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getModele().contains(modele))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by modele: {}", liste.size());
        }
        if (couleur != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getCouleur().contains(couleur))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by couleur: {}", liste.size());
        }
        if (tailleCadre != null && tailleCadre > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getTailleCadre().equals(tailleCadre))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by tailleCadre: {}", liste.size());
        }
        if (poids != null && poids > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getPoids().equals(poids))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by poids: {}", liste.size());
        }
        if (electrique != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getElectrique().equals(electrique))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by electrique: {}", liste.size());
        }
        if (capaciteBatterie != null && capaciteBatterie > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getCapaciteBatterie().equals(capaciteBatterie))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by capaciteBatterie: {}", liste.size());
        }
        if (autonomie != null && autonomie > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getAutonomie().equals(autonomie))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by autonomie: {}", liste.size());
        }
        if (freinsADisque != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getFreinsADisque().equals(freinsADisque))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by freinsADisque: {}", liste.size());
        }
        if (tarifJournalier != null && tarifJournalier > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getTarifJournalier() == (tarifJournalier))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by tarifJournalier: {}", liste.size());
        }
        if (kilometrage != null && kilometrage >= 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getKilometrage() == (kilometrage))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by kilometrage: {}", liste.size());
        }
        if (actif != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getActif().equals(actif))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by actif: {}", liste.size());
        }
        if (retireDuParc != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getRetireDuParc().equals(retireDuParc))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by retireDuParc: {}", liste.size());
        }
        if (liste.isEmpty()) {
            throw new VehiculeException("Un critère de recherche est obligatoire !");
        }
        return liste;
    }
}