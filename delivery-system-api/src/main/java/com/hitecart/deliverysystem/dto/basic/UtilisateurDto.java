package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.Utilisateur;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class UtilisateurDto {

    private Integer id;
    private String lastname;
    private String firstname;
    private String fullname;
    private String phoneNum;
    private String email;
    private String password;
    private AdresseDto address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(type = "string", format = "date", example = "27-05-2025")
    private LocalDate dateOfBirth;
    private boolean accountLocked;
    private boolean enabled;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;
    private List<RoleDto> rolesDto;
    private List<NotificationDto> notificationsDtos;



    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        if(utilisateur == null){
            return null;
        }
        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .lastname(utilisateur.getLastname())
                .firstname(utilisateur.getFirstname())
                .fullname(utilisateur.fullName())
                .phoneNum(utilisateur.getPhoneNum())
                .email(utilisateur.getEmail())
                .password(utilisateur.getPassword())
                .dateOfBirth(utilisateur.getDateOfBirth())
                .address(AdresseDto.fromEntity(utilisateur.getAddress()))
                .rolesDto(utilisateur.getRoles()!=null?utilisateur.getRoles().stream()
                        .map(RoleDto::fromEntiy).collect(Collectors.toList()):null)
                .notificationsDtos(utilisateur.getNotifications()!=null?
                        utilisateur.getNotifications().stream()
                                .map(NotificationDto::fromEntity)
                                .collect(Collectors.toList()):null)
                .createdAt(utilisateur.getCreatedAt())
                .updatedAt(utilisateur.getUpdatedAt())
                .build();
    }

    public static Utilisateur toEntity(UtilisateurDto dto) {
        if(dto==null){
            return null;
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setLastname(dto.getLastname());
        utilisateur.setFirstname(dto.getFirstname());
        utilisateur.setPhoneNum(dto.getPhoneNum());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setPassword(dto.getPassword());
        utilisateur.setAddress(AdresseDto.toEntity(dto.getAddress()));
       utilisateur.setNotifications(dto.notificationsDtos!=null?dto.notificationsDtos.stream()
               .map(NotificationDto::toEntity).collect(Collectors.toList()):null);
        utilisateur.setCreatedAt(dto.getCreatedAt());
        utilisateur.setUpdatedAt(dto.getUpdatedAt());
        utilisateur.setRoles(dto.rolesDto!=null?dto.rolesDto.stream().map(
                RoleDto::toEntiy).collect(Collectors.toList()):null);

        return utilisateur;
    }
}
