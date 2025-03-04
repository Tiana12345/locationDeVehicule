package com.accenture.repository;

import com.accenture.model.param.Carburant;
import com.accenture.model.param.Permis;
import com.accenture.repository.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoitureDao extends JpaRepository<Voiture, Long> {

}
