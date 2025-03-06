package com.accenture.service;

import com.accenture.repository.MotoDao;
import com.accenture.repository.UtilitaireDao;
import com.accenture.repository.VeloDao;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class VehiculeServiceImpl implements VehiculeService {

    VoitureDao voitureDao;
    MotoDao motoDao;
    VeloDao veloDao;
    UtilitaireDao utilitaireDao;



    public VehiculeServiceImpl(UtilitaireDao utilitaireDao, MotoDao motoDao, VeloDao veloDao, VoitureDao voitureDao) {
        this.utilitaireDao = utilitaireDao;
        this.motoDao = motoDao;
        this.veloDao = veloDao;
        this.voitureDao = voitureDao;
    }

    @Override
    public Vehicule trouverParId(Long id) {
        log.info("Entrée dans la méthode trouverParId avec ID : {}", id);
        Vehicule vehicule = null;
        try {
            log.debug("Recherche du véhicule avec ID : {} dans voitureDao", id);
            vehicule = voitureDao.findById(id).orElse(null);
            if (vehicule == null) {
                log.debug("Recherche du véhicule avec ID : {} dans motoDao", id);
                vehicule = motoDao.findById(id).orElse(null);
            }
            if (vehicule == null) {
                log.debug("Recherche du véhicule avec ID : {} dans veloDao", id);
                vehicule = veloDao.findById(id).orElse(null);
            }
            if (vehicule == null) {
                log.debug("Recherche du véhicule avec ID : {} dans utilitaireDao", id);
                vehicule = utilitaireDao.findById(id).orElse(null);
            }
        } catch (Exception e) {
            log.error("Une exception est survenue lors de la recherche du véhicule avec ID : {} : {}", id, e.getMessage(), e);
        }
        if (vehicule != null) {
            log.info("Véhicule trouvé avec ID : {}", id);
        } else {
            log.warn("Aucun véhicule trouvé avec ID : {}", id);
        }
        return vehicule;
    }

    @Override
    public List<Vehicule> trouverToutVehicules() {
        log.info("Entrée dans la méthode trouverToutVehicules");
        List<Vehicule> vehicule = new ArrayList<>();
        try {
            log.debug("Récupération de toutes les voitures depuis voiturdao");
            vehicule.addAll(voitureDao.findAll());
            log.debug("Récupération de toutes les motos depuis motoDao");
            vehicule.addAll(motoDao.findAll());
            log.debug("Récupération de toutes les velos depuis velodao");
            vehicule.addAll(veloDao.findAll());
            log.debug("Récupération de toutes les utilitaires depuis utilitaireDao");
            vehicule.addAll(utilitaireDao.findAll());
        } catch (Exception e) {
            log.error("Une exception est survenue lors de la recherche de tous les véhicules : {}", e.getMessage(), e);
        }
        log.info("Sortie de la méthode trouverToutVehicules avec {} véhicules trouvés", vehicule.size());
        return vehicule;
    }
    @Override
    public List<Vehicule> rechercher(Boolean actif, Boolean retireDuParc) {
        log.info("Entrée dans la méthode rechercher avec actif={} et retireDuParc={}", actif, retireDuParc);
        List<Vehicule> vehicules = new ArrayList<>();
        try {
            log.debug("Récupération de toutes les voitures");
            List<Voiture> voitures = voitureDao.findAll();
            log.debug("Récupération de toutes les motos");
            List<Moto> motos = motoDao.findAll();
            log.debug("Récupération de toutes les vélos");
            List<Velo> velos = veloDao.findAll();
            log.debug("Récupération de toutes les utilitaires");
            List<Utilitaire> utilitaires = utilitaireDao.findAll();

            vehicules.addAll(filtrerVehicules(voitures, actif, retireDuParc));
            vehicules.addAll(filtrerVehicules(motos, actif, retireDuParc));
            vehicules.addAll(filtrerVehicules(velos, actif, retireDuParc));
            vehicules.addAll(filtrerVehicules(utilitaires, actif, retireDuParc));
        } catch (Exception e) {
            log.error("Une exception est survenue lors de la recherche de tous les véhicules : {}", e.getMessage(), e);
        }
        log.info("Sortie de la méthode rechercher avec {} véhicules trouvés", vehicules.size());
        return vehicules;
    }

    private <T extends Vehicule> List<T> filtrerVehicules(List<T> vehicules, Boolean actif, Boolean retireDuParc) {
        return vehicules.stream()
                .filter(vehicule -> (actif == null || vehicule.getActif().equals(actif)) &&
                                    (retireDuParc == null || vehicule.getRetireDuParc().equals(retireDuParc)))
                .toList();
    }

}