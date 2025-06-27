package com.hitecart.deliverysystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class Address {
    @Column(name = "street")
    private String street;
    @Column(name = "additional_info")
    private String additionalInfo;
    @Column(name = "city")
    private String city;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "country")
    private String country;

}
