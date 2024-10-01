package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "devis")
public class Devis extends AbstractEntity{

    @Column(name = "code")
    private String code;
    @Column(name = "dateDevis")
    private LocalDateTime dateDevis;
    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;
    @OneToMany(mappedBy = "devis")
    private List<LigneDevis> ligneDevis;

}
