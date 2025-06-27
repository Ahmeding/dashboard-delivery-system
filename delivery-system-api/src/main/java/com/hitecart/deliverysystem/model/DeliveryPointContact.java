package com.hitecart.deliverysystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DeliveryPointContact {
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "phone_nbr")
    private String phoneNbr;
    @Column(name = "email")
    private String email;
}
