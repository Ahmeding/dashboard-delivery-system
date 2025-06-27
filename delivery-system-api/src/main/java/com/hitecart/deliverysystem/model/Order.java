package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@Entity
@Table(name = "order_tab")
public class Order extends AbstractEntity {

    private String reference;
    @Column(name = "code")
    private String code;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "total_amount_ht")
    private BigDecimal totalAmountHT;
    @Column(name = "total_amount_ttc")
    private BigDecimal totalAmountTTC;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @PrePersist
    private void generateReference() {
        if(this.reference == null || this.reference.isEmpty()){
            this.reference = "ORD-"+System.currentTimeMillis();
        }
    }
}
