package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.model.Chauffeur;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChauffeurDtoTest {

/*
    @Test
    public void should_convert_chauffeur_to_chauffeurdto(){
        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setId(1);
        chauffeur.setAdresse(null);
        chauffeur.setNom("Nomchauffeur");
        chauffeur.setPrenom("prenom chauffeur");
        chauffeur.setContrats(null);
        chauffeur.setMission(null);
        chauffeur.setDateNaissance(new Date().toInstant());
        chauffeur.setNumTel("1221212121");
        chauffeur.setNumSecu("numsecu");
        chauffeur.setTypePermi("B");

        ChauffeurDto expetedChauffeur = ChauffeurDto.fromEntity(chauffeur);

        assertNotNull(expetedChauffeur);
        assertEquals(expetedChauffeur.getId(),chauffeur.getId());
        assertEquals(expetedChauffeur.getNom(),chauffeur.getNom());
        assertEquals(expetedChauffeur.getPrenom(),chauffeur.getPrenom());
        assertEquals(expetedChauffeur.getContratDtos(),chauffeur.getContrats());
        assertEquals(expetedChauffeur.getMissionsDto(),chauffeur.getMission());
        assertEquals(expetedChauffeur.getDateNaissance(),chauffeur.getDateNaissance());
        assertEquals(expetedChauffeur.getNumTel(),chauffeur.getNumTel());
        assertEquals(expetedChauffeur.getNumSecu(),chauffeur.getNumSecu());
        assertEquals(expetedChauffeur.getTypePermi(),chauffeur.getTypePermi());

    }

    @Test
    public void should_convert_chauffeurdto_to_chauffeur(){
        ChauffeurDto chauffeurDto = ChauffeurDto.builder()
                .id(1)
                .nom("nomchauffeur")
                .prenom("prenom chauffeur")
                .adresseEmail("adresse@email.com")
                .numTel("1221212121")
                .dateNaissance(new Date().toInstant())
                .numSecu("numsecu")
                .typePermi("B")
                .adresseDto(null)
                .contratDtos(null)
                .missionsDto(null)
                .build();

        Chauffeur expetedChauffeur = ChauffeurDto.toEntity(chauffeurDto);

        assertNotNull(expetedChauffeur);
        assertEquals(expetedChauffeur.getId(),chauffeurDto.getId());
        assertEquals(expetedChauffeur.getNom(),chauffeurDto.getNom());
        assertEquals(expetedChauffeur.getPrenom(),chauffeurDto.getPrenom());
        assertEquals(expetedChauffeur.getAdresseEmail(),chauffeurDto.getAdresseEmail());
        assertEquals(expetedChauffeur.getNumTel(),chauffeurDto.getNumTel());
        assertEquals(expetedChauffeur.getDateNaissance(),chauffeurDto.getDateNaissance());
        assertEquals(expetedChauffeur.getNumSecu(),chauffeurDto.getNumSecu());
        assertEquals(expetedChauffeur.getTypePermi(),chauffeurDto.getTypePermi());
        assertEquals(expetedChauffeur.getContrats(),chauffeurDto.getContratDtos());
        assertEquals(expetedChauffeur.getMission(),chauffeurDto.getMissionsDto());

    }
    */
}