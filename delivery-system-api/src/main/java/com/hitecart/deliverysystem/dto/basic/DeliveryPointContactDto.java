package com.hitecart.deliverysystem.dto.basic;

import com.hitecart.deliverysystem.model.DeliveryPointContact;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryPointContactDto {

    private String firstname;
    private String lastname;
    private String phoneNum;
    private String email;


    public static DeliveryPointContactDto fromEntity(DeliveryPointContact contact){
        if(contact==null){
            return null;
        }
        return DeliveryPointContactDto.builder()
                .firstname(contact.getFirstname())
                .lastname(contact.getLastname())
                .phoneNum(contact.getPhoneNum())
                .email(contact.getEmail())
                .build();
    }

    public static DeliveryPointContact toEntity(DeliveryPointContactDto dto){
        if(dto==null){
            return null;
        }
        return DeliveryPointContact.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .phoneNum(dto.getPhoneNum())
                .email(dto.getEmail())
                .build();
    }
}
