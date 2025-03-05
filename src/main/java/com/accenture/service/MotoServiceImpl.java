package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.param.Permis;
import com.accenture.repository.MotoDao;
import com.accenture.repository.entity.Moto;
import com.accenture.service.dto.MotoRequestDto;
import com.accenture.service.dto.MotoResponseDto;
import com.accenture.service.mapper.MotoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe d'implémentation du service de gestion des motos.
 * Cette classe fournit des méthodes pour ajouter, trouver, modifier, et supprimer des motos,
 * ainsi que pour rechercher des motos en fonction de plusieurs critères.
 */

@Service
public class MotoServiceImpl implements MotoService {

    private final MotoDao motoDao;
    private final MotoMapper motoMapper;
    private static final Logger logger = LoggerFactory.getLogger(MotoServiceImpl.class);

    public MotoServiceImpl(MotoDao motoDao, MotoMapper motoMapper) {
        this.motoDao = motoDao;
        this.motoMapper = motoMapper;
    }

    /**
     * Ajouter une nouvelle moto.
     *
     * @param motoRequestDto l'objet contenant les informations de la moto à ajouter
     * @return un objet MotoResponseDto contenant les informations de la moto ajoutée
     * @throws VehiculeException si une erreur survient lors de l'ajout de la moto
     */

    @Override
    public MotoResponseDto ajouter(MotoRequestDto motoRequestDto) throws VehiculeException {
        verifMoto(motoRequestDto);

        Moto moto = motoMapper.toMoto(motoRequestDto);
        if (moto.getPuissanceEnkW() <= 11) {
            moto.setPermis(Permis.A1);
        } else if (moto.getPuissanceEnkW() > 11 && moto.getPuissanceEnkW() < 35) {
            moto.setPermis(Permis.A2);
        } else {
            moto.setPermis(Permis.A);
        }
        Moto motoEnreg = motoDao.save(moto);
        return motoMapper.toMotoResponseDto(motoEnreg);
    }

    /**
     * Trouver une moto par son identifiant.
     *
     * @param id l'identifiant unique de la moto à trouver
     * @return un objet MotoResponseDto contenant les informations de la moto trouvée
     * @throws EntityNotFoundException si aucune moto n'est trouvée avec l'identifiant fourni
     */

    @Override
    public MotoResponseDto trouver(long id) throws EntityNotFoundException {
        Optional<Moto> optMoto = motoDao.findById(id);
        if (optMoto.isEmpty())
            throw new EntityNotFoundException("Erreur, aucune moto trouvée avec cet id");
        Moto moto = optMoto.get();
        return motoMapper.toMotoResponseDto(moto);
    }

    /**
     * Trouver toutes les motos.
     *
     * @return une liste d'objets MotoResponseDto contenant les informations de toutes les motos
     */

    @Override
    public List<MotoResponseDto> trouverToutes() {
        return motoDao.findAll().stream()
                .map(motoMapper::toMotoResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Modifier partiellement une moto.
     *
     * @param id             l'identifiant unique de la moto à modifier
     * @param motoRequestDto l'objet contenant les informations de la moto à modifier
     * @return un objet MotoResponseDto contenant les informations de la moto modifiée
     * @throws VehiculeException       si une erreur survient lors de la modification de la moto
     * @throws EntityNotFoundException si aucune moto n'est trouvée avec l'identifiant fourni
     */

    @Override
    public MotoResponseDto modifierPartiellement(Long id, MotoRequestDto motoRequestDto) throws VehiculeException, EntityNotFoundException {
        Optional<Moto> optMoto = motoDao.findById(id);
        if (optMoto.isEmpty())
            throw new VehiculeException("Erreur, l'identifiant ne correspond à aucune moto en base");
        Moto motoExistante = optMoto.get();

        Moto nouvelle = motoMapper.toMoto(motoRequestDto);
        remplacer(nouvelle, motoExistante);

        Moto motoEnreg = motoDao.save(motoExistante);
        return getMotoResponseDto(motoEnreg);
    }

    /**
     * Convertir une entité Moto en un objet MotoResponseDto.
     *
     * @param motoEnreg l'entité Moto à convertir
     * @return un objet MotoResponseDto contenant les informations de la moto
     */

    @Override
    public MotoResponseDto getMotoResponseDto(Moto motoEnreg) {
        return motoMapper.toMotoResponseDto(motoEnreg);
    }

    /**
     * Supprimer une moto par son identifiant.
     *
     * @param id l'identifiant unique de la moto à supprimer
     * @throws EntityNotFoundException si aucune moto n'est trouvée avec l'identifiant fourni
     */

    @Override
    public void supprimer(Long id) throws EntityNotFoundException {
        if (motoDao.existsById(id))
            motoDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucune moto n'est enregistrée sous cet identifiant");
    }

    /**
     * Rechercher des motos par critères.
     *
     * @param id              l'identifiant de la moto
     * @param marque          la marque de la moto
     * @param modele          le modèle de la moto
     * @param couleur         la couleur de la moto
     * @param nombreCylindres le nombre de cylindres de la moto
     * @param poids           le poids de la moto
     * @param puissanceEnkW   la puissance en kW de la moto
     * @param hauteurSelle    la hauteur de selle de la moto
     * @param transmission    la transmission de la moto
     * @param permis          permis requis pour la moto
     * @param tarifJournalier le tarif journalier de la moto
     * @param kilometrage     le kilométrage de la moto
     * @param actif           la moto est-elle active
     * @param retireDuParc    la moto est-elle retirée du parc
     * @return la liste des motos correspondant aux critères
     */

    @Override
    public List<MotoResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer nombreCylindres,
                                            Integer poids, Integer puissanceEnkW, Integer hauteurSelle, String transmission,
                                            Permis permis, Long tarifJournalier, Long kilometrage, Boolean actif,
                                            Boolean retireDuParc) {

        List<Moto> liste = motoDao.findAll();


        liste = rechercheMoto(id, marque, modele, couleur, nombreCylindres, poids, puissanceEnkW, hauteurSelle,
                transmission, permis, tarifJournalier, kilometrage, actif, retireDuParc, liste);

        return liste.stream()
                .map(motoMapper::toMotoResponseDto)
                .collect(Collectors.toList());
    }

    private static void verifMoto(MotoRequestDto motoRequestDto) {
        if (motoRequestDto == null)
            throw new VehiculeException("Le motoRequestDto est null");
        if (motoRequestDto.marque() == null || motoRequestDto.marque().isBlank())
            throw new VehiculeException("Vous devez ajouter la marque de la moto");
        if (motoRequestDto.modele() == null || motoRequestDto.modele().isBlank())
            throw new VehiculeException("Vous devez ajouter le modèle de la moto");
        if (motoRequestDto.couleur() == null || motoRequestDto.couleur().isBlank())
            throw new VehiculeException("Vous devez ajouter la couleur de la moto");
        if (motoRequestDto.nombreCylindres() == null || motoRequestDto.nombreCylindres() <= 0)
            throw new VehiculeException("Vous devez ajouter le nombre de cylindres de la moto");
        if (motoRequestDto.poids() == null || motoRequestDto.poids() <= 0)
            throw new VehiculeException("Vous devez ajouter le poids de la moto");
        if (motoRequestDto.puissanceEnkW() == null || motoRequestDto.puissanceEnkW() <= 0)
            throw new VehiculeException("Vous devez ajouter la puissance en kW de la moto");
        if (motoRequestDto.hauteurSelle() == null || motoRequestDto.hauteurSelle() <= 0)
            throw new VehiculeException("Vous devez ajouter la hauteur de selle de la moto");
        if (motoRequestDto.transmission() == null || motoRequestDto.transmission().isBlank())
            throw new VehiculeException("Vous devez ajouter la transmission de la moto");
        if (motoRequestDto.tarifJournalier() <= 0)
            throw new VehiculeException("Vous devez ajouter le tarif journalier de la moto");
        if (motoRequestDto.kilometrage() < 0)
            throw new VehiculeException("Vous devez ajouter le kilométrage de la moto");
        if (motoRequestDto.actif() == null)
            throw new VehiculeException("Vous devez indiquer si la moto est active");
        if (motoRequestDto.retireDuParc() == null)
            throw new VehiculeException("Vous devez indiquer si la moto est retirée du parc");
    }

    private static void remplacer(Moto moto, Moto motoExistante) {
        if (moto.getMarque() != null && !moto.getMarque().isBlank())
            motoExistante.setMarque(moto.getMarque());
        if (moto.getModele() != null && !moto.getModele().isBlank())
            motoExistante.setModele(moto.getModele());
        if (moto.getCouleur() != null && !moto.getCouleur().isBlank())
            motoExistante.setCouleur(moto.getCouleur());
        if (moto.getNombreCylindres() != null && moto.getNombreCylindres() > 0)
            motoExistante.setNombreCylindres(moto.getNombreCylindres());
        if (moto.getPoids() != null && moto.getPoids() > 0)
            motoExistante.setPoids(moto.getPoids());
        if (moto.getPuissanceEnkW() != null && moto.getPuissanceEnkW() > 0)
            motoExistante.setPuissanceEnkW(moto.getPuissanceEnkW());
        if (moto.getHauteurSelle() != null && moto.getHauteurSelle() > 0)
            motoExistante.setHauteurSelle(moto.getHauteurSelle());
        if (moto.getTransmission() != null && !moto.getTransmission().isBlank())
            motoExistante.setTransmission(moto.getTransmission());
        if (moto.getPermis() != null)
            motoExistante.setPermis(moto.getPermis());
        if (moto.getTarifJournalier() > 0)
            motoExistante.setTarifJournalier(moto.getTarifJournalier());
        if (moto.getKilometrage() >= 0)
            motoExistante.setKilometrage(moto.getKilometrage());
        if (moto.getActif() != null)
            motoExistante.setActif(moto.getActif());
        if (moto.getRetireDuParc() != null)
            motoExistante.setRetireDuParc(moto.getRetireDuParc());
    }

    private static List<Moto> rechercheMoto(Long id, String marque, String modele, String couleur, Integer nombreCylindres,
                                            Integer poids, Integer puissanceEnkW, Integer hauteurSelle, String transmission,
                                           Permis permis, Long tarifJournalier, Long kilometrage, Boolean actif,
                                            Boolean retireDuParc, List<Moto> liste) throws VehiculeException {
        logger.debug("Initial list size: {}", liste.size());
        if (id != null && id != 0) {
            liste = liste.stream()
                    .filter(moto -> moto.getId() == id)
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by ID: {}", liste.size());
        }
        if (marque != null) {
            liste = liste.stream()
                    .filter(moto -> moto.getMarque().contains(marque))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by marque: {}", liste.size());
        }
        if (modele != null) {
            liste = liste.stream()
                    .filter(moto -> moto.getModele().contains(modele))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by modele: {}", liste.size());
        }
        if (couleur != null) {
            liste = liste.stream()
                    .filter(moto -> moto.getCouleur().contains(couleur))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by couleur: {}", liste.size());
        }
        if (nombreCylindres != null && nombreCylindres > 0) {
            liste = liste.stream()
                    .filter(moto -> moto.getNombreCylindres().equals(nombreCylindres))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by nombreCylindres: {}", liste.size());
        }
        if (poids != null && poids > 0) {
            liste = liste.stream()
                    .filter(moto -> moto.getPoids().equals(poids))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by poids: {}", liste.size());
        }
        if (puissanceEnkW != null && puissanceEnkW > 0) {
            liste = liste.stream()
                    .filter(moto -> moto.getPuissanceEnkW().equals(puissanceEnkW))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by puissanceEnkW: {}", liste.size());
        }
        if (hauteurSelle != null && hauteurSelle > 0) {
            liste = liste.stream()
                    .filter(moto -> moto.getHauteurSelle().equals(hauteurSelle))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by hauteurSelle: {}", liste.size());
        }
        if (transmission != null && (transmission.equals("automatique") || transmission.equals("manuelle"))) {
            liste = liste.stream()
                    .filter(moto -> moto.getTransmission().contains(transmission))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by transmission: {}", liste.size());
        }
        if (permis != null ) {
            liste = liste.stream()
                    .filter(moto -> moto.getPermis()==(permis))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by permis: {}", liste.size());
        }
        if (tarifJournalier != null && tarifJournalier > 0) {
            liste = liste.stream()
                    .filter(moto -> moto.getTarifJournalier() == (tarifJournalier))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by tarifJournalier: {}", liste.size());
        }
        if (kilometrage != null && kilometrage >= 0) {
            liste = liste.stream()
                    .filter(moto -> moto.getKilometrage() == (kilometrage))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by kilometrage: {}", liste.size());
        }
        if (actif != null) {
            liste = liste.stream()
                    .filter(moto -> moto.getActif().equals(actif))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by actif: {}", liste.size());
        }
        if (retireDuParc != null) {
            liste = liste.stream()
                    .filter(moto -> moto.getRetireDuParc().equals(retireDuParc))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by retireDuParc: {}", liste.size());
        }
        if (liste.isEmpty()) {
            throw new VehiculeException("Un critère de recherche est obligatoire !");
        }
        return liste;
    }
}