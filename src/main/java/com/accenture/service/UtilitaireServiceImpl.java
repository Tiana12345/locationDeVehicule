package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.param.Accessoires;
import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import com.accenture.repository.UtilitaireDao;
import com.accenture.repository.entity.Utilitaire;
import com.accenture.service.dto.UtilitaireRequestDto;
import com.accenture.service.dto.UtilitaireResponseDto;
import com.accenture.service.mapper.UtilitaireMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Classe d'implémentation du service de gestion des utilitaires.
 * Cette classe fournit des méthodes pour ajouter, trouver, modifier, et supprimer des utilitaires,
 * ainsi que pour rechercher des utilitaires en fonction de plusieurs critères.
 */
@Slf4j
@Service
public class UtilitaireServiceImpl implements UtilitaireService {

    private final UtilitaireDao utilitaireDao;
    private final UtilitaireMapper utilitaireMapper;

    public UtilitaireServiceImpl(UtilitaireDao utilitaireDao, UtilitaireMapper utilitaireMapper) {
        this.utilitaireDao = utilitaireDao;
        this.utilitaireMapper = utilitaireMapper;
    }

    /**
     * Ajouter un nouvel utilitaire.
     *
     * @param utilitaireRequestDto l'objet contenant les informations de l'utilitaire à ajouter
     * @return un objet UtilitaireResponseDto contenant les informations de l'utilitaire ajouté
     * @throws VehiculeException si une erreur survient lors de l'ajout de l'utilitaire
     */

    @Override
    public UtilitaireResponseDto ajouter(UtilitaireRequestDto utilitaireRequestDto) throws VehiculeException {
        verifUtilitaire(utilitaireRequestDto);

        Utilitaire utilitaire = utilitaireMapper.toUtilitaire(utilitaireRequestDto);
        if (utilitaire.getPoidsPATC()<= 3.5){
        utilitaire.setPermis(Permis.B);
        }
        else if (utilitaire.getPoidsPATC()> 3.5 && utilitaire.getPoidsPATC()<7.5){
            utilitaire.setPermis(Permis.C1);
        }
        else log.info("L'utilitaire est trop lourd");

        utilitaire.ajouterAccessoire(Accessoires.DIABLE_UTILITAIRE);
        utilitaire.ajouterAccessoire(Accessoires.SANGLE_UTILITAIRE);
        utilitaire.ajouterAccessoire(Accessoires.COUVERTURE_PROTECTION_UTILITAIRE);

        Utilitaire utilitaireEnreg = utilitaireDao.save(utilitaire);
        return utilitaireMapper.toUtilitaireResponseDto(utilitaireEnreg);
    }

    /**
     * Trouver un utilitaire par son identifiant.
     *
     * @param id l'identifiant unique de l'utilitaire à trouver
     * @return un objet UtilitaireResponseDto contenant les informations de l'utilitaire trouvé
     * @throws EntityNotFoundException si aucun utilitaire n'est trouvé avec l'identifiant fourni
     */

    @Override
    public UtilitaireResponseDto trouver(long id) throws EntityNotFoundException {
        Optional<Utilitaire> optUtilitaire = utilitaireDao.findById(id);
        if (optUtilitaire.isEmpty())
            throw new EntityNotFoundException("Erreur, aucun utilitaire trouvé avec cet id");
        Utilitaire utilitaire = optUtilitaire.get();
        return utilitaireMapper.toUtilitaireResponseDto(utilitaire);
    }

    /**
     * Trouver tous les utilitaires.
     *
     * @return une liste d'objets UtilitaireResponseDto contenant les informations de tous les utilitaires
     */

    @Override
    public List<UtilitaireResponseDto> trouverToutes() {
        return utilitaireDao.findAll().stream()
                .map(utilitaireMapper::toUtilitaireResponseDto)
                .toList();
    }

    /**
     * Modifier partiellement un utilitaire.
     *
     * @param id                   l'identifiant unique de l'utilitaire à modifier
     * @param utilitaireRequestDto l'objet contenant les informations de l'utilitaire à modifier
     * @return un objet UtilitaireResponseDto contenant les informations de l'utilitaire modifié
     * @throws VehiculeException       si une erreur survient lors de la modification de l'utilitaire
     * @throws EntityNotFoundException si aucun utilitaire n'est trouvé avec l'identifiant fourni
     */


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

    /**
     * Convertir une entité Utilitaire en un objet UtilitaireResponseDto.
     *
     * @param utilitaireEnreg l'entité Utilitaire à convertir
     * @return un objet UtilitaireResponseDto contenant les informations de l'utilitaire
     */

    @Override
    public UtilitaireResponseDto getUtilitaireResponseDto(Utilitaire utilitaireEnreg) {
        return utilitaireMapper.toUtilitaireResponseDto(utilitaireEnreg);
    }

    /**
     * Supprimer un utilitaire par son identifiant.
     *
     * @param id l'identifiant unique de l'utilitaire à supprimer
     * @throws EntityNotFoundException si aucun utilitaire n'est trouvé avec l'identifiant fourni
     */

    @Override
    public void supprimer(Long id) throws EntityNotFoundException {
        if (utilitaireDao.existsById(id))
            utilitaireDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucun utilitaire n'est enregistré sous cet identifiant");
    }

    /**
     * Rechercher des utilitaires par critères.
     *
     * @param id              l'identifiant de l'utilitaire
     * @param marque          la marque de l'utilitaire
     * @param modele          le modèle de l'utilitaire
     * @param couleur         la couleur de l'utilitaire
     * @param nombreDePlace   le nombre de places de l'utilitaire
     * @param carburant       le type de carburant de l'utilitaire
     * @param transmission    la transmission de l'utilitaire
     * @param clim            l'utilitaire a-t-il la climatisation
     * @param chargeMax       la charge maximale de l'utilitaire
     * @param poidsPATC       le poids PATC de l'utilitaire
     * @param capaciteM3      la capacité en m3 de l'utilitaire
     * @param permis     la liste des permis requis pour l'utilitaire
     * @param tarifJournalier le tarif journalier de l'utilitaire
     * @param kilometrage     le kilométrage de l'utilitaire
     * @param actif           l'utilitaire est-il actif
     * @param retireDuParc    l'utilitaire est-il retiré du parc
     * @return la liste des utilitaires correspondant aux critères
     */

    @Override
    public List<UtilitaireResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer nombreDePlace,
                                                  Carburant carburant, String transmission, Boolean clim, Integer chargeMax,
                                                  Integer poidsPATC, Integer capaciteM3,Permis permis, Long tarifJournalier,
                                                  Long kilometrage, Boolean actif, Boolean retireDuParc) {

        List<Utilitaire> liste = utilitaireDao.findAll();


        liste = rechercheUtilitaire(id, marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax,
                poidsPATC, capaciteM3, permis, tarifJournalier, kilometrage, actif, retireDuParc, liste);

        return liste.stream()
                .map(utilitaireMapper::toUtilitaireResponseDto)
                .toList();
    }

    private static void verifUtilitaire(UtilitaireRequestDto utilitaireRequestDto) {
        if (utilitaireRequestDto == null)
            throw new VehiculeException("Le utilitaireRequestDto est null");

        if (utilitaireRequestDto.actif() == null)
            throw new VehiculeException("Vous devez indiquer si l'utilitaire est actif");
        if (utilitaireRequestDto.retireDuParc() == null)
            throw new VehiculeException("Vous devez indiquer si l'utilitaire est retiré du parc");
        verifUtilitaireParamBase(utilitaireRequestDto);
        verifUtilitairesOptions(utilitaireRequestDto);}

    private static void verifUtilitairesOptions(UtilitaireRequestDto utilitaireRequestDto) {
        if (utilitaireRequestDto.carburant()== null)
            throw new VehiculeException("Vous devez ajouter le type de carburant de l'utilitaire");
        if (utilitaireRequestDto.transmission() == null || utilitaireRequestDto.transmission().isBlank())
            throw new VehiculeException("Vous devez ajouter la transmission de l'utilitaire");
        if (utilitaireRequestDto.clim() == null)
            throw new VehiculeException("Vous devez ajouter l'information concernant la clim");
        if (utilitaireRequestDto.chargeMax() <= 0)
            throw new VehiculeException("Vous devez ajouter la charge maximale de l'utilitaire");
        if (utilitaireRequestDto.poidsPATC() <= 0)
            throw new VehiculeException("Vous devez ajouter le poids PATC de l'utilitaire");
        if (utilitaireRequestDto.capaciteM3() <= 0)
            throw new VehiculeException("Vous devez ajouter la capacité en m3 de l'utilitaire");
        if (utilitaireRequestDto.tarifJournalier() <= 0)
            throw new VehiculeException("Vous devez ajouter le tarif journalier de l'utilitaire");
        if (utilitaireRequestDto.kilometrage() < 0)
            throw new VehiculeException("Vous devez ajouter le kilométrage de l'utilitaire");
    }

    private static void verifUtilitaireParamBase(UtilitaireRequestDto utilitaireRequestDto) {
        if (utilitaireRequestDto.marque() == null || utilitaireRequestDto.marque().isBlank())
            throw new VehiculeException("Vous devez ajouter la marque de l'utilitaire");
        if (utilitaireRequestDto.modele() == null || utilitaireRequestDto.modele().isBlank())
            throw new VehiculeException("Vous devez ajouter le modèle de l'utilitaire");
        if (utilitaireRequestDto.couleur() == null || utilitaireRequestDto.couleur().isBlank())
            throw new VehiculeException("Vous devez ajouter la couleur de l'utilitaire");
        if ( utilitaireRequestDto.nombreDePlace() <= 0)
            throw new VehiculeException("Vous devez ajouter le nombre de places de l'utilitaire");
    }

    private static void remplacer(Utilitaire utilitaire, Utilitaire utilitaireExistante) {

        if (utilitaire.getActif() != null)
            utilitaireExistante.setActif(utilitaire.getActif());
        if (utilitaire.getRetireDuParc() != null)
            utilitaireExistante.setRetireDuParc(utilitaire.getRetireDuParc());
        utilitaireRemplacerParamBase(utilitaire, utilitaireExistante);
        remplacerUtilitaireOptions(utilitaire, utilitaireExistante); }

    private static void remplacerUtilitaireOptions(Utilitaire utilitaire, Utilitaire utilitaireExistante) {
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
        if (utilitaire.getPermis() != null)
            utilitaireExistante.setPermis(utilitaire.getPermis());
        if (utilitaire.getTarifJournalier() > 0)
            utilitaireExistante.setTarifJournalier(utilitaire.getTarifJournalier());
        if (utilitaire.getKilometrage() >= 0)
            utilitaireExistante.setKilometrage(utilitaire.getKilometrage());
    }

    private static void utilitaireRemplacerParamBase(Utilitaire utilitaire, Utilitaire utilitaireExistante) {
        if (utilitaire.getMarque() != null && !utilitaire.getMarque().isBlank())
            utilitaireExistante.setMarque(utilitaire.getMarque());
        if (utilitaire.getModele() != null && !utilitaire.getModele().isBlank())
            utilitaireExistante.setModele(utilitaire.getModele());
        if (utilitaire.getCouleur() != null && !utilitaire.getCouleur().isBlank())
            utilitaireExistante.setCouleur(utilitaire.getCouleur());
        if (utilitaire.getNombreDePlace() != null && utilitaire.getNombreDePlace() > 0)
            utilitaireExistante.setNombreDePlace(utilitaire.getNombreDePlace());
    }

    private static List<Utilitaire> rechercheUtilitaire(Long id, String marque, String modele, String couleur, Integer nombreDePlace,
                                                        Carburant carburant, String transmission, Boolean clim, Integer chargeMax,
                                                        Integer poidsPATC, Integer capaciteM3, Permis permis, Long tarifJournalier,
                                                        Long kilometrage, Boolean actif, Boolean retireDuParc, List<Utilitaire> liste) throws VehiculeException {
        log.debug("Initial list size: {}", liste.size());
        if (id != null && id != 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getId() == id)
                    .toList();
            log.debug("List size after filtering by ID: {}", liste.size());
        }
        if (actif != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getActif().equals(actif))
                    .toList();
            log.debug("List size after filtering by actif: {}", liste.size());
        }
        if (retireDuParc != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getRetireDuParc().equals(retireDuParc))
                    .toList();
            log.debug("List size after filtering by retireDuParc: {}", liste.size());
        }
        if (liste.isEmpty()) {
            throw new VehiculeException("Un critère de recherche est obligatoire !");
        }
        liste = rechercherUtilitaireParamBase(marque, modele, couleur, nombreDePlace, liste);
        liste = rechercheUtilitaireParOptions(marque, modele, couleur, nombreDePlace, carburant, transmission, clim, chargeMax, poidsPATC,
                capaciteM3, permis, tarifJournalier, kilometrage, liste);

        return liste;
    }

    private static List<Utilitaire> rechercheUtilitaireParOptions(String marque, String modele, String couleur, Integer nombreDePlace,
                                                                  Carburant carburant, String transmission, Boolean clim, Integer chargeMax,
                                                                  Integer poidsPATC, Integer capaciteM3, Permis permis, Long tarifJournalier,
                                                                  Long kilometrage, List<Utilitaire> liste) {

        if (carburant != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getCarburant().equals(carburant))
                    .toList();
            log.debug("List size after filtering by carburant: {}", liste.size());
        }
        if (transmission != null && (transmission.equals("automatique") || transmission.equals("manuelle"))) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getTransmission().contains(transmission))
                    .toList();
            log.debug("List size after filtering by transmission: {}", liste.size());
        }
        if (clim != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getClim().equals(clim))
                    .toList();
            log.debug("List size after filtering by clim: {}", liste.size());
        }

        if (permis != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getPermis() == permis)
                    .toList();
            log.debug("List size after filtering by permis: {}", liste.size());
        }
        if (tarifJournalier != null && tarifJournalier > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getTarifJournalier() == tarifJournalier)
                    .toList();
            log.debug("List size after filtering by tarifJournalier: {}", liste.size());
        }
        if (kilometrage != null && kilometrage >= 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getKilometrage() == kilometrage)
                    .toList();
            log.debug("List size after filtering by kilometrage: {}", liste.size());


        }
        liste = rechercheOptionsPourUtilitaire(chargeMax, poidsPATC, capaciteM3, liste);
        liste = rechercherUtilitaireParamBase (marque, modele, couleur,  nombreDePlace, liste);

            return liste;
    }

    private static List<Utilitaire> rechercheOptionsPourUtilitaire(Integer chargeMax, Integer poidsPATC, Integer capaciteM3, List<Utilitaire> liste) {
        if (chargeMax != null && chargeMax > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getChargeMax().equals(chargeMax))
                    .toList();
            log.debug("List size after filtering by chargeMax: {}", liste.size());
        }
        if (poidsPATC != null && poidsPATC > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getPoidsPATC().intValue() == poidsPATC)
                    .toList();
            log.debug("List size after filtering by poidsPATC: {}", liste.size());
        }
        if (capaciteM3 != null && capaciteM3 > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getCapaciteM3().equals(capaciteM3))
                    .toList();
            log.debug("List size after filtering by capaciteM3: {}", liste.size());
        }
        return liste;
    }

    private static List<Utilitaire> rechercherUtilitaireParamBase(String marque, String modele, String couleur, Integer nombreDePlace, List<Utilitaire> liste) {
        if (marque != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getMarque().contains(marque))
                    .toList();
            log.debug("List size after filtering by marque: {}", liste.size());
        }
        if (modele != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getModele().contains(modele))
                    .toList();
            log.debug("List size after filtering by modele: {}", liste.size());
        }
        if (couleur != null) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getCouleur().contains(couleur))
                    .toList();
            log.debug("List size after filtering by couleur: {}", liste.size());
        }
        if (nombreDePlace != null && nombreDePlace > 0) {
            liste = liste.stream()
                    .filter(utilitaire -> utilitaire.getNombreDePlace().equals(nombreDePlace))
                    .toList();
            log.debug("List size after filtering by nombreDePlace: {}", liste.size());
        }
        return liste;
    }
}