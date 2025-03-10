package com.accenture.repository.entity;

import com.accenture.model.param.Permis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Est la classe utilisée pour créer les attributs nécéssaire aux clients
 * Voir classe mère {@link Utilisateur}
 *
 * @author tatiana.m.tessier
 * @since 1.0
 */

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue(value = "ROLE_CLIENT")
public class Client extends Utilisateur {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id")
    private Adresse adresse;

    private LocalDate dateNaissance;
    private LocalDate dateInscription = LocalDate.now();
    @ElementCollection
    private List<Permis> listePermis;
    private Boolean desactive;

    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    private List<Location> locations;
}
