package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.StockDeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Bon de livraison de marchandises stockées. Un client peut recevoir plusieurs bons
 * pour une même marchandise, ce qui permet les livraisons partielles.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "lines")
@Entity
@Table(name = "stock_delivery")
public class StockDelivery extends AbstractEntity{
    @Column(name = "reference", nullable = false, unique = true, updatable = false)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "planned_date", nullable = false)
    private LocalDate plannedDate;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "delivery_address", length = 255)
    private String deliveryAddress;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private StockDeliveryStatus status = StockDeliveryStatus.PLANNED;

    @Builder.Default
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockDeliveryLine> lines = new ArrayList<>();

    public void addLine(StockDeliveryLine line) {
        lines.add(line);
        line.setDelivery(this);
    }

    @PrePersist
    private void initializeDefaults() {
        if (reference == null || reference.isEmpty()) {
            reference = "BL-" + System.currentTimeMillis();
        }
        if (status == null) {
            status = StockDeliveryStatus.PLANNED;
        }
    }
}
