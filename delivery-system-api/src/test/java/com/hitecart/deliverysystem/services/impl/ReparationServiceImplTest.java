package com.hitecart.deliverysystem.services.impl;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class ReparationServiceImplTest {
/*
    // which service we want to test
    @InjectMocks
    private RepairServiceImpl reparationService;
    @Mock
    private RepairRepository reparationRepository;
    @Mock
    private SparePartRepository pieceRechangeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void should_successfully_save_a_Reparation(){

        // given
        List<SparePartFullDto> spareParts = new ArrayList<>();
        SparePartFullDto pieceRechangeDto1 = SparePartFullDto.builder()
                .nom("Pneu")
                .prix(BigDecimal.valueOf(100.0))
                .reference("Ref001")
                .build();
        spareParts.add(pieceRechangeDto1);
        SparePartFullDto pieceRechangeDto2 = SparePartFullDto.builder()
                .nom("Huile moteur")
                .prix(BigDecimal.valueOf(50.0))
                .reference("Ref002")
                .build();
        spareParts.add(pieceRechangeDto2);

        VehicleFullDto vehiculeDto = VehicleFullDto.builder()
                .id(1)
                .missionsDto(null).marque("Renault").modele("Clio").immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .build();

        RepairFullDto repairFullDto = RepairFullDto.builder()
                .cout(BigDecimal.valueOf(500))
                .type(TypeReparation.MECANIQUE)
                .dateDebut(new Date().toInstant())
                .commentaire("commentaire test")
                .dateFin(new Date().toInstant())
                .pieceRechangesDtos(spareParts)
                .vehiculeDto(vehiculeDto)
                .build();

        List<SparePart> piecesRList = new ArrayList<>();


        Repair reparationToSave = new Repair();
        reparationToSave.setComment("commentaire test");
        reparationToSave.setCost(BigDecimal.valueOf(500));
        reparationToSave.setType(TypeReparation.MECANIQUE);
        reparationToSave.setStartDate(new Date().toInstant());
        reparationToSave.setEndDate(new Date("12/04/2024").toInstant());
        reparationToSave.setVehicle(VehicleFullDto.toEntity(vehiculeDto));
        reparationToSave.setSpareParts(
                 spareParts.stream()
                .map(SparePartFullDto::toEntity).collect(Collectors.toList()));


        // Mocking

        when(reparationRepository.save(reparationToSave))
                .thenReturn(RepairFullDto.toEntity(repairFullDto));

        // When

        RepairFullDto reparationDto1  = reparationService.save(RepairFullDto.fromEntity(reparationToSave));

        // then

        assertEquals(reparationDto1.getCost(), repairFullDto.getCost());


    }
    */
}
