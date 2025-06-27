package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "order_line")
public class OrderLine extends  AbstractEntity{


    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "ttc_price")
    private BigDecimal ttcPrice;
    @Column(name = "tva")
    private BigDecimal tva;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_service_id",nullable = false,referencedColumnName = "id")
    private DeliveryService deliveryService;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
