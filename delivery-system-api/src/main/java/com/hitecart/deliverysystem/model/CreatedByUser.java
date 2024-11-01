package com.hitecart.deliverysystem.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedByUser {
    private Integer id;
    private String fullname;
}
