package com.hitecart.deliverysystem.services.impl;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class ReparationServiceImplTest {
/*
    // which service we want to test
    @InjectMocks
    private ReparationServiceImpl reparationService;
    @Mock
    private ReparationRepository reparationRepository;
    @Mock
    private PieceRechangeRepository pieceRechangeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void should_successfully_save_a_Reparation(){

        // given
        List<PieceRechangeDto> pieceRechanges = new ArrayList<>();
        PieceRechangeDto pieceRechangeDto1 = PieceRechangeDto.builder()
                .nom("Pneu")
                .prix(BigDecimal.valueOf(100.0))
                .reference("Ref001")
                .build();
        pieceRechanges.add(pieceRechangeDto1);
        PieceRechangeDto pieceRechangeDto2 = PieceRechangeDto.builder()
                .nom("Huile moteur")
                .prix(BigDecimal.valueOf(50.0))
                .reference("Ref002")
                .build();
        pieceRechanges.add(pieceRechangeDto2);

        VehiculeDto vehiculeDto = VehiculeDto.builder()
                .id(1)
                .missionsDto(null).marque("Renault").modele("Clio").immatriculation("AB-858-ER")
                .dateExpCtrlTec(LocalDate.from(new Date("12/04/2024").toInstant()))
                .datePMC(LocalDate.from(new Date("12/12/2022").toInstant()))
                .kilometrage(BigDecimal.valueOf(122000))
                .volume(BigDecimal.valueOf(14))
                .numVin("RRTRE4557B545B545455")
                .build();

        ReparationDto reparationDto = ReparationDto.builder()
                .cout(BigDecimal.valueOf(500))
                .type(TypeReparation.MECANIQUE)
                .dateDebut(new Date().toInstant())
                .commentaire("commentaire test")
                .dateFin(new Date().toInstant())
                .pieceRechangesDtos(pieceRechanges)
                .vehiculeDto(vehiculeDto)
                .build();

        List<PieceRechange> piecesRList = new ArrayList<>();


        Reparation reparationToSave = new Reparation();
        reparationToSave.setCommentaire("commentaire test");
        reparationToSave.setCout(BigDecimal.valueOf(500));
        reparationToSave.setType(TypeReparation.MECANIQUE);
        reparationToSave.setDateDebut(new Date().toInstant());
        reparationToSave.setDateFin(new Date("12/04/2024").toInstant());
        reparationToSave.setVehicule(VehiculeDto.toEntity(vehiculeDto));
        reparationToSave.setPieceRechanges(
                 pieceRechanges.stream()
                .map(PieceRechangeDto::toEntity).collect(Collectors.toList()));


        // Mocking

        when(reparationRepository.save(reparationToSave))
                .thenReturn(ReparationDto.toEntity(reparationDto));

        // When

        ReparationDto reparationDto1  = reparationService.save(ReparationDto.fromEntity(reparationToSave));

        // then

        assertEquals(reparationDto1.getCout(), reparationDto.getCout());


    }
    */
}
