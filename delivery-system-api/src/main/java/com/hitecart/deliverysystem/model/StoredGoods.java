package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.GoodsType;
import com.hitecart.deliverysystem.model.enums.StoredGoodsStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stored_goods")
public class StoredGoods extends AbstractEntity{

    @Column(name = "reference", nullable = false, unique = true,updatable = false)
    private String reference;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;

    @Column(name = "designation", nullable = false, length = 150)
    private String designation;
    @Enumerated(EnumType.STRING)
    @Column(name = "goods_type", nullable = false)
    private GoodsType goodsType;
    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "dimensions", length = 100)
    private String dimensions;
    @Column(name = "weight_kg", precision = 10, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "storage_location", length = 100)
    private String storageLocation;
    @Column(name = "received_at")
    private LocalDateTime receivedAt;
    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;
    @Builder.Default
    @Column(name = "quantity_reserved", nullable = false)
    private Integer quantityReserved=0;

    @Builder.Default
    @Column(name = "quantity_delivered", nullable = false)
    private Integer quantityDelivered=0;
    @Column(name = "remarks", length = 500)
    private String remarks;
    @PrePersist
    private void initializeDefaults() {
        if (reference == null || reference.isEmpty()) {
            reference = "STK-" + System.currentTimeMillis();
        }
        if (receivedAt == null) {
            receivedAt = LocalDateTime.now();
        }
        if (quantityReserved == null) {
            quantityReserved = 0;
        }
        if (quantityDelivered == null) {
            quantityDelivered = 0;
        }
    }
    /** Quantité pouvant encore être planifiée dans un nouveau bon de livraison. */
    public int availableQuantity() {
        return quantityReceived - quantityReserved - quantityDelivered;
    }
    /** Statut calculé à partir des quantités livrées. */
    public StoredGoodsStatus status() {
        if (quantityDelivered >= quantityReceived) {
            return StoredGoodsStatus.DELIVERED;
        }
        if (quantityDelivered > 0) {
            return StoredGoodsStatus.PARTIALLY_DELIVERED;
        }
        return StoredGoodsStatus.IN_STORAGE;
    }
}
