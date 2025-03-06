package com.accenture.repository.entity;

import com.accenture.model.param.Accessoires;
import com.accenture.model.param.TypeVehiculeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "Vehicules")


public abstract class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Enumerated(EnumType.STRING)
    private TypeVehiculeEnum typeVehiculeEnum;
    @Enumerated(EnumType.STRING)
    private List<Accessoires> listeAccessoires;
    private String marque;
    private String modele;
    private String couleur;
    private String type;
    private long tarifJournalier;
    private long kilometrage;
    private Boolean actif;
    private Boolean retireDuParc;


    public void ajouterAccessoire(Accessoires accessoire) {
        if (accessoire.getType() == this.typeVehiculeEnum) {
            listeAccessoires.add(accessoire);
        }
    }
}
