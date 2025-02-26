package com.accenture.repository;

import com.accenture.model.paramVehicule.Carburant;
import com.accenture.model.paramVehicule.Permis;
import com.accenture.repository.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoitureDao extends JpaRepository<Voiture, Long> {

    List<Voiture> findByMarqueContaining(String marque);

    List<Voiture> findByModeleContaining(String modele);

    List<Voiture> findByCouleurContaining(String couleur);

    List<Voiture> findByNombreDePlaces(Integer nombreDePlaces);

    List<Voiture> findByCarburant(Carburant carburant);

    List<Voiture> findByNombreDePortes(Integer nombreDePortes);

    List<Voiture> findByTransmissionContaining(String transmission);

    List<Voiture> findByClim(Boolean clim);

    List<Voiture> findByNombreDeBagages(Integer nombreDeBagages);

    List<Voiture> findByTypeContaining(String type);

    List<Voiture> findByListePermisContaining(Permis permis);

    List<Voiture> findByTarifJournalier(Long tarifJournalier);

    List<Voiture> findByKilometrage(Long kilometrage);

    List<Voiture> findByActif(Boolean actif);

    List<Voiture> findByRetireDuParc(Boolean retireDuParc);
}
