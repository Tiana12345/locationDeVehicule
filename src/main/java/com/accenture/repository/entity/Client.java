package com.accenture.repository.entity;

import com.accenture.model.Permis;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "CLIENT")

public class Client extends Utilisateur {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id")
    private Adresse adresse;
    private LocalDate dateNaissance;
    private LocalDate dateInscription;
    @ElementCollection
    private List<Permis> listePermis;
    private Boolean desactive;

}

