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


@Service
public class VoitureServiceImpl implements VoitureService {

    private final VoitureDao voitureDao;
    private final VoitureMapper voitureMapper;
    private static final Logger logger = LoggerFactory.getLogger(VoitureServiceImpl.class);


    public VoitureServiceImpl(VoitureDao voitureDao, VoitureMapper voitureMapper) {
        this.voitureDao = voitureDao;
        this.voitureMapper = voitureMapper;
    }

    @Override
    public VoitureResponseDto ajouter(VoitureRequestDto voitureRequestDto) throws VehiculeException {
        verifVoiture(voitureRequestDto);

        Voiture voiture = voitureMapper.toVoiture(voitureRequestDto);

        Voiture voitureEnreg = voitureDao.save(voiture);
        return voitureMapper.toVoitureResponseDto(voitureEnreg);
    }

    @Override
    public VoitureResponseDto trouver(long id) throws EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDao.findById(id);
        if (optVoiture.isEmpty())
            throw new EntityNotFoundException("Erreur, aucune voiture trouvée avec cet id");
        Voiture voiture = optVoiture.get();
        return voitureMapper.toVoitureResponseDto(voiture);
    }

    @Override
    public List<VoitureResponseDto> trouverToutes() {
        return voitureDao.findAll().stream()
                .map(voitureMapper::toVoitureResponseDto)
                .toList();
    }


    @Override
    public VoitureResponseDto modifierPartiellement(Long id, VoitureRequestDto voitureRequestDto) throws VehiculeException, EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDao.findById(id);
        if (optVoiture.isEmpty())
            throw new VehiculeException("Erreur, l'identifiant ne correspond à aucune voiture en base");
        Voiture voitureExistante = optVoiture.get();

        voitureRequestDto = voitureMapper.toVoitureRequestDto(voitureExistante);
        verifVoiture(voitureRequestDto);

        Voiture nouvelle = voitureMapper.toVoiture(voitureRequestDto);
        remplacer(nouvelle, voitureExistante);



        Voiture voitureEnreg = voitureDao.save(voitureExistante);
        return getVoitureResponseDto(voitureEnreg);
    }

    @Override
    public VoitureResponseDto getVoitureResponseDto(Voiture voitureEnreg) {
        return voitureMapper.toVoitureResponseDto(voitureEnreg);
    }

    @Override
    public void supprimer(Long id) throws EntityNotFoundException {
        if (voitureDao.existsById(id))
            voitureDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucune voiture n'est enregistrée sous cet identifiant");
    }

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
                .sorted(Comparator.comparing(Voiture::getModele))
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

    private static void remplacer(Voiture voitureRequestDto, Voiture voitureRequestDtoExistante) {
        if (voitureRequestDto.getMarque() != null)
            voitureRequestDtoExistante.setMarque(voitureRequestDto.getMarque());
        if (voitureRequestDto.getModele() != null)
            voitureRequestDtoExistante.setModele(voitureRequestDto.getModele());
        if (voitureRequestDto.getCouleur() != null)
            voitureRequestDtoExistante.setCouleur(voitureRequestDto.getCouleur());
        if (voitureRequestDto.getNombreDePlaces() == null ||voitureRequestDto.getNombreDePlaces() <= 0)
            voitureRequestDtoExistante.setNombreDePlaces(voitureRequestDto.getNombreDePlaces());
        if (voitureRequestDto.getCarburant() != null)
            voitureRequestDtoExistante.setCarburant(voitureRequestDto.getCarburant());

        if (voitureRequestDto.getNombreDePortes() == null || voitureRequestDto.getNombreDePortes() <= 0)
            voitureRequestDtoExistante.setNombreDePortes(voitureRequestDto.getNombreDePortes());
        if (voitureRequestDto.getTransmission() != null)
            voitureRequestDtoExistante.setTransmission(voitureRequestDto.getTransmission());
        if (voitureRequestDto.getClim() != null)
            voitureRequestDtoExistante.setClim(voitureRequestDto.getClim());
        if (voitureRequestDto.getNombreDeBagages() == null ||voitureRequestDto.getNombreDeBagages() < 0)
            voitureRequestDtoExistante.setNombreDeBagages(voitureRequestDto.getNombreDeBagages());
        if (voitureRequestDto.getType() != null)
            voitureRequestDtoExistante.setType(voitureRequestDto.getType());
        if (voitureRequestDto.getListePermis() != null && !voitureRequestDto.getListePermis().isEmpty())
            voitureRequestDtoExistante.setListePermis(voitureRequestDto.getListePermis());
        if (voitureRequestDto.getTarifJournalier() > 0)
            voitureRequestDtoExistante.setTarifJournalier(voitureRequestDto.getTarifJournalier());
        if (voitureRequestDto.getKilometrage() >= 0)
            voitureRequestDtoExistante.setKilometrage(voitureRequestDto.getKilometrage());
        if (voitureRequestDto.getActif() != null)
            voitureRequestDtoExistante.setActif(voitureRequestDto.getActif());
        if (voitureRequestDto.getRetireDuParc() != null)
            voitureRequestDtoExistante.setRetireDuParc(voitureRequestDto.getRetireDuParc());
    }

    private static List<Voiture> rechercheVoiture(Long id, String marque, String modele, String couleur, Integer nombreDePlaces, Carburant carburant,
                                                  Integer nombreDePortes, String transmission, Boolean clim, Integer nombreDeBagages, String type,
                                                  List<Permis> listePermis, Long tarifJournalier, Long kilometrage, Boolean actif, Boolean retireDuParc,
                                                  List<Voiture> liste) throws VehiculeException{
        if (id != null && id != 0) {
                liste = liste.stream()
                        .filter(voiture -> voiture.getId() == id)
                        .collect(Collectors.toList());
        }
        if (marque != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getMarque().contains(marque))
                    .collect(Collectors.toList());
        }
        if (modele != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getModele().contains(modele))
                    .collect(Collectors.toList());
        }
        if (couleur != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getCouleur().contains(couleur))
                    .collect(Collectors.toList());
        }
        if (nombreDePlaces != null  && nombreDePlaces > 0 ) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getNombreDePlaces().equals(nombreDePlaces))
                    .collect(Collectors.toList());
        }
        if (carburant != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getCarburant().equals(carburant))
                    .collect(Collectors.toList());
        }
        if (nombreDePortes != null  && nombreDePortes > 0 ) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getNombreDePortes().equals(nombreDePortes))
                    .collect(Collectors.toList());
        }
        if (transmission != null && (transmission.equals("automatique") || transmission.equals("manuelle"))) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getTransmission().contains(transmission))
                    .collect(Collectors.toList());
        }
        if (clim != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getClim().equals(clim))
                    .collect(Collectors.toList());
        }
        if (nombreDeBagages!= null  && nombreDeBagages >= 0 ) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getNombreDeBagages().equals(nombreDeBagages))
                    .collect(Collectors.toList());
        }
        if (type != null && (type.equals("Citadine") || type.equals("Berline") || type.equals("SUV") || type.equals("Familiales") || type.equals("Voiture électrique") || type.equals("Voiture de luxe"))) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getType().contains(type))
                    .collect(Collectors.toList());
        }
        if (listePermis != null && !listePermis.isEmpty()) {
            Permis permis = listePermis.get(0);
            liste = liste.stream()
                    .filter(voiture -> voiture.getListePermis().contains(permis))
                    .collect(Collectors.toList());
        }
        if (tarifJournalier != null && tarifJournalier> 0) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getTarifJournalier() == tarifJournalier)
                    .collect(Collectors.toList());
        }
        if (kilometrage != null && kilometrage >= 0) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getKilometrage() == kilometrage)
                    .collect(Collectors.toList());
        }
        if (actif != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getActif().equals(actif))
                    .collect(Collectors.toList());
        }
        if (retireDuParc != null) {
            liste = liste.stream()
                    .filter(voiture -> voiture.getRetireDuParc().equals(retireDuParc))
                    .collect(Collectors.toList());
        }
        if (liste.isEmpty()) {
            throw new VehiculeException("Un critère de recherche est obligatoire !");
        }
        return liste;
    }
}
