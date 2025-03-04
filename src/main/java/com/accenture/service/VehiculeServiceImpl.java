package com.accenture.service;

import com.accenture.repository.MotoDao;
import com.accenture.repository.UtilitaireDao;
import com.accenture.repository.VeloDao;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculeServiceImpl implements VehiculeService {
    private static final Logger logger = LoggerFactory.getLogger(VehiculeServiceImpl.class);
    @Autowired
    VoitureDao voitureDao;
    @Autowired
    MotoDao motoDao;
    @Autowired
    VeloDao veloDao;
    @Autowired
    UtilitaireDao utilitaireDao;

    @Override
    public List<Vehicule> trouverToutVehicules() {
        logger.info("Entrée dans la méthode trouverToutVehicules");
        List<Vehicule> vehicule = new ArrayList<>();
        try {
            logger.debug("Récupération de toutes les voitures depuis voiturdao");
            vehicule.addAll(voitureDao.findAll());
            logger.debug("Récupération de toutes les motos depuis motoDao");
            vehicule.addAll(motoDao.findAll());
            logger.debug("Récupération de toutes les velos depuis velodao");
            vehicule.addAll(veloDao.findAll());
            logger.debug("Récupération de toutes les utilitaires depuis utilitaireDao");
            vehicule.addAll(utilitaireDao.findAll());
        } catch (Exception e) {
            logger.error("Une exception est survenue lors de la recherche de tous les véhicules : {}", e.getMessage(), e);
        }
        logger.info("Sortie de la méthode trouverToutVehicules avec {} véhicules trouvés", vehicule.size());
        return vehicule;
    }
    @Override
    public List<Vehicule> rechercher(Boolean actif, Boolean retireDuParc) {
        logger.info("Entrée dans la méthode rechercher avec actif={} et retireDuParc={}", actif, retireDuParc);
        List<Vehicule> vehicules = new ArrayList<>();
        try {
            logger.debug("Récupération de toutes les voitures");
            List<Voiture> voitures = voitureDao.findAll();
            logger.debug("Récupération de toutes les motos");
            List<Moto> motos = motoDao.findAll();
            logger.debug("Récupération de toutes les vélos");
            List<Velo> velos = veloDao.findAll();
            logger.debug("Récupération de toutes les utilitaires");
            List<Utilitaire> utilitaires = utilitaireDao.findAll();

            vehicules.addAll(filtrerVehicules(voitures, actif, retireDuParc));
            vehicules.addAll(filtrerVehicules(motos, actif, retireDuParc));
            vehicules.addAll(filtrerVehicules(velos, actif, retireDuParc));
            vehicules.addAll(filtrerVehicules(utilitaires, actif, retireDuParc));
        } catch (Exception e) {
            logger.error("Une exception est survenue lors de la recherche de tous les véhicules : {}", e.getMessage(), e);
        }
        logger.info("Sortie de la méthode rechercher avec {} véhicules trouvés", vehicules.size());
        return vehicules;
    }

    private <T extends Vehicule> List<T> filtrerVehicules(List<T> vehicules, Boolean actif, Boolean retireDuParc) {
        return vehicules.stream()
                .filter(vehicule -> (actif == null || vehicule.getActif().equals(actif)) &&
                                    (retireDuParc == null || vehicule.getRetireDuParc().equals(retireDuParc)))
                .collect(Collectors.toList());
    }

}