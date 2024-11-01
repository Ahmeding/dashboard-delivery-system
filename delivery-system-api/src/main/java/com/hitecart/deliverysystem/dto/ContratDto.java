package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.model.enums.TypeContrat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class ContratDto{

    private TypeContrat typeContrat;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private ChauffeurDto chauffeurDto;
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private CreatedByUserDto createdByUser;

    public static ContratDto fromEntity(Contrat contrat) {
        if(contrat ==null){
            return null;
        }
        return ContratDto.builder()
                .id(contrat.getId())
                .typeContrat(contrat.getTypeContrat())
                .dateDebut(contrat.getDateDebut())
                .dateFin(contrat.getDateFin())
                .chauffeurDto(ChauffeurDto.fromEntity(contrat.getChauffeur()))

                .createdAt(contrat.getCreatedAt())
                .updatedAt(contrat.getUpdatedAt())
                .createdBy(contrat.getCreatedBy())
                .lastModifiedBy(contrat.getLastModifiedBy())
                .build();
    }

    public static Contrat toEntity(ContratDto dto) {
        if(dto==null){
        return null;
        }
        Contrat contrat = new Contrat();
        contrat.setId(dto.getId());
        contrat.setTypeContrat(dto.getTypeContrat());
        contrat.setDateDebut(dto.getDateDebut());
        contrat.setDateFin(dto.getDateFin());
        contrat.setChauffeur(ChauffeurDto.toEntity(dto.getChauffeurDto()));

        contrat.setCreatedAt(dto.getCreatedAt());
        contrat.setUpdatedAt(dto.getUpdatedAt());
        contrat.setCreatedBy(dto.getCreatedBy());
        contrat.setLastModifiedBy(dto.getLastModifiedBy());
        return contrat;
    }
}
