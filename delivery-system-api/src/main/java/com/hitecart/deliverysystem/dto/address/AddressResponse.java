package com.hitecart.deliverysystem.dto.address;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse{
        private String street;
        private String additionalInfo;
        private String city;
        private String postalCode;
        private String country;
    }
