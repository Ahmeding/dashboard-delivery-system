package com.hitecart.deliverysystem.dto;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class DriverFullDtoTest {

/*
    @Test
    public void should_convert_chauffeur_to_chauffeurdto(){
        Driver driver = new Driver();
        driver.setId(1);
        driver.setAddress(null);
        driver.setName("Nomchauffeur");
        driver.setPrenom("prenom driver");
        driver.setEmploymentContracts(null);
        driver.setMission(null);
        driver.setDateNaissance(new Date().toInstant());
        driver.setNumTel("1221212121");
        driver.setSecuNbr("numsecu");
        driver.setTypePermi("B");

        DriverFullDto expetedChauffeur = DriverFullDto.fromEntity(driver);

        assertNotNull(expetedChauffeur);
        assertEquals(expetedChauffeur.getId(),driver.getId());
        assertEquals(expetedChauffeur.getName(),driver.getName());
        assertEquals(expetedChauffeur.getPrenom(),driver.getPrenom());
        assertEquals(expetedChauffeur.getEmploymentContractFullDtos(),driver.getEmploymentContracts());
        assertEquals(expetedChauffeur.getMissionsDto(),driver.getMission());
        assertEquals(expetedChauffeur.getDateNaissance(),driver.getDateNaissance());
        assertEquals(expetedChauffeur.getNumTel(),driver.getNumTel());
        assertEquals(expetedChauffeur.getSecuNbr(),driver.getSecuNbr());
        assertEquals(expetedChauffeur.getTypePermi(),driver.getTypePermi());

    }

    @Test
    public void should_convert_chauffeurdto_to_chauffeur(){
        DriverFullDto driverFullDto = DriverFullDto.builder()
                .id(1)
                .nom("nomchauffeur")
                .prenom("prenom driver")
                .adresseEmail("address@email.com")
                .numTel("1221212121")
                .dateNaissance(new Date().toInstant())
                .numSecu("numsecu")
                .typePermi("B")
                .addressDto(null)
                .employmentContractFullDtos(null)
                .missionsDto(null)
                .build();

        Driver expetedChauffeur = DriverFullDto.toEntity(driverFullDto);

        assertNotNull(expetedChauffeur);
        assertEquals(expetedChauffeur.getId(),driverFullDto.getId());
        assertEquals(expetedChauffeur.getName(),driverFullDto.getName());
        assertEquals(expetedChauffeur.getPrenom(),driverFullDto.getPrenom());
        assertEquals(expetedChauffeur.getEmailAddress(),driverFullDto.getEmailAddress());
        assertEquals(expetedChauffeur.getNumTel(),driverFullDto.getNumTel());
        assertEquals(expetedChauffeur.getDateNaissance(),driverFullDto.getDateNaissance());
        assertEquals(expetedChauffeur.getSecuNbr(),driverFullDto.getSecuNbr());
        assertEquals(expetedChauffeur.getTypePermi(),driverFullDto.getTypePermi());
        assertEquals(expetedChauffeur.getEmploymentContracts(),driverFullDto.getEmploymentContractFullDtos());
        assertEquals(expetedChauffeur.getMission(),driverFullDto.getMissionsDto());

    }
    */
}
