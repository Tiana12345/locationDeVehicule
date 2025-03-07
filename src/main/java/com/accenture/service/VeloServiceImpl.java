package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.param.Accessoires;
import com.accenture.repository.VeloDao;
import com.accenture.repository.entity.Velo;
import com.accenture.service.dto.VeloRequestDto;
import com.accenture.service.dto.VeloResponseDto;
import com.accenture.service.mapper.VeloMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Classe d'implémentation du service de gestion des vélos.
 * Cette classe fournit des méthodes pour ajouter, trouver, modifier, et supprimer des vélos,
 * ainsi que pour rechercher des vélos en fonction de plusieurs critères.
 */
@Slf4j
@Service
public class VeloServiceImpl implements VeloService {

    private final VeloDao veloDao;
    private final VeloMapper veloMapper;

    public VeloServiceImpl(VeloDao veloDao, VeloMapper veloMapper) {
        this.veloDao = veloDao;
        this.veloMapper = veloMapper;
    }

    /**
     * Ajouter un nouveau vélo.
     *
     * @param veloRequestDto l'objet contenant les informations du vélo à ajouter
     * @return un objet VeloResponseDto contenant les informations du vélo ajouté
     * @throws VehiculeException si une erreur survient lors de l'ajout du vélo
     */

    @Override
    public VeloResponseDto ajouter(VeloRequestDto veloRequestDto) throws VehiculeException {
        verifVelo(veloRequestDto);

        Velo velo = veloMapper.toVelo(veloRequestDto);

        velo.ajouterAccessoire(Accessoires.CASQUE_VELO);
        velo.ajouterAccessoire(Accessoires.ANTIVOL_VELO);
        velo.ajouterAccessoire(Accessoires.PANIER_VELO);
        velo.ajouterAccessoire(Accessoires.SACOCHE_VELO);
        velo.ajouterAccessoire(Accessoires.KIT_REPARATION_VELO);
        velo.ajouterAccessoire(Accessoires.GILET_REFLECHISSANT_VELO);

        Velo veloEnreg = veloDao.save(velo);
        return veloMapper.toVeloResponseDto(veloEnreg);
    }

    /**
     * Trouver un vélo par son identifiant.
     *
     * @param id l'identifiant unique du vélo à trouver
     * @return un objet VeloResponseDto contenant les informations du vélo trouvé
     * @throws EntityNotFoundException si aucun vélo n'est trouvé avec l'identifiant fourni
     */

    @Override
    public VeloResponseDto trouver(long id) throws EntityNotFoundException {
        Optional<Velo> optVelo = veloDao.findById(id);
        if (optVelo.isEmpty())
            throw new EntityNotFoundException("Erreur, aucun vélo trouvé avec cet id");
        Velo velo = optVelo.get();
        return veloMapper.toVeloResponseDto(velo);
    }

    /**
     * Trouver tous les vélos.
     *
     * @return une liste d'objets VeloResponseDto contenant les informations de tous les vélos
     */

    @Override
    public List<VeloResponseDto> trouverToutes() {
        return veloDao.findAll().stream()
                .map(veloMapper::toVeloResponseDto)
                .toList();
    }

    /**
     * Modifier partiellement un vélo.
     *
     * @param id             l'identifiant unique du vélo à modifier
     * @param veloRequestDto l'objet contenant les informations du vélo à modifier
     * @return un objet VeloResponseDto contenant les informations du vélo modifié
     * @throws VehiculeException       si une erreur survient lors de la modification du vélo
     * @throws EntityNotFoundException si aucun vélo n'est trouvé avec l'identifiant fourni
     */
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

    /**
     * Convertir une entité Velo en un objet VeloResponseDto.
     *
     * @param veloEnreg l'entité Velo à convertir
     * @return un objet VeloResponseDto contenant les informations du vélo
     */

    @Override
    public VeloResponseDto getVeloResponseDto(Velo veloEnreg) {
        return veloMapper.toVeloResponseDto(veloEnreg);
    }

    /**
     * Supprimer un vélo par son identifiant.
     *
     * @param id l'identifiant unique du vélo à supprimer
     * @throws EntityNotFoundException si aucun vélo n'est trouvé avec l'identifiant fourni
     */
    @Override
    public void supprimer(Long id) throws EntityNotFoundException {
        if (veloDao.existsById(id))
            veloDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucun vélo n'est enregistré sous cet identifiant");
    }

    /**
     * Rechercher des vélos par critères.
     *
     * @param id               l'identifiant du vélo
     * @param marque           la marque du vélo
     * @param modele           le modèle du vélo
     * @param couleur          la couleur du vélo
     * @param tailleCadre      la taille du cadre du vélo
     * @param poids            le poids du vélo
     * @param electrique       le vélo est-il électrique
     * @param capaciteBatterie la capacité de la batterie du vélo
     * @param autonomie        l'autonomie du vélo
     * @param freinsADisque    le vélo a-t-il des freins à disque
     * @param tarifJournalier  le tarif journalier du vélo
     * @param kilometrage      le kilométrage du vélo
     * @param actif            le vélo est-il actif
     * @param retireDuParc     le vélo est-il retiré du parc
     * @return la liste des vélos correspondant aux critères
     */

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
                .toList();
    }


    private static void verifVelo(VeloRequestDto veloRequestDto) {
        if (veloRequestDto == null)
            throw new VehiculeException("Le veloRequestDto est null");
        verifVeloMarqueModele(veloRequestDto);
        verifVeloCaracteristiques(veloRequestDto);
        verifVeloElectrique(veloRequestDto);
        verifVeloTarifKilometrage(veloRequestDto);
        verifVeloEtat(veloRequestDto);
    }

    private static void verifVeloMarqueModele(VeloRequestDto veloRequestDto) {
        if (veloRequestDto.marque() == null || veloRequestDto.marque().isBlank())
            throw new VehiculeException("Vous devez ajouter la marque du vélo");
        if (veloRequestDto.modele() == null || veloRequestDto.modele().isBlank())
            throw new VehiculeException("Vous devez ajouter le modèle du vélo");
    }

    private static void verifVeloCaracteristiques(VeloRequestDto veloRequestDto) {
        if (veloRequestDto.couleur() == null || veloRequestDto.couleur().isBlank())
            throw new VehiculeException("Vous devez ajouter la couleur du vélo");
        if (veloRequestDto.tailleCadre() == null || veloRequestDto.tailleCadre() <= 0)
            throw new VehiculeException("Vous devez ajouter la taille du cadre du vélo");
        if (veloRequestDto.poids() == null || veloRequestDto.poids() <= 0)
            throw new VehiculeException("Vous devez ajouter le poids du vélo");
    }

    private static void verifVeloElectrique(VeloRequestDto veloRequestDto) {
        if (veloRequestDto.electrique() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo est électrique");
        if (veloRequestDto.freinsADisque() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo a des freins à disque");
        if (veloRequestDto.electrique() == true) {
            if (veloRequestDto.capaciteBatterie() == null || veloRequestDto.capaciteBatterie() <= 0)
                throw new VehiculeException("Vous devez ajouter la capacité de la batterie du vélo");
            if (veloRequestDto.autonomie() == null || veloRequestDto.autonomie() <= 0)
                throw new VehiculeException("Vous devez ajouter l'autonomie du vélo");
        }
    }

    private static void verifVeloTarifKilometrage(VeloRequestDto veloRequestDto) {
        if (veloRequestDto.tarifJournalier() <= 0)
            throw new VehiculeException("Vous devez ajouter le tarif journalier du vélo");
        if (veloRequestDto.kilometrage() < 0)
            throw new VehiculeException("Vous devez ajouter le kilométrage du vélo");
    }

    private static void verifVeloEtat(VeloRequestDto veloRequestDto) {
        if (veloRequestDto.actif() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo est actif");
        if (veloRequestDto.retireDuParc() == null)
            throw new VehiculeException("Vous devez indiquer si le vélo est retiré du parc");
    }

    private static void remplacer(Velo velo, Velo veloExistante) {
        remplacerMarqueModele(velo, veloExistante);
        remplacerCaracteristiques(velo, veloExistante);
        remplacerElectrique(velo, veloExistante);
        remplacerTarifKilometrage(velo, veloExistante);
        remplacerEtat(velo, veloExistante);
    }

    private static void remplacerMarqueModele(Velo velo, Velo veloExistante) {
        if (velo.getMarque() != null && !velo.getMarque().isBlank())
            veloExistante.setMarque(velo.getMarque());
        if (velo.getModele() != null && !velo.getModele().isBlank())
            veloExistante.setModele(velo.getModele());
    }

    private static void remplacerCaracteristiques(Velo velo, Velo veloExistante) {
        if (velo.getCouleur() != null && !velo.getCouleur().isBlank())
            veloExistante.setCouleur(velo.getCouleur());
        if (velo.getTailleCadre() != null && velo.getTailleCadre() > 0)
            veloExistante.setTailleCadre(velo.getTailleCadre());
        if (velo.getPoids() != null && velo.getPoids() > 0)
            veloExistante.setPoids(velo.getPoids());
    }

    private static void remplacerElectrique(Velo velo, Velo veloExistante) {
        if (velo.getElectrique() != null)
            veloExistante.setElectrique(velo.getElectrique());
        if (velo.getCapaciteBatterie() != null && velo.getCapaciteBatterie() > 0)
            veloExistante.setCapaciteBatterie(velo.getCapaciteBatterie());
        if (velo.getAutonomie() != null && velo.getAutonomie() > 0)
            veloExistante.setAutonomie(velo.getAutonomie());
        if (velo.getFreinsADisque() != null)
            veloExistante.setFreinsADisque(velo.getFreinsADisque());
    }

    private static void remplacerTarifKilometrage(Velo velo, Velo veloExistante) {
        if (velo.getTarifJournalier() > 0)
            veloExistante.setTarifJournalier(velo.getTarifJournalier());
        if (velo.getKilometrage() >= 0)
            veloExistante.setKilometrage(velo.getKilometrage());
    }

    private static void remplacerEtat(Velo velo, Velo veloExistante) {
        if (velo.getActif() != null)
            veloExistante.setActif(velo.getActif());
        if (velo.getRetireDuParc() != null)
            veloExistante.setRetireDuParc(velo.getRetireDuParc());
    }


    private static List<Velo> rechercheVelo(Long id, String marque, String modele, String couleur, Integer tailleCadre,
                                            Integer poids, Boolean electrique, Integer capaciteBatterie, Integer autonomie,
                                            Boolean freinsADisque, Long tarifJournalier, Long kilometrage, Boolean actif,
                                            Boolean retireDuParc, List<Velo> liste) throws VehiculeException {
        log.debug("Initial list size: {}", liste.size());
        liste = filtrerParId(id, liste);
        liste = filtrerParMarque(marque, liste);
        liste = filtrerParModele(modele, liste);
        liste = filtrerParCouleur(couleur, liste);
        liste = filtrerParTailleCadre(tailleCadre, liste);
        liste = filtrerParPoids(poids, liste);
        liste = filtrerParElectrique(electrique, liste);
        liste = filtrerParCapaciteBatterie(capaciteBatterie, liste);
        liste = filtrerParAutonomie(autonomie, liste);
        liste = filtrerParFreinsADisque(freinsADisque, liste);
        liste = filtrerParTarifJournalier(tarifJournalier, liste);
        liste = filtrerParKilometrage(kilometrage, liste);
        liste = filtrerParActif(actif, liste);
        liste = filtrerParRetireDuParc(retireDuParc, liste);

        if (liste.isEmpty()) {
            throw new VehiculeException("Un critère de recherche est obligatoire !");
        }
        return liste;
    }

    private static List<Velo> filtrerParId(Long id, List<Velo> liste) {
        if (id != null && id != 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getId() == id)
                    .toList();
            log.debug("List size after filtering by ID: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParMarque(String marque, List<Velo> liste) {
        if (marque != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getMarque().contains(marque))
                    .toList();
            log.debug("List size after filtering by marque: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParModele(String modele, List<Velo> liste) {
        if (modele != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getModele().contains(modele))
                    .toList();
            log.debug("List size after filtering by modele: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParCouleur(String couleur, List<Velo> liste) {
        if (couleur != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getCouleur().contains(couleur))
                    .toList();
            log.debug("List size after filtering by couleur: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParTailleCadre(Integer tailleCadre, List<Velo> liste) {
        if (tailleCadre != null && tailleCadre > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getTailleCadre().equals(tailleCadre))
                    .toList();
            log.debug("List size after filtering by tailleCadre: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParPoids(Integer poids, List<Velo> liste) {
        if (poids != null && poids > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getPoids().equals(poids))
                    .toList();
            log.debug("List size after filtering by poids: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParElectrique(Boolean electrique, List<Velo> liste) {
        if (electrique != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getElectrique().equals(electrique))
                    .toList();
            log.debug("List size after filtering by electrique: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParCapaciteBatterie(Integer capaciteBatterie, List<Velo> liste) {
        if (capaciteBatterie != null && capaciteBatterie > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getCapaciteBatterie().equals(capaciteBatterie))
                    .toList();
            log.debug("List size after filtering by capaciteBatterie: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParAutonomie(Integer autonomie, List<Velo> liste) {
        if (autonomie != null && autonomie > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getAutonomie().equals(autonomie))
                    .toList();
            log.debug("List size after filtering by autonomie: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParFreinsADisque(Boolean freinsADisque, List<Velo> liste) {
        if (freinsADisque != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getFreinsADisque().equals(freinsADisque))
                    .toList();
            log.debug("List size after filtering by freinsADisque: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParTarifJournalier(Long tarifJournalier, List<Velo> liste) {
        if (tarifJournalier != null && tarifJournalier > 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getTarifJournalier() == (tarifJournalier))
                    .toList();
            log.debug("List size after filtering by tarifJournalier: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParKilometrage(Long kilometrage, List<Velo> liste) {
        if (kilometrage != null && kilometrage >= 0) {
            liste = liste.stream()
                    .filter(velo -> velo.getKilometrage() == (kilometrage))
                    .toList();
            log.debug("List size after filtering by kilometrage: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParActif(Boolean actif, List<Velo> liste) {
        if (actif != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getActif().equals(actif))
                    .toList();
            log.debug("List size after filtering by actif: {}", liste.size());
        }
        return liste;
    }

    private static List<Velo> filtrerParRetireDuParc(Boolean retireDuParc, List<Velo> liste) {
        if (retireDuParc != null) {
            liste = liste.stream()
                    .filter(velo -> velo.getRetireDuParc().equals(retireDuParc))
                    .toList();
            log.debug("List size after filtering by retireDuParc: {}", liste.size());
        }
        return liste;
    }
}
