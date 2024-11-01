package com.hitecart.deliverysystem.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "service_delivery")
public class ServiceDelivery extends AbstractEntity{

    @Column(name = "reference")
    private String reference;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "unit_price")
    private BigDecimal unitPriceHT;
    @Column(name = "prixTTC")
    private BigDecimal priceTTC;
    @Column(name = "tva")
    private BigDecimal tva;
    private boolean enabled;

}
