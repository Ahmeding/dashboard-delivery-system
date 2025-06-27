package com.hitecart.deliverysystem.model;

import com.hitecart.deliverysystem.model.enums.OrderStatus;
import com.hitecart.deliverysystem.model.enums.QuoteStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "quote")
public class Quote extends AbstractEntity{

    private String reference;
    @Column(name = "code")
    private String code;
    @Column(name = "quote_date")
    private LocalDateTime quoteDate;
    @Column(name = "total_amount_ht")
    private BigDecimal totalAmountHT;
    @Column(name = "total_mount_ttc")
    private BigDecimal totalAmountTTC;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "quote")
    private List<QuoteLine> quoteLines = new ArrayList<>();
    @Column(name = "quote_status")
    @Enumerated(EnumType.STRING)
    private QuoteStatus quoteStatus;

    @PrePersist
    private void generateCode() {
        if(this.code == null || this.code.isEmpty()){
            this.code = "QT-"+System.currentTimeMillis();
        }
    }
}
