package com.hitecart.deliverysystem.dto;

import com.hitecart.deliverysystem.dto.employmentcontract.EmploymentContractResponse;
import com.hitecart.deliverysystem.model.EmploymentContract;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class EmploymentContractDtoTest {

    EmploymentContractResponse employmentContractBasicDto;
    EmploymentContract employmentContract;

    @Before
    public void setUp() throws Exception {
//        employmentContractBasicDto = EmploymentContractFullDto.builder()
//                .id(1)
//                .driverFullDto(null)
//                .contractType(ContractType.CDI)
//                .dateDebut(new Date().toInstant())
//                .dateFin(new Date().toInstant())
//                .build();
//        employmentContract = new EmploymentContract();
//        employmentContract.setDriver(null);
//        employmentContract.setContractType(ContractType.CDI);
//        employmentContract.setId(1);
//        employmentContract.setStartDate(new Date().toInstant());
//        employmentContract.setEndDate(new Date().toInstant());

    }
/*
    @Test
    public void should_mapped_contrat_to_contratdto(){

        employmentContract = new EmploymentContract();
        employmentContract.setDriver(null);
        employmentContract.setContractType(ContractType.CDI);
        employmentContract.setId(1);
        employmentContract.setStartDate(LocalDate.now());
        employmentContract.setEndDate(LocalDate.now());

        EmploymentContractFullDto expectedContratDto = EmploymentContractFullDto.fromEntity(employmentContract);

        //assertNotNull(expectedContratDto);
        assertEquals(expectedContratDto.getId(),employmentContract.getId());
        assertEquals(expectedContratDto.getContractType(),employmentContract.getContractType());
        assertEquals(expectedContratDto.getDriverFullDto(),employmentContract.getDriver());
        assertEquals(expectedContratDto.getStartDate(),employmentContract.getStartDate());
        assertEquals(expectedContratDto.getEndDate(),employmentContract.getEndDate());
    }
    @Test
    public void should_mapped_contratdto_to_contrat(){

        employmentContractBasicDto = EmploymentContractFullDto.builder()
                .id(1)
                .driverFullDto(null)
                .contractType(ContractType.CDI)
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now())
                .build();
        EmploymentContract expectedContrat = EmploymentContractFullDto.toEntity(employmentContractBasicDto);

        //assertNotNull(expectedContrat);
        assertEquals(expectedContrat.getId(),employmentContractBasicDto.getId());
        assertEquals(expectedContrat.getContractType(),employmentContractBasicDto.getContractType());
        assertEquals(expectedContrat.getDriver(),employmentContractBasicDto.getDriverFullDto());
        assertEquals(expectedContrat.getStartDate(),employmentContractBasicDto.getStartDate());
        assertEquals(expectedContrat.getEndDate(),employmentContractBasicDto.getEndDate());


    }*/
}
