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

/**
 * Est la classe utilisée pour créer les attributs nécéssaire aux clients
 * Voir classe mère {@link Utilisateur}
 * @author tatiana.m.tessier
 * @since 1.0
 *
 */

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
    //localDate.now
    private LocalDate dateInscription;
    @ElementCollection
    private List<Permis> listePermis;
    private Boolean desactive;

}

