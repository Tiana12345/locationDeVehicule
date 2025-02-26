package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j


/**
 * @author tatiana.tessier
 * Classe d'implémentation du service de gestion des voitures.
 * Cette classe fournit des méthodes pour ajouter, trouver, modifier, et supprimer des voitures,
 * ainsi que pour rechercher des voitures en fonction de plusieurs critères.
 */
@Service
public class VoitureServiceImpl implements VoitureService {

    private final VoitureDao voitureDao;
    private final VoitureMapper voitureMapper;
    private static final Logger logger = LoggerFactory.getLogger(VoitureServiceImpl.class);

    /**
     * Constructeur de la classe VoitureServiceImpl.
     *
     * @param voitureDao l'objet DAO pour accéder aux données des voitures
     * @param voitureMapper l'objet Mapper pour convertir entre les entités Voiture et les DTO
     */

    public VoitureServiceImpl(VoitureDao voitureDao, VoitureMapper voitureMapper) {
        this.voitureDao = voitureDao;
        this.voitureMapper = voitureMapper;
    }

    /**
     * Méthode servant à ajouter une voiture.
     * Cette méthode prend un objet VoitureRequestDto, vérifie sa validité,
     * le convertit en entité Voiture, l'enregistre dans la base de données,
     * puis retourne un objet VoitureResponseDto contenant les informations de la voiture ajoutée.
     *
     * @param voitureRequestDto l'objet contenant les informations de la voiture à ajouter
     *
     * @return un objet VoitureResponseDto contenant les informations de la voiture ajoutée
     *         -
     * @throws VehiculeException si une erreur survient lors de l'ajout de la voiture,
     *                           par exemple si les informations de la voiture sont invalides
     */
    @Override
    public VoitureResponseDto ajouter(VoitureRequestDto voitureRequestDto) throws VehiculeException {
        verifVoiture(voitureRequestDto);

        Voiture voiture = voitureMapper.toVoiture(voitureRequestDto);

        Voiture voitureEnreg = voitureDao.save(voiture);
        return voitureMapper.toVoitureResponseDto(voitureEnreg);
    }

    /**
     * Méthode servant à trouver une voiture par son identifiant.
     * Cette méthode recherche une voiture dans la base de données à partir de son identifiant.
     * Si aucune voiture n'est trouvée, une exception EntityNotFoundException est levée.
     *
     * @param "l'identifiant unique de la voiture à trouver"
     * @return un objet VoitureResponseDto contenant les informations de la voiture trouvée
     *         -
     * @throws EntityNotFoundException si aucune voiture n'est trouvée avec l'identifiant fourni
     */
    @Override
    public VoitureResponseDto trouver(long id) throws EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDao.findById(id);
        if (optVoiture.isEmpty())
            throw new EntityNotFoundException("Erreur, aucune voiture trouvée avec cet id");
        Voiture voiture = optVoiture.get();
        return voitureMapper.toVoitureResponseDto(voiture);
    }

    /**
     * Méthode servant à trouver toutes les voitures.
     * Cette méthode récupère toutes les voitures de la base de données,
     * les convertit en objets VoitureResponseDto et retourne la liste de ces objets.
     *
     * @return une liste d'objets VoitureResponseDto contenant les informations de toutes les voitures
     *
     */
    @Override
    public List<VoitureResponseDto> trouverToutes() {
        return voitureDao.findAll().stream()
                .map(voitureMapper::toVoitureResponseDto)
                .toList();
    }

    /**
     * Méthode servant à modifier partiellement une voiture existante.
     * Cette méthode recherche une voiture dans la base de données à partir de son identifiant.
     * Si aucune voiture n'est trouvée, une exception EntityNotFoundException est levée.
     * Si l'identifiant est valide, les informations de la voiture sont partiellement mises à jour
     * avec les données fournies dans VoitureRequestDto.
     *
     * @param id l'identifiant unique de la voiture à modifier
     * @param voitureRequestDto l'objet contenant les informations à mettre à jour
     *
     * @return un objet VoitureResponseDto contenant les informations de la voiture modifiée
     *
     * @throws VehiculeException si une erreur survient lors de la modification de la voiture,
     *                           par exemple si l'identifiant ne correspond à aucune voiture en base
     * @throws EntityNotFoundException si aucune voiture n'est trouvée avec l'identifiant fourni
     */

    @Override
    public VoitureResponseDto modifierPartiellement(Long id, VoitureRequestDto voitureRequestDto) throws VehiculeException, EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDao.findById(id);
        if (optVoiture.isEmpty())
            throw new VehiculeException("Erreur, l'identifiant ne correspond à aucune voiture en base");
        Voiture voitureExistante = optVoiture.get();



        Voiture nouvelle = voitureMapper.toVoiture(voitureRequestDto);
        remplacer(nouvelle, voitureExistante);
//
//        voitureRequestDto = voitureMapper.toVoitureRequestDto(voitureExistante);
//        verifVoiture(voitureRequestDto);

        Voiture voitureEnreg = voitureDao.save(voitureExistante);
        return getVoitureResponseDto(voitureEnreg);
    }

    /**
     * Méthode servant à convertir une entité Voiture en un objet VoitureResponseDto.
     * Cette méthode utilise le mapper pour transformer une entité Voiture enregistrée
     * en un objet VoitureResponseDto contenant les informations de la voiture.
     *
     * @param voitureEnreg l'entité Voiture à convertir
     *
     * @return un objet VoitureResponseDto contenant les informations de la voiture
     *
     */
    @Override
    public VoitureResponseDto getVoitureResponseDto(Voiture voitureEnreg) {
        return voitureMapper.toVoitureResponseDto(voitureEnreg);
    }

    /**
     * Méthode servant à supprimer une voiture par son identifiant.
     * Cette méthode vérifie si une voiture existe dans la base de données avec l'identifiant fourni.
     * Si la voiture existe, elle est supprimée de la base de données.
     * Si aucune voiture n'est trouvée, une exception EntityNotFoundException est levée.
     *
     * @param id l'identifiant unique de la voiture à supprimer
     * @throws EntityNotFoundException si aucune voiture n'est trouvée avec l'identifiant fourni
     */
    @Override
    public void supprimer(Long id) throws EntityNotFoundException {
        if (voitureDao.existsById(id))
            voitureDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucune voiture n'est enregistrée sous cet identifiant");
    }

    /**
     * Méthode servant à rechercher des voitures en fonction de plusieurs critères.
     * Cette méthode récupère toutes les voitures de la base de données, vérifie les objets nécessaires,
     * applique les critères de recherche fournis, trie les résultats par modèle, et retourne une liste
     * d'objets VoitureResponseDto correspondant aux critères.
     *
     * @return une liste d'objets VoitureResponseDto correspondant aux critères de recherche
     *
     * @throws VehiculeException si une erreur survient lors de la recherche, par exemple si les objets nécessaires ne sont pas initialisés
     */
    @Override
    public List<VoitureResponseDto> rechercher(Long id, String marque, String modele, String couleur, Integer nombreDePlaces,
                                               Carburant carburant, Integer nombreDePortes, String transmission, Boolean clim,
                                               Integer nombreDeBagages, String type, List<Permis> listePermis, Long tarifJournalier,
                                               Long kilometrage, Boolean actif, Boolean retireDuParc) {

        List < Voiture > liste = voitureDao.findAll();

        if (liste == null) {
            throw new VehiculeException("La méthode findAll a retourné null !");
        }
        if (voitureDao == null) {
            throw new VehiculeException("voitureDao n'est pas initialisé !");
        }
        if (voitureMapper == null) {
            throw new VehiculeException("voitureMapper n'est pas initialisé !");
        }

        liste = rechercheVoiture(id, marque, modele, couleur, nombreDePlaces, carburant, nombreDePortes, transmission, clim, nombreDeBagages, type, listePermis, tarifJournalier, kilometrage, actif, retireDuParc, liste);

        return liste.stream()
               // .sorted(Comparator.comparing(Voiture::getModele))
                .map(voitureMapper::toVoitureResponseDto)
                .collect(Collectors.toList());
    }


    //__________________________________________________________________________________________________________
    private static void verifVoiture(VoitureRequestDto voitureRequestDto) {
        if (voitureRequestDto == null)
            throw new VehiculeException("La voitureRequestDto est null");
        if (voitureRequestDto.marque() == null || voitureRequestDto.marque().isBlank())
            throw new VehiculeException("Vous devez ajouter la marque de la voiture");
        if (voitureRequestDto.modele() == null || voitureRequestDto.modele().isBlank())
            throw new VehiculeException("Vous devez ajouter le modèle de la voiture");
        if (voitureRequestDto.couleur() == null || voitureRequestDto.couleur().isBlank())
            throw new VehiculeException("Vous devez ajouter la couleur de la voiture");
        if (voitureRequestDto.nombreDePlaces() == null || voitureRequestDto.nombreDePlaces() <= 0)
            throw new VehiculeException("Vous devez ajouter le nombre de places de la voiture");
        if (voitureRequestDto.carburant() == null)
            throw new VehiculeException("Vous devez ajouter le type de carburant de la voiture");
        if (voitureRequestDto.nombreDePortes() == null ||voitureRequestDto.nombreDePortes() <= 0)
            throw new VehiculeException("Vous devez ajouter le nombre de portes de la voiture");
        if (voitureRequestDto.transmission() == null || voitureRequestDto.transmission().isBlank())
            throw new VehiculeException("Vous devez ajouter la transmission de la voiture");
        if (voitureRequestDto.clim()== null)
            throw new VehiculeException("Vous devez ajouter l'information concernant la clim");
        if (voitureRequestDto.nombreDeBagages() == null ||voitureRequestDto.nombreDeBagages() < 0)
            throw new VehiculeException("Vous devez ajouter le nombre de bagages de la voiture");
        if (voitureRequestDto.type() == null || voitureRequestDto.type().isBlank())
            throw new VehiculeException("Vous devez ajouter le type de la voiture");
        if (voitureRequestDto.listePermis() == null || voitureRequestDto.listePermis().isEmpty())
            throw new VehiculeException("Vous devez ajouter les permis requis pour la voiture");
        if (voitureRequestDto.tarifJournalier() <= 0)
            throw new VehiculeException("Vous devez ajouter le tarif journalier de la voiture");
        if (voitureRequestDto.kilometrage() < 0)
            throw new VehiculeException("Vous devez ajouter le kilométrage de la voiture");
        if (voitureRequestDto.actif() == null)
            throw new VehiculeException("Vous devez indiquer si la voiture est active");
        if (voitureRequestDto.retireDuParc() == null)
            throw new VehiculeException("Vous devez indiquer si la voiture est retirée du parc");
    }

    private static void remplacer(Voiture voiture, Voiture voitureExistante) {
        if ( voiture.getMarque() != null && voiture.getMarque().isBlank())
            voitureExistante.setMarque(voiture.getMarque());
        if (voiture.getModele() != null && voiture.getModele().isBlank())
            voitureExistante.setModele(voiture.getModele());
        if (voiture.getCouleur() != null && voiture.getCouleur().isBlank())
            voitureExistante.setCouleur(voiture.getCouleur());
        if (voiture.getNombreDePlaces() == null ||voiture.getNombreDePlaces() <= 0)
            voitureExistante.setNombreDePlaces(voiture.getNombreDePlaces());
        if (voiture.getCarburant() != null )
            voitureExistante.setCarburant(voiture.getCarburant());
        if (voiture.getNombreDePortes() == null || voiture.getNombreDePortes() <= 0)
            voitureExistante.setNombreDePortes(voiture.getNombreDePortes());
        if (voiture.getTransmission() != null && voiture.getTransmission().isBlank())
            voitureExistante.setTransmission(voiture.getTransmission());
        if (voiture.getClim() != null)
            voitureExistante.setClim(voiture.getClim());
        if (voiture.getNombreDeBagages() == null ||voiture.getNombreDeBagages() < 0)
            voitureExistante.setNombreDeBagages(voiture.getNombreDeBagages());
        if (voiture.getType() != null && voiture.getType().isBlank())
            voitureExistante.setType(voiture.getType());
        if (voiture.getListePermis() != null && !voiture.getListePermis().isEmpty())
            voitureExistante.setListePermis(voiture.getListePermis());
        if (voiture.getTarifJournalier() > 0)
            voitureExistante.setTarifJournalier(voiture.getTarifJournalier());
        if (voiture.getKilometrage() >= 0)
            voitureExistante.setKilometrage(voiture.getKilometrage());
        if (voiture.getActif() != null)
            voitureExistante.setActif(voiture.getActif());
        if (voiture.getRetireDuParc() != null)
            voitureExistante.setRetireDuParc(voiture.getRetireDuParc());
    }

    private static List<Voiture> rechercheVoiture(Long id, String marque, String modele, String couleur, Integer nombreDePlaces, Carburant carburant,
                                                  Integer nombreDePortes, String transmission, Boolean clim, Integer nombreDeBagages, String type,
                                                  List<Permis> listePermis, Long tarifJournalier, Long kilometrage, Boolean actif, Boolean retireDuParc,
                                                  List<Voiture> liste) throws VehiculeException{
        logger.debug("Initial list size: {}", liste.size());
        if (id != null && id != 0) {
                liste = liste.stream()
                        .filter(voiture -> voiture.getId() == id)
                        .collect(Collectors.toList());
            logger.debug("List size after filtering by ID: {}", liste.size());
        }
        if (marque != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getMarque().contains(marque))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by marque: {}", liste.size());
        }
        if (modele != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getModele().contains(modele))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by modele: {}", liste.size());
        }
        if (couleur != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getCouleur().contains(couleur))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by couleur: {}", liste.size());
        }
        if (nombreDePlaces != null  && nombreDePlaces > 0 ) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getNombreDePlaces().equals(nombreDePlaces))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by nombreDePlaces: {}", liste.size());
        }
        if (carburant != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getCarburant().equals(carburant))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by carburant: {}", liste.size());
        }
        if (nombreDePortes != null  && nombreDePortes > 0 ) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getNombreDePortes().equals(nombreDePortes))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by nombreDePortes: {}", liste.size());
        }
        if (transmission != null && (transmission.equals("automatique") || transmission.equals("manuelle"))) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getTransmission().contains(transmission))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by transmission: {}", liste.size());

        }
        if (clim != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getClim().equals(clim))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by clim: {}", liste.size());

        }
        if (nombreDeBagages!= null  && nombreDeBagages >= 0 ) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getNombreDeBagages().equals(nombreDeBagages))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by nombreDeBagages: {}", liste.size());

        }
        if (type != null && (type.equals("Citadine") || type.equals("Berline") || type.equals("SUV") || type.equals("Familiales") || type.equals("Voiture électrique") || type.equals("Voiture de luxe"))) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getType().contains(type))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by type: {}", liste.size());

        }
        if (listePermis != null && !listePermis.isEmpty()) {
            Permis permis = listePermis.get(0);
            liste = liste.stream()
                    .filter(voiture -> voiture.getListePermis().contains(permis))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by permis: {}", liste.size());

        }
        if (tarifJournalier != null && tarifJournalier> 0) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getTarifJournalier() == tarifJournalier)
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by tarifjournalier: {}", liste.size());

        }
        if (kilometrage != null && kilometrage >= 0) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getKilometrage() == kilometrage)
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by kilometrage: {}", liste.size());

        }
        if (actif != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getActif().equals(actif))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by actif: {}", liste.size());

        }
        if (retireDuParc != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getRetireDuParc().equals(retireDuParc))
                    .collect(Collectors.toList());
            logger.debug("List size after filtering by RetireDuParc: {}", liste.size());

        }
        if (liste.isEmpty()) {
            throw new VehiculeException("Un critère de recherche est obligatoire !");
        }
        return liste;
    }
}
