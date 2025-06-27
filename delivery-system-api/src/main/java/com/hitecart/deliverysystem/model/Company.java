package com.hitecart.deliverysystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "company")
public class Company extends AbstractEntity{

    @Column(name = "name")
    private String name;
    @Column(name = "taxNbr")
    private String taxNbr;
    @Column(name = "phoneNbr")
    private String phoneNbr;
    @Column(name = "email")
    private String email;
    @Embedded
    private Address address;
}
