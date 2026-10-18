package com.hitecart.deliverysystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "delivery")
@Entity
@Table(name = "stock_delivery_line")
public class StockDeliveryLine extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_delivery_id", nullable = false)
    private StockDelivery delivery;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stored_goods_id", nullable = false)
    private StoredGoods storedGoods;

    /** Quantité prévue sur le bon. */
    @Column(name = "planned_quantity", nullable = false)
    private Integer plannedQuantity;

    /** Quantité réellement livrée, renseignée à la confirmation du bon. */
    @Column(name = "delivered_quantity")
    private Integer deliveredQuantity;
}
