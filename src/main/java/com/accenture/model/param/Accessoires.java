package com.accenture.model.param;

public enum Accessoires {
    SIEGEBEBE(TypeVehiculeEnum.VOITURE),
    GPS(TypeVehiculeEnum.VOITURE),
    PORTEVELO(TypeVehiculeEnum.VOITURE),
    COFFRETOIT(TypeVehiculeEnum.VOITURE),
    CASQUE(TypeVehiculeEnum.MOTO),
    GANTS(TypeVehiculeEnum.MOTO),
    ANTIVOL(TypeVehiculeEnum.MOTO),
    PANTALONPLUIE(TypeVehiculeEnum.MOTO),
    DIABLE(TypeVehiculeEnum.UTILITAIRE),
    SANGLE(TypeVehiculeEnum.UTILITAIRE),
    COUVERTURE_PROTECTION(TypeVehiculeEnum.UTILITAIRE),
    CASQUE_VELO(TypeVehiculeEnum.VELO),
    ANTIVOL_VELO(TypeVehiculeEnum.VELO),
    PANIER(TypeVehiculeEnum.VELO),
    SACOCHE(TypeVehiculeEnum.VELO),
    KIT_REPARATION(TypeVehiculeEnum.VELO),
    GILET_REFLECHISSANT(TypeVehiculeEnum.VELO);

    private TypeVehiculeEnum type;

    Accessoires(TypeVehiculeEnum type) {
        this.type = type;
    }

    public TypeVehiculeEnum getType() {
        return type;
    }
}