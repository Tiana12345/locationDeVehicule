package com.accenture.repository.entity;

import com.accenture.model.param.Accessoires;
import com.accenture.model.param.Etat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Location {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "client_id")
        private Client client;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "vehicule_id")
        private Vehicule vehicule;

        @ElementCollection(targetClass = Accessoires.class)
        @Enumerated(EnumType.STRING)
        private List<Accessoires> accessoires;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
        private LocalDate dateDebut;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
        private LocalDate dateFin;

        private int kilometresParcourus;
        private int montantTotal;
        private LocalDate dateValidation;
        @Enumerated(EnumType.STRING)
        private Etat etat;
}
