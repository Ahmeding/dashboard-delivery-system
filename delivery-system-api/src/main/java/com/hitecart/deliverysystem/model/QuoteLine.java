package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuperBuilder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name = "quote_line")
public class QuoteLine extends AbstractEntity{

    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "unitPrice")
    private BigDecimal unitPrice;
    @Column(name = "ttc_price")
    private BigDecimal ttcPrice;
    @Column(name = "tva")
    private BigDecimal tva;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_service_id",nullable = false,referencedColumnName = "id")
    private DeliveryService deliveryService;

    @ManyToOne
    @JoinColumn(name = "quote_id")
    private Quote quote;
}
