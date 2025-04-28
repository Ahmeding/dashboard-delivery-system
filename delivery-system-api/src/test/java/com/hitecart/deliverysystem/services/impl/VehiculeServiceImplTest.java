package com.hitecart.deliverysystem.services.impl;


class VehiculeServiceImplTest {
/*
    @InjectMocks
    private VehiculeServiceImpl vehiculeService;
    @Mock
    private VehiculeRepository vehiculeRepository;
    @Mock
    private ReparationRepository reparationRepository;

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
        VehicleDto expectedVehiculeDto= VehicleDto.builder().build();
        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class,()->vehiculeService.save(expectedVehiculeDto));

        assertEquals(ErrorCodes.VEHICULE_NOT_VALID,expectedException.getErrorCodes());
        //assertEquals(ErrorCodes.VEHICULE_NOT_VALID,expectedException.getErrorCodes()); //tille de liste des message ?
       // assertEquals(ErrorCodes.VEHICULE_NOT_VALID,expectedException.getErrorCodes()); //les messages?



    }
    @Test
    public void should_update_vehicule_whith_success(){
        VehicleDto expectedvehiculeDto = VehicleDto.builder()

                .missionsDto(null)
                .marque("Mercedes")
                .modele("Sprinter")
                .immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .typecarburant(TypeEnergie.GASOIL)
                .build();

        Vehicule vehiculeToSave = new Vehicule();
        vehiculeToSave.setMarque("Mercedes");
        vehiculeToSave.setModele("Sprinter");
        vehiculeToSave.setImmatriculation("AB-858-ER");
        vehiculeToSave.setDateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()));
        vehiculeToSave.setDatePMC(LocalDate.from(new Date("12/12/2022").toInstant()));
        vehiculeToSave.setKilometrage(BigDecimal.valueOf(122000));
        vehiculeToSave.setVolume(BigDecimal.valueOf(14));
        vehiculeToSave.setNumVin("RRTRE4557B545B545455");
        vehiculeToSave.setTypecarburant(TypeEnergie.GASOIL);

        Vehicule savedVehicule = new Vehicule();
        savedVehicule.setMarque("Mercedes");
        savedVehicule.setModele("Sprinter");
        savedVehicule.setImmatriculation("AB-858-ER");
        savedVehicule.setDateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDatePMC(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setKilometrage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setNumVin("RRTRE4557B545B545455");
        savedVehicule.setTypecarburant(TypeEnergie.GASOIL);
        savedVehicule.setId(2);

        //Mock
        when(vehiculeRepository.save(vehiculeToSave)).thenReturn(savedVehicule);


        //
        VehicleDto saveVehiculeDto = vehiculeService.save(VehicleDto.fromEntity(savedVehicule));

        VehicleDto vToupdate  = saveVehiculeDto;
        vToupdate.setModele("Boxer");
        saveVehiculeDto = vehiculeService.save(vToupdate);

        //assertNotNull(saveVehiculeDto);
        assertNotNull(saveVehiculeDto.getId());
        assertEquals(vToupdate.getMarque(),saveVehiculeDto.getMarque());
        assertEquals(vToupdate.getModele(),saveVehiculeDto.getModele());
        assertEquals(vToupdate.getKilometrage(),saveVehiculeDto.getKilometrage());

    }

    @Test
    public void test_save_vehicule(){
        // Given
        VehicleDto vehiculeDto = VehicleDto.builder()
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
                .typecarburant(TypeEnergie.GASOIL)
                .build();
        //
        Vehicule vehiculeToSave = new Vehicule();
        vehiculeToSave.setMarque("Mercedes");
        vehiculeToSave.setModele("Sprinter");
        vehiculeToSave.setImmatriculation("AB-858-ER");
        vehiculeToSave.setDateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()));
        vehiculeToSave.setDatePMC(LocalDate.from(new Date("12/12/2022").toInstant()));
        vehiculeToSave.setKilometrage(BigDecimal.valueOf(122000));
        vehiculeToSave.setVolume(BigDecimal.valueOf(14));
        vehiculeToSave.setNumVin("RRTRE4557B545B545455");
        vehiculeToSave.setTypecarburant(TypeEnergie.GASOIL);
        //
        Vehicule savedVehicule = new Vehicule();
        savedVehicule.setMarque("Mercedes");
        savedVehicule.setModele("Sprinter");
        savedVehicule.setImmatriculation("AB-858-ER");
        savedVehicule.setDateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDatePMC(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setKilometrage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setNumVin("RRTRE4557B545B545455");
        savedVehicule.setTypecarburant(TypeEnergie.GASOIL);
        savedVehicule.setId(2);
        // Mock
        when(vehiculeRepository.save(vehiculeToSave)).thenReturn(savedVehicule);
        // When
        //vehiculeRepository.save(vehiculeToSave); // for testing verify(vr,times(1)).save(v);
        VehicleDto vehiculeDto1 = vehiculeService.save(VehicleDto.fromEntity(vehiculeToSave));
        //verify(vehiculeService).save(VehicleDto.fromEntity(vehiculeToSave));
        // Then
        assertEquals(vehiculeDto1.getMarque(), vehiculeDto.getMarque());
        verify(vehiculeRepository,times(1)).save(vehiculeToSave);
    }

    @Test
    public void test_find_vehicule_by_id(){
        Integer id = 3;

        Vehicule savedVehicule = new Vehicule();
        savedVehicule.setMarque("Mercedes");
        savedVehicule.setModele("Sprinter");
        savedVehicule.setImmatriculation("AB-858-ER");
        savedVehicule.setDateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDatePMC(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setKilometrage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setNumVin("RRTRE4557B545B545455");
        savedVehicule.setTypecarburant(TypeEnergie.GASOIL);
        savedVehicule.setId(id);

        //Mock calls
        when(vehiculeRepository.findById(id)).thenReturn(Optional.of(savedVehicule));
        // When
        VehicleDto v = vehiculeService.findById(id);
        // Then
        assertNotNull(v);

    }
    @Test
    public void findByImmatriculation(){
        String immat= "AA-852-AA";
        Vehicule savedVehicule = new Vehicule();
        savedVehicule.setMarque("Mercedes");
        savedVehicule.setModele("Sprinter");
        savedVehicule.setImmatriculation(immat);
        savedVehicule.setDateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()));
        savedVehicule.setDatePMC(LocalDate.from(new Date("12/12/2022").toInstant()));
        savedVehicule.setKilometrage(BigDecimal.valueOf(122000));
        savedVehicule.setVolume(BigDecimal.valueOf(14));
        savedVehicule.setNumVin("RRTRE4557B545B545455");
        savedVehicule.setTypecarburant(TypeEnergie.GASOIL);
        savedVehicule.setId(1);

        //Mock calls
        when(vehiculeRepository.findByImmatriculation(immat)).thenReturn(Optional.of(savedVehicule));
        // When
        VehicleDto v = vehiculeService.findByImmatriculation(immat);
        // Then
        assertNotNull(v);
    }
    @Test
    public void test_successfull_find_All(){

        // Given
        List<VehicleDto> allVehiculesDto = new ArrayList<>();

        VehicleDto vehicule1 = VehicleDto.builder()
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
                .typecarburant(TypeEnergie.GASOIL)
                .build();

        VehicleDto vehicule2 = VehicleDto.builder()
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
                .typecarburant(TypeEnergie.GASOIL)
                .build();
        allVehiculesDto.add(vehicule1);
        allVehiculesDto.add(vehicule2);

        List<Vehicule> allVehicules = new ArrayList<>();

        for(VehicleDto v : allVehiculesDto){
            allVehicules.add(VehicleDto.toEntity(v));
        }
        //Mock calls
        when(vehiculeRepository.findAll()).thenReturn(allVehicules);
        //
        List<VehicleDto> expVehiculeList = vehiculeService.findAll();
        //
        assertEquals(2,expVehiculeList.size());


    }
    @Test
    public void test_delete_vehicule_by_id(){
            Integer id=2;

        VehicleDto vehicule2 = VehicleDto.builder()
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
                .typecarburant(TypeEnergie.GASOIL)
                .build();
        // Mock
            when(vehiculeRepository.findById(id)).thenReturn(Optional.ofNullable(VehicleDto.toEntity(vehicule2)));
        // When
        vehiculeService.delete(id);
        //
        //verify(vehiculeService).delete(id);


    }

    @Test
    public void test_delete_throw_Invalide_operation_exception_when_vehicule_has_repation(){
        Integer id = 2;

        List<Reparation> reparationList = Arrays.asList(new Reparation());
        // Mock calls
        when(reparationRepository.findAllByVehiculeId(id)).thenReturn(reparationList);
        // When
        try {
            vehiculeService.delete(id);
            fail("L'exception InvalidOperatioException n'apas été levée");
        } catch (InvalidOperationException e) {
            assertEquals("Impossible de supprimer un vehicule qui " +
                    "a deja une liste des reparations",e.getMessage());
        }
        verify(reparationRepository).findAllByVehiculeId(id);
        verifyNoMoreInteractions(reparationRepository, vehiculeRepository);
    }

    @Test
    public void testDelete_shouldDeleteVehicule_whenVehiculeHasNoReparations(){
        Integer id = 2;
        Vehicule  v = new Vehicule();
                v.setId(id);
                v.setMission(null);
                v.setMarque("Mercedes");
                v.setModele("Sprinter");
                v.setImmatriculation("AB-858-ER");
                v.setDateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()));
                v.setDatePMC(LocalDate.from(new Date("12/12/2022").toInstant()));
                v.setKilometrage(BigDecimal.valueOf(122000));
                v.setVolume(BigDecimal.valueOf(14));
                v.setNumVin("RRTRE4557B545B545455");
                v.setTypecarburant(TypeEnergie.GASOIL);

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
