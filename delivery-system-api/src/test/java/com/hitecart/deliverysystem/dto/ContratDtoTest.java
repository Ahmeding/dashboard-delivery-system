package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Contrat;
import com.hitecart.deliverysystem.model.enums.TypeContrat;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ContratDtoTest {

    ContratDto contratDto;
    Contrat contrat;

    @Before
    public void setUp() throws Exception {
//        contratDto = ContratDto.builder()
//                .id(1)
//                .chauffeurDto(null)
//                .typeContrat(TypeContrat.CDI)
//                .dateDebut(new Date().toInstant())
//                .dateFin(new Date().toInstant())
//                .build();
//        contrat = new Contrat();
//        contrat.setChauffeur(null);
//        contrat.setTypeContrat(TypeContrat.CDI);
//        contrat.setId(1);
//        contrat.setDateDebut(new Date().toInstant());
//        contrat.setDateFin(new Date().toInstant());

    }

    @Test
    public void should_mapped_contrat_to_contratdto(){

        contrat = new Contrat();
        contrat.setChauffeur(null);
        contrat.setTypeContrat(TypeContrat.CDI);
        contrat.setId(1);
        contrat.setDateDebut(LocalDateTime.now());
        contrat.setDateFin(LocalDateTime.now());

        ContratDto expectedContratDto = ContratDto.fromEntity(contrat);

        //assertNotNull(expectedContratDto);
        assertEquals(expectedContratDto.getId(),contrat.getId());
        assertEquals(expectedContratDto.getTypeContrat(),contrat.getTypeContrat());
        assertEquals(expectedContratDto.getChauffeurDto(),contrat.getChauffeur());
        assertEquals(expectedContratDto.getDateDebut(),contrat.getDateDebut());
        assertEquals(expectedContratDto.getDateFin(),contrat.getDateFin());
    }
    @Test
    public void should_mapped_contratdto_to_contrat(){

        contratDto = ContratDto.builder()
                .id(1)
                .chauffeurDto(null)
                .typeContrat(TypeContrat.CDI)
                .dateDebut(LocalDateTime.now())
                .dateFin(LocalDateTime.now())
                .build();
        Contrat expectedContrat = ContratDto.toEntity(contratDto);

        //assertNotNull(expectedContrat);
        assertEquals(expectedContrat.getId(),contratDto.getId());
        assertEquals(expectedContrat.getTypeContrat(),contratDto.getTypeContrat());
        assertEquals(expectedContrat.getChauffeur(),contratDto.getChauffeurDto());
        assertEquals(expectedContrat.getDateDebut(),contratDto.getDateDebut());
        assertEquals(expectedContrat.getDateFin(),contratDto.getDateFin());


    }
}
