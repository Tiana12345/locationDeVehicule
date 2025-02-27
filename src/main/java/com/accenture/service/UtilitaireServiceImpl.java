package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.repository.UtilitaireDao;
import com.accenture.repository.entity.Utilitaire;
import com.accenture.service.dto.UtilitaireRequestDto;
import com.accenture.service.dto.UtilitaireResponseDto;
import com.accenture.service.mapper.UtilitaireMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilitaireServiceImpl implements UtilitaireService {

    private final UtilitaireDao utilitaireDao;
    private final UtilitaireMapper utilitaireMapper;
    private static final Logger logger = LoggerFactory.getLogger(UtilitaireServiceImpl.class);

    public UtilitaireServiceImpl(UtilitaireDao utilitaireDao, UtilitaireMapper utilitaireMapper) {
        this.utilitaireDao = utilitaireDao;
        this.utilitaireMapper = utilitaireMapper;
    }

    @Override
    public UtilitaireResponseDto ajouter(UtilitaireRequestDto utilitaireRequestDto) throws VehiculeException {
        verifUtilitaire(utilitaireRequestDto);

        Utilitaire utilitaire = utilitaireMapper.toUtilitaire(utilitaireRequestDto);
        Utilitaire utilitaireEnreg = utilitaireDao.save(utilitaire);
        return utilitaireMapper.toUtilitaireResponseDto(utilitaireEnreg);
    }

    @Override
    public UtilitaireResponseDto trouver(long id) throws EntityNotFoundException {
        Optional<Utilitaire> optUtilitaire = utilitaireDao.findById(id);
        if (optUtilitaire.isEmpty())
            throw new EntityNotFoundException("Erreur, aucun utilitaire trouvé avec cet id");
        Utilitaire utilitaire = optUtilitaire.get();
        return utilitaireMapper.toUtilitaireResponseDto(utilitaire);
    }

    @Override
    public List<UtilitaireResponseDto> trouverToutes() {
        return utilitaireDao.findAll().stream()
                .map(utilitaireMapper::toUtilitaireResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UtilitaireResponseDto modifierPartiellement(Long id, UtilitaireRequestDto utilitaireRequestDto) throws VehiculeException, EntityNotFoundException {
        Optional<Utilitaire> optUtilitaire = utilitaireDao.findById(id);
        if (optUtilitaire.isEmpty())
            throw new VehiculeException("Erreur, l'identifiant ne correspond à aucun utilitaire en base");
        Utilitaire utilitaireExistante = optUtilitaire.get();

        Utilitaire nouvelle = utilitaireMapper.toUtilitaire(utilitaireRequestDto);
        remplacer(nouvelle, utilitaireExistante);

        Utilitaire utilitaireEnreg = utilitaireDao.save(utilitaireExistante);
        return getUtilitaireResponseDto(utilitaireEnreg);
    }

    @Override
    public UtilitaireResponseDto getUtilitaireResponseDto(Utilitaire utilitaireEnreg) {
        return utilitaireMapper.toUtilitaireResponseDto(utilitaireEnreg);
    }

    @Override
    public void supprimer(Long id) throws EntityNotFoundException {
        if (utilitaireDao.existsById(id))
            utilitaireDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucun utilitaire n'est enregistré sous cet identifiant");
    }

    @Override
    public List<UtilitaireResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer nombreDePlace,
                                                  Carburant carburant, String transmission, Boolean clim, Integer chargeMax,
                                                  Integer poidsPATC, Integer capaciteM3, List<Permis> listePermis, Long tarifJournalier,
                                                  Long kilometrage, Boolean actif, Boolean retireDuParc) {

        List<Utilitaire> liste = utilitaireDao.findAll();


        liste = rechercheUtilitaire(id, marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax,
                poidsPATC, capaciteM3, listePermis, tarifJournalier, kilometrage, actif, retireDuParc, liste);

        return liste.stream()
                .map(utilitaireMapper::toUtilitaireResponseDto)
                .collect(Collectors.toList());
    }

    private static void verifUtilitaire(UtilitaireRequestDto utilitaireRequestDto) {
        if (utilitaireRequestDto == null)
            throw new VehiculeException("Le utilitaireRequestDto est null");
        if (utilitaireRequestDto.marque() == null || utilitaireRequestDto.marque().isBlank())
            throw new VehiculeException("Vous devez ajouter la marque de l'utilitaire");
        if (utilitaireRequestDto.modele() == null || utilitaireRequestDto.modele().isBlank())
            throw new VehiculeException("Vous devez ajouter le modèle de l'utilitaire");
        if (utilitaireRequestDto.couleur() == null || utilitaireRequestDto.couleur().isBlank())
            throw new VehiculeException("Vous devez ajouter la couleur de l'utilitaire");
        if (utilitaireRequestDto.nombreDePlace() == null || utilitaireRequestDto.nombreDePlace() <= 0)
            throw new VehiculeException("Vous devez ajouter le nombre de places de l'utilitaire");
        if (utilitaireRequestDto.carburant() == null)
            throw new VehiculeException("Vous devez ajouter le type de carburant de l'utilitaire");
        if (utilitaireRequestDto.transmission() == null || utilitaireRequestDto.transmission().isBlank())
            throw new VehiculeException("Vous devez ajouter la transmission de l'utilitaire");
        if (utilitaireRequestDto.clim() == null)
            throw new VehiculeException("Vous devez ajouter l'information concernant la clim");
        if (utilitaireRequestDto.chargeMax() == null || utilitaireRequestDto.chargeMax() <= 0)
            throw new VehiculeException("Vous devez ajouter la charge maximale de l'utilitaire");
        if (utilitaireRequestDto.poidsPATC() == null || utilitaireRequestDto.poidsPATC() <= 0)
            throw new VehiculeException("Vous devez ajouter le poids PATC de l'utilitaire");
        if (utilitaireRequestDto.capaciteM3() == null || utilitaireRequestDto.capaciteM3() <= 0)
            throw new VehiculeException("Vous devez ajouter la capacité en m3 de l'utilitaire");
        if (utilitaireRequestDto.listePermis() == null || utilitaireRequestDto.listePermis().isEmpty())
            throw new VehiculeException("Vous devez ajouter les permis requis pour l'utilitaire");
        if (utilitaireRequestDto.tarifJournalier() <= 0)
            throw new VehiculeException("Vous devez ajouter le tarif journalier de l'utilitaire");
        if (utilitaireRequestDto.kilometrage() < 0)
            throw new VehiculeException("Vous devez ajouter le kilométrage de l'utilitaire");
        if (utilitaireRequestDto.actif() == null)
            throw new VehiculeException("Vous devez indiquer si l'utilitaire est actif");
        if (utilitaireRequestDto.retireDuParc() == null)
            throw new VehiculeException("Vous devez indiquer si l'utilitaire est retiré du parc");
    }

    private static void remplacer(Utilitaire utilitaire, Utilitaire utilitaireExistante) {
        if (utilitaire.getMarque() != null && !utilitaire.getMarque().isBlank())
            utilitaireExistante.setMarque(utilitaire.getMarque());
        if (utilitaire.getModele() != null && !utilitaire.getModele().isBlank())
            utilitaireExistante.setModele(utilitaire.getModele());
        if (utilitaire.getCouleur() != null && !utilitaire.getCouleur().isBlank())
            utilitaireExistante.setCouleur(utilitaire.getCouleur());
        if (utilitaire.getNombreDePlace() != null && utilitaire.getNombreDePlace() > 0)
            utilitaireExistante.setNombreDePlace(utilitaire.getNombreDePlace());
        if (utilitaire.getCarburant() != null)
            utilitaireExistante.setCarburant(utilitaire.getCarburant());
        if (utilitaire.getTransmission() != null && !utilitaire.getTransmission().isBlank())
            utilitaireExistante.setTransmission(utilitaire.getTransmission());
        if (utilitaire.getClim() != null)
            utilitaireExistante.setClim(utilitaire.getClim());
        if (utilitaire.getChargeMax() != null && utilitaire.getChargeMax() > 0)
            utilitaireExistante.setChargeMax(utilitaire.getChargeMax());
        if (utilitaire.getPoidsPATC() != null && utilitaire.getPoidsPATC() > 0)
            utilitaireExistante.setPoidsPATC(utilitaire.getPoidsPATC());
        if (utilitaire.getCapaciteM3() != null && utilitaire.getCapaciteM3() > 0)
            utilitaireExistante.setCapaciteM3(utilitaire.getCapaciteM3());
        if (utilitaire.getListePermis() != null && !utilitaire.getListePermis().isEmpty())
            utilitaireExistante.setListePermis(utilitaire.getListePermis());
        if (utilitaire.getTarifJournalier() > 0)
            utilitaireExistante.setTarifJournalier(utilitaire.getTarifJournalier());
        if (utilitaire.getKilometrage() >= 0)
            utilitaireExistante.setKilometrage(utilitaire.getKilometrage());
        if (utilitaire.getActif() != null)
            utilitaireExistante.setActif(utilitaire.getActif());
        if (utilitaire.getRetireDuParc() != null)
            utilitaireExistante.setRetireDuParc(utilitaire.getRetireDuParc());
    }

       private static List<Utilitaire>rechercheUtilitaire(Long id, String marque, String modele, String couleur, Integer nombreDePlace,
                                                          Carburant carburant, String transmission, Boolean clim, Integer chargeMax,
                                                          Integer poidsPATC, Integer capaciteM3, List<Permis> listePermis, Long tarifJournalier,
                                                          Long kilometrage, Boolean actif, Boolean retireDuParc, List<Utilitaire> liste) throws VehiculeException {
        logger.debug("Initial list size: {}", liste.size());
        if (id != null && id != 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getId() == id)
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by ID: {}", liste.size());
        }
        if (marque != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getMarque().contains(marque))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by marque: {}", liste.size());
        }
        if (modele != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getModele().contains(modele))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by modele: {}", liste.size());
        }
        if (couleur != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getCouleur().contains(couleur))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by couleur: {}", liste.size());
        }
        if (nombreDePlace != null && nombreDePlace > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getNombreDePlace().equals(nombreDePlace))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by nombreDePlace: {}", liste.size());
        }
        if (carburant != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getCarburant().equals(carburant))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by carburant: {}", liste.size());
        }
        if (transmission != null && (transmission.equals("automatique") || transmission.equals("manuelle"))) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getTransmission().contains(transmission))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by transmission: {}", liste.size());
        }
        if (clim != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getClim().equals(clim))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by clim: {}", liste.size());
        }
        if (chargeMax != null && chargeMax > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getChargeMax().equals(chargeMax))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by chargeMax: {}", liste.size());
        }
        if (poidsPATC != null && poidsPATC > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getPoidsPATC().equals(poidsPATC))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by poidsPATC: {}", liste.size());
        }
        if (capaciteM3 != null && capaciteM3 > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getCapaciteM3().equals(capaciteM3))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by capaciteM3: {}", liste.size());
        }
        if (listePermis != null && !listePermis.isEmpty()) {
            Permis permis = listePermis.getFirst();
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getListePermis().contains(permis))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by permis: {}", liste.size());
        }
        if (tarifJournalier != null && tarifJournalier > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getTarifJournalier()==(tarifJournalier))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by tarifJournalier: {}", liste.size());
        }
        if (kilometrage != null && kilometrage >= 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getKilometrage()==(kilometrage))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by kilometrage: {}", liste.size());
        }
        if (actif != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getActif().equals(actif))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by actif: {}", liste.size());
        }
        if (retireDuParc != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getRetireDuParc().equals(retireDuParc))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by retireDuParc: {}", liste.size());
        }
        if (liste.isEmpty()) {
            throw new VehiculeException("Un critère de recherche est obligatoire !");
        }
        return liste;
    }
}