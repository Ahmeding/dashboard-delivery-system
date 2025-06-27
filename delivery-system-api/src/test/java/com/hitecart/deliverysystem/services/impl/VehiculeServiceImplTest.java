package com.hitecart.deliverysystem.services.impl;


class VehiculeServiceImplTest {
/*
    @InjectMocks
    private VehicleServiceImpl vehiculeService;
    @Mock
    private VehicleRepository vehiculeRepository;
    @Mock
    private RepairRepository reparationRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test//(expected= EntityNotFoundException.class)
    public void should_throw_not_found_entity_exception_2(){
        vehiculeService.findById(0);
    }
    @Test
    public void should_throw_not_found_entity_exception(){
        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class,()->vehiculeService.findById(0));

        assertEquals(ErrorCodes.VEHICULE_NOT_FOUND,expectedException.getErrorCodes());
        // assertEquals(ErrorCodes.VEHICULE_NOT_FOUND,expectedException.getErrorCodes()); //les messages?
    }

    @Test
    public void should_throw_invalid_entity_exception(){
        VehicleFullDto expectedVehiculeDto= VehicleFullDto.builder().build();
        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class,()->vehiculeService.save(expectedVehiculeDto));

        assertEquals(ErrorCodes.VEHICULE_NOT_VALID,expectedException.getErrorCodes());
        //assertEquals(ErrorCodes.VEHICULE_NOT_VALID,expectedException.getErrorCodes()); //tille de liste des message ?
       // assertEquals(ErrorCodes.VEHICULE_NOT_VALID,expectedException.getErrorCodes()); //les messages?



    }
    @Test
    public void should_update_vehicule_whith_success(){
        VehicleFullDto expectedvehiculeDto = VehicleFullDto.builder()

                .missionsDto(null)
                .marque("Mercedes")
                .modele("Sprinter")
                .immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .typecarburant(EnergyType.GASOIL)
                .build();

        Vehicle vehiculeToSave = new Vehicle();
        vehiculeToSave.setBrand("Mercedes");
        vehiculeToSave.setModel("Sprinter");
        vehiculeToSave.setRegistrationNbr("AB-858-ER");
        vehiculeToSave.setCtExpDate(LocalDate.from(new Date("12/04/2024").toInstant()));
        vehiculeToSave.setDfcDate(LocalDate.from(new Date("12/12/2022").toInstant()));
        vehiculeToSave.setMileage(BigDecimal.valueOf(122000));
        vehiculeToSave.setVolume(BigDecimal.valueOf(14));
        vehiculeToSave.setVinNbr("RRTRE4557B545B545455");
        vehiculeToSave.setEnergyType(EnergyType.GASOIL);

        Vehicle savedVehicule = new Vehicle();
        savedVehicule.setBrand("Mercedes");
        savedVehicule.setModel("Sprinter");
        savedVehicule.setRegistrationNbr("AB-858-ER");
        savedVehicule.setCtExpDate(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDfcDate(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setMileage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setVinNbr("RRTRE4557B545B545455");
        savedVehicule.setEnergyType(EnergyType.GASOIL);
        savedVehicule.setId(2);

        //Mock
        when(vehiculeRepository.save(vehiculeToSave)).thenReturn(savedVehicule);


        //
        VehicleFullDto saveVehiculeDto = vehiculeService.save(VehicleFullDto.fromEntity(savedVehicule));

        VehicleFullDto vToupdate  = saveVehiculeDto;
        vToupdate.setModel("Boxer");
        saveVehiculeDto = vehiculeService.save(vToupdate);

        //assertNotNull(saveVehiculeDto);
        assertNotNull(saveVehiculeDto.getId());
        assertEquals(vToupdate.getBrand(),saveVehiculeDto.getBrand());
        assertEquals(vToupdate.getModel(),saveVehiculeDto.getModel());
        assertEquals(vToupdate.getMileage(),saveVehiculeDto.getMileage());

    }

    @Test
    public void test_save_vehicule(){
        // Given
        VehicleFullDto vehiculeDto = VehicleFullDto.builder()
                .id(1)
                .missionsDto(null)
                .marque("Mercedes")
                .modele("Sprinter")
                .immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .typecarburant(EnergyType.GASOIL)
                .build();
        //
        Vehicle vehiculeToSave = new Vehicle();
        vehiculeToSave.setBrand("Mercedes");
        vehiculeToSave.setModel("Sprinter");
        vehiculeToSave.setRegistrationNbr("AB-858-ER");
        vehiculeToSave.setCtExpDate(LocalDate.from(new Date("12/04/2024").toInstant()));
        vehiculeToSave.setDfcDate(LocalDate.from(new Date("12/12/2022").toInstant()));
        vehiculeToSave.setMileage(BigDecimal.valueOf(122000));
        vehiculeToSave.setVolume(BigDecimal.valueOf(14));
        vehiculeToSave.setVinNbr("RRTRE4557B545B545455");
        vehiculeToSave.setEnergyType(EnergyType.GASOIL);
        //
        Vehicle savedVehicule = new Vehicle();
        savedVehicule.setBrand("Mercedes");
        savedVehicule.setModel("Sprinter");
        savedVehicule.setRegistrationNbr("AB-858-ER");
        savedVehicule.setCtExpDate(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDfcDate(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setMileage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setVinNbr("RRTRE4557B545B545455");
        savedVehicule.setEnergyType(EnergyType.GASOIL);
        savedVehicule.setId(2);
        // Mock
        when(vehiculeRepository.save(vehiculeToSave)).thenReturn(savedVehicule);
        // When
        //vehiculeRepository.save(vehiculeToSave); // for testing verify(vr,times(1)).save(v);
        VehicleFullDto vehiculeDto1 = vehiculeService.save(VehicleFullDto.fromEntity(vehiculeToSave));
        //verify(vehiculeService).save(VehicleFullDto.fromEntity(vehiculeToSave));
        // Then
        assertEquals(vehiculeDto1.getBrand(), vehiculeDto.getBrand());
        verify(vehiculeRepository,times(1)).save(vehiculeToSave);
    }

    @Test
    public void test_find_vehicule_by_id(){
        Integer id = 3;

        Vehicle savedVehicule = new Vehicle();
        savedVehicule.setBrand("Mercedes");
        savedVehicule.setModel("Sprinter");
        savedVehicule.setRegistrationNbr("AB-858-ER");
        savedVehicule.setCtExpDate(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDfcDate(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setMileage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setVinNbr("RRTRE4557B545B545455");
        savedVehicule.setEnergyType(EnergyType.GASOIL);
        savedVehicule.setId(id);

        //Mock calls
        when(vehiculeRepository.findById(id)).thenReturn(Optional.of(savedVehicule));
        // When
        VehicleFullDto v = vehiculeService.findById(id);
        // Then
        assertNotNull(v);

    }
    @Test
    public void findByRegistrationNbr(){
        String immat= "AA-852-AA";
        Vehicle savedVehicule = new Vehicle();
        savedVehicule.setBrand("Mercedes");
        savedVehicule.setModel("Sprinter");
        savedVehicule.setRegistrationNbr(immat);
        savedVehicule.setCtExpDate(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDfcDate(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setMileage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setVinNbr("RRTRE4557B545B545455");
        savedVehicule.setEnergyType(EnergyType.GASOIL);
        savedVehicule.setId(1);

        //Mock calls
        when(vehiculeRepository.findByRegistrationNbr(immat)).thenReturn(Optional.of(savedVehicule));
        // When
        VehicleFullDto v = vehiculeService.findByRegistrationNbr(immat);
        // Then
        assertNotNull(v);
    }
    @Test
    public void test_successfull_find_All(){

        // Given
        List<VehicleFullDto> allVehiculesDto = new ArrayList<>();

        VehicleFullDto vehicule1 = VehicleFullDto.builder()
                .id(1)
                .missionsDto(null)
                .marque("Mercedes")
                .modele("Sprinter")
                .immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .typecarburant(EnergyType.GASOIL)
                .build();

        VehicleFullDto vehicule2 = VehicleFullDto.builder()
                .id(1)
                .missionsDto(null)
                .marque("Mercedes")
                .modele("Sprinter")
                .immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .typecarburant(EnergyType.GASOIL)
                .build();
        allVehiculesDto.add(vehicule1);
        allVehiculesDto.add(vehicule2);

        List<Vehicle> allVehicules = new ArrayList<>();

        for(VehicleFullDto v : allVehiculesDto){
            allVehicules.add(VehicleFullDto.toEntity(v));
        }
        //Mock calls
        when(vehiculeRepository.findAll()).thenReturn(allVehicules);
        //
        List<VehicleFullDto> expVehiculeList = vehiculeService.findAll();
        //
        assertEquals(2,expVehiculeList.size());


    }
    @Test
    public void test_delete_vehicule_by_id(){
            Integer id=2;

        VehicleFullDto vehicule2 = VehicleFullDto.builder()
                .id(id)
                .missionsDto(null)
                .marque("Mercedes")
                .modele("Sprinter")
                .immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .typecarburant(EnergyType.GASOIL)
                .build();
        // Mock
            when(vehiculeRepository.findById(id)).thenReturn(Optional.ofNullable(VehicleFullDto.toEntity(vehicule2)));
        // When
        vehiculeService.delete(id);
        //
        //verify(vehiculeService).delete(id);


    }

    @Test
    public void test_delete_throw_Invalide_operation_exception_when_vehicule_has_repation(){
        Integer id = 2;

        List<Repair> reparationList = Arrays.asList(new Repair());
        // Mock calls
        when(reparationRepository.findAllByVehiculeId(id)).thenReturn(reparationList);
        // When
        try {
            vehiculeService.delete(id);
            fail("L'exception InvalidOperatioException n'apas été levée");
        } catch (InvalidOperationException e) {
            assertEquals("Impossible de supprimer un vehicle qui " +
                    "a deja une liste des repairs",e.getMessage());
        }
        verify(reparationRepository).findAllByVehiculeId(id);
        verifyNoMoreInteractions(reparationRepository, vehiculeRepository);
    }

    @Test
    public void testDelete_shouldDeleteVehicule_whenVehiculeHasNoReparations(){
        Integer id = 2;
        Vehicle  v = new Vehicle();
                v.setId(id);
                v.setMission(null);
                v.setBrand("Mercedes");
                v.setModel("Sprinter");
                v.setRegistrationNbr("AB-858-ER");
                v.setCtExpDate(LocalDate.from(new Date("12/04/2024").toInstant()));
                v.setDfcDate(LocalDate.from(new Date("12/12/2022").toInstant()));
                v.setMileage(BigDecimal.valueOf(122000));
                v.setVolume(BigDecimal.valueOf(14));
                v.setVinNbr("RRTRE4557B545B545455");
                v.setEnergyType(EnergyType.GASOIL);

        //Mock calls
        when(reparationRepository.findAllByVehiculeId(id)).thenReturn(Collections.emptyList());
        when(vehiculeRepository.findById(id)).thenReturn(Optional.of(v));
        // When
        vehiculeService.delete(v.getId());

        // Then
        verify(reparationRepository).findAllByVehiculeId(id);
        verify(vehiculeRepository).deleteById(id);
        when(vehiculeRepository.findById(id)).thenReturn(Optional.empty());

        assertTrue(vehiculeRepository.findById(id).isEmpty());
//        verifyNoMoreInteractions(reparationRepository, vehiculeRepository);


    }
*/
}
