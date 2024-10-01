package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.model.Chauffeur;
import com.hitecart.deliverysystem.repository.ChauffeurRepository;
import com.hitecart.deliverysystem.repository.ContratRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ChauffeurServiceImplTest {
/*
    @InjectMocks
    ChauffeurServiceImpl chauffeurService;
    @Mock
    private ChauffeurRepository chauffeurRepository;
    @Mock
    private ContratRepository contratRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_save_chauffeur(){
        ChauffeurDto chauffeurDto = ChauffeurDto.builder()
                .nom("Adel")
                .prenom("Salim")
                .adresseEmail("adel.s@gmail.com")
                .dateNaissance(new Date("12/12/1998").toInstant())
                .numSecu("471471458455845845845")
                .numTel("0675757584")
                .typePermi("B")
                .motDePasse("456566464")
                .rib("FR58542154778963558845874")
                .build();


        Chauffeur chauffeurToSave = new Chauffeur();
        chauffeurToSave.setNom("Adel");
        chauffeurToSave.setPrenom("Salim");
        chauffeurToSave.setAdresseEmail("adel.s@gmail.com");
        chauffeurToSave.setDateNaissance(new Date("12/12/1998").toInstant());
        chauffeurToSave.setNumSecu("471471458455845845845");
        chauffeurToSave.setNumTel("0675757584");
        chauffeurToSave.setTypePermi("B");
        chauffeurToSave.setMotDePasse("456566464");
        chauffeurToSave.setRib("FR58542154778963558845874");

        // Mock Calls
        Mockito.when(chauffeurRepository.save(ChauffeurDto.toEntity( chauffeurDto))).thenReturn(chauffeurToSave);
        // When
        ChauffeurDto dto =chauffeurService.save(ChauffeurDto.fromEntity(chauffeurToSave));
        // Then
        assertEquals(dto.getTypePermi(), chauffeurDto.getTypePermi());

    }

 */
}