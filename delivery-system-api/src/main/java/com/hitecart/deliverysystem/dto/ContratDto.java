package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.model.enums.TypeContrat;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
public class ContratDto {

    private Integer id;
    private TypeContrat typeContrat;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private ChauffeurDto chauffeurDto;

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
        return contrat;
    }
}
