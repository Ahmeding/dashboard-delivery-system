package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.DrivingLicenseType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@SuperBuilder
@Table(name = "chauffeur")
public class Chauffeur extends Utilisateur{


    @Column(name = "driving_license_nbr",nullable = false,unique = true)
    private String drivinglicenseNbr;
    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL for integer mapping
    @Column(name="typepermis")
    private DrivingLicenseType drivinglicenseType;
    @Column(name="num_secu")
    private String numSecu;
    @Column(name="rib")
    private String rib;
    @OneToMany(mappedBy = "chauffeur")
    private List<Contrat> contrats;
}
