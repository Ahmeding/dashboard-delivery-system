package com.hitecart.deliverysystem.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.model.enums.TypeContrat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ContratDto{

    private TypeContrat typeContrat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(type = "string", format = "date", example = "27-05-2025")
    private LocalDate dateDebut;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(type = "string", format = "date", example = "27-05-2025")
    private LocalDate dateFin;
    private ChauffeurDto chauffeurDto;
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", format = "date-time", example = "2025-05-27T06:00:00")
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
