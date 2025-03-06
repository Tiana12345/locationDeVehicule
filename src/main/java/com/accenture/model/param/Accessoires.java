package com.accenture.model.param;

import lombok.Getter;

@Getter
public enum Accessoires {
    SIEGE_BEBE_VOITURE(TypeVehiculeEnum.VOITURE),
    GPS_VOITURE(TypeVehiculeEnum.VOITURE),
    PORTE_VELO_VOITURE(TypeVehiculeEnum.VOITURE),
    COFFRE_TOIT_VOITURE(TypeVehiculeEnum.VOITURE),
    CASQUE_MOTO(TypeVehiculeEnum.MOTO),
    GANTS_MOTO(TypeVehiculeEnum.MOTO),
    ANTIVOL_MOTO(TypeVehiculeEnum.MOTO),
    PANTALON_PLUIE_MOTO(TypeVehiculeEnum.MOTO),
    DIABLE_UTILITAIRE(TypeVehiculeEnum.UTILITAIRE),
    SANGLE_UTILITAIRE(TypeVehiculeEnum.UTILITAIRE),
    COUVERTURE_PROTECTION_UTILITAIRE(TypeVehiculeEnum.UTILITAIRE),
    CASQUE_VELO(TypeVehiculeEnum.VELO),
    ANTIVOL_VELO(TypeVehiculeEnum.VELO),
    PANIER_VELO(TypeVehiculeEnum.VELO),
    SACOCHE_VELO(TypeVehiculeEnum.VELO),
    KIT_REPARATION_VELO(TypeVehiculeEnum.VELO),
    GILET_REFLECHISSANT_VELO(TypeVehiculeEnum.VELO);

    private TypeVehiculeEnum type;

    Accessoires(TypeVehiculeEnum typeVehiculeEnum) {
        this.type = typeVehiculeEnum;
    }

}