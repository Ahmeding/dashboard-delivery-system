package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.TypeFraisMission;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lignefraismission")
public class LigneFraisMission  extends AbstractEntity{

    @Column(name = "type")
    private TypeFraisMission type;
    @Column(name = "dateAchat")
    private LocalDateTime dateAchat;
    @Column(name = "quantite")
    private BigDecimal quantite;
    @Column(name = "cout")
    private BigDecimal cout;
    @ManyToOne
    @JoinColumn(name = "idfraismission")
    private FraisMission fraisMission;
}
