package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeEnergie;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "vehicule")
public class Vehicule extends AbstractEntity{

    @Column(name = "immatriculation")
    private String immatriculation;
    @Column(name = "marque")
    private String marque;
    @Column(name = "modele")
    private String modele;
    @Column(name = "numVin")
    private String numVin;
    @Column(name = "datepmc")
    private LocalDateTime datePMC;
    @Column(name = "kilometrage")
    private BigDecimal kilometrage;
    @Column(name = "typecarburant")
    private TypeEnergie typecarburant;
    @Column(name = "volume")
    private BigDecimal volume;
    @Column(name = "dateexpctrltech")
    private LocalDateTime dateExpCtrlTec;

    @OneToMany(mappedBy = "vehicule")
    private List<Reparation> reparations;
}
