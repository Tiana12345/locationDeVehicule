package com.accenture.service;

import com.accenture.exception.VehiculeException;
import com.accenture.model.Carburant;
import com.accenture.model.Permis;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoitureServiceImpl implements VoitureService {

    private final VoitureDao voitureDao;
    private final VoitureMapper voitureMapper;


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
    public VoitureResponseDto modifier(Long id, VoitureRequestDto voitureRequestDto) throws VehiculeException, EntityNotFoundException {
        if (!voitureDao.existsById(id))
            throw new VehiculeException("Erreur, l'identifiant ne correponds à aucune voiture. ");
        verifVoiture(voitureRequestDto);

        Voiture voiture = voitureMapper.toVoiture(voitureRequestDto);
        voiture.setId(id);
        Voiture voitureEnreg = voitureDao.save(voiture);
        return voitureMapper.toVoitureResponseDto(voitureEnreg);
    }

    @Override
    public VoitureResponseDto modifierPartiellement(Long id, VoitureRequestDto voitureRequestDto) throws VehiculeException, EntityNotFoundException {
        Optional<Voiture> optVoiture = voitureDao.findById(id);
        if (optVoiture.isEmpty())
            throw new VehiculeException("Erreur, l'identifiant ne correspond à aucune voiture en base");
        Voiture voitureExistante = optVoiture.get();

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
    public List<VoitureResponseDto> rechercher(Long id, String marque, String modele, String couleur, int nombreDePlaces, Carburant carburant, int nombreDePortes, String transmission, Boolean clim, int nombreDeBagages, String type, List<Permis> listePermis, Long tarifJournalier, Long kilometrage, Boolean actif, Boolean retireDuParc) {
        List<Voiture> liste = null;
        Optional<Voiture> optional;

        if (id != 0)
            optional = voitureDao.findById(id);
        else if (marque != null)
            liste = voitureDao.findByMarqueContaining(marque);
        else if (modele != null)
            liste = voitureDao.findByModeleContaining(modele);
        else if (couleur != null)
            liste = voitureDao.findByCouleurContaining(couleur);
        else if (nombreDePlaces != 0)
            liste = voitureDao.findByNombreDePlaces(nombreDePlaces);
        else if (carburant != null )
            liste = voitureDao.findByCarburant(carburant);
        else if (nombreDePortes == 3 || nombreDePortes == 5)
            liste = voitureDao.findByNombreDePortes(nombreDePortes);
        else if (transmission.equals("auto") || transmission.equals("manuelle"))
            liste = voitureDao.findByTransmissionContaining(transmission);
        else if (clim != null)
            liste = voitureDao.findByClim(clim);
        else if (nombreDeBagages != 0)
            liste = voitureDao.findByNombreDeBagages(nombreDeBagages);
        else if (type.equals("Citadine") || type.equals("Berline") || type.equals("SUV") || type.equals("Familiales") || type.equals("Voiture électrique") || type.equals("Voiture de luxe"))
            liste = voitureDao.findByTypeContaining(type);
        else if (listePermis != null && !listePermis.isEmpty()) {
            Permis permis = listePermis.get(0);
            liste = voitureDao.findByListePermisContaining(permis);
        } else if (tarifJournalier > 0)
            liste = voitureDao.findByTarifJournalier(tarifJournalier);
        else if (kilometrage >= 0)
            liste = voitureDao.findByKilometrage(kilometrage);
        else if (actif != null)
            liste = voitureDao.findByActif(actif);
        else if (retireDuParc != null)
            liste = voitureDao.findByRetireDuParc(retireDuParc);
        if (liste == null)
            throw new VehiculeException("Un critère de recherche est obligatoire ! ");
        return liste.stream()
                .map(voitureMapper::toVoitureResponseDto)
                .toList();
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
        if (voitureRequestDto.nombreDePlaces() <= 0)
            throw new VehiculeException("Vous devez ajouter le nombre de places de la voiture");
        if (voitureRequestDto.carburant() == null)
            throw new VehiculeException("Vous devez ajouter le type de carburant de la voiture");
        if (voitureRequestDto.nombreDePortes() <= 0)
            throw new VehiculeException("Vous devez ajouter le nombre de portes de la voiture");
        if (voitureRequestDto.transmission() == null || voitureRequestDto.transmission().isBlank())
            throw new VehiculeException("Vous devez ajouter la transmission de la voiture");
        if (voitureRequestDto.nombreDeBagages() < 0)
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
        if (voitureRequestDto.getNombreDePlaces() != 0)
            voitureRequestDtoExistante.setNombreDePlaces(voitureRequestDto.getNombreDePlaces());
        if (voitureRequestDto.getCarburant() != null)
            voitureRequestDtoExistante.setCarburant(voitureRequestDto.getCarburant());

        if (voitureRequestDto.getNombreDePortes() != 0)
            voitureRequestDtoExistante.setNombreDePortes(voitureRequestDto.getNombreDePortes());
        if (voitureRequestDto.getTransmission() != null)
            voitureRequestDtoExistante.setTransmission(voitureRequestDto.getTransmission());
        if (voitureRequestDto.getClim() != null)
            voitureRequestDtoExistante.setClim(voitureRequestDto.getClim());
        if (voitureRequestDto.getNombreDeBagages() != 0)
            voitureRequestDtoExistante.setNombreDeBagages(voitureRequestDto.getNombreDeBagages());
        if (voitureRequestDto.getType() != null)
            voitureRequestDtoExistante.setType(voitureRequestDto.getType());
        if (voitureRequestDto.getListePermis() != null && !voitureRequestDto.getListePermis().isEmpty())
            voitureRequestDtoExistante.setListePermis(voitureRequestDto.getListePermis());
        if (voitureRequestDto.getTarifJournalier() != 0)
            voitureRequestDtoExistante.setTarifJournalier(voitureRequestDto.getTarifJournalier());
        if (voitureRequestDto.getKilometrage() != 0)
            voitureRequestDtoExistante.setKilometrage(voitureRequestDto.getKilometrage());
        if (voitureRequestDto.getActif() != null)
            voitureRequestDtoExistante.setActif(voitureRequestDto.getActif());
        if (voitureRequestDto.getRetireDuParc() != null)
            voitureRequestDtoExistante.setRetireDuParc(voitureRequestDto.getRetireDuParc());
    }
}
