package com.accenture.repository;

import com.accenture.model.Carburant;
import com.accenture.model.Permis;
import com.accenture.repository.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoitureDao extends JpaRepository<Voiture, Long> {

    List<Voiture> findByMarqueContaining(String marque);

    List<Voiture> findByModeleContaining(String modele);

    List<Voiture> findByCouleurContaining(String couleur);

    List<Voiture> findByNombreDePlaces(int nombreDePlaces);

    List<Voiture> findByCarburant(Carburant carburant);

    List<Voiture> findByNombreDePortes(int nombreDePortes);

    List<Voiture> findByTransmissionContaining(String transmission);

    List<Voiture> findByClim(Boolean clim);

    List<Voiture> findByNombreDeBagages(int nombreDeBagages);

    List<Voiture> findByTypeContaining(String type);

    List<Voiture> findByListePermisContaining(Permis permis);

    List<Voiture> findByTarifJournalier(Long tarifJournalier);

    List<Voiture> findByKilometrage(Long kilometrage);

    List<Voiture> findByActif(Boolean actif);

    List<Voiture> findByRetireDuParc(Boolean retireDuParc);
}
