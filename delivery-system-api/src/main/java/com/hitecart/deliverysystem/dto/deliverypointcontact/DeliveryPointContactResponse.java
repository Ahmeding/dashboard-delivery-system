package com.hitecart.deliverysystem.dto.deliverypointcontact;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPointContactResponse {

    private String firstname;
    private String lastname;
    private String phoneNbr;
    private String email;


}
