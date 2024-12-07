import {Component, OnInit} from '@angular/core';
import {MissionDto} from "../../../../../../ds-api/src/models/mission-dto";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ServiceDeliveryDto} from "../../../../../../ds-api/src/models/service-delivery-dto";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";
import {ServiceDeliveryService} from "../../../../../../ds-api/src/services/service-delivery.service";
import {DeliveryPointsService} from "../../../../../../ds-api/src/services/delivery-points.service";
import {EtapeDto} from "../../../../../../ds-api/src/models/etape-dto";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {VehiculeService} from "../../../../../../ds-api/src/services/vehicule.service";
import {ChauffeurService} from "../../../../../../ds-api/src/services/chauffeur.service";

@Component({
  selector: 'app-add-mission',
  templateUrl: './add-mission.component.html',
  styleUrls: ['./add-mission.component.scss']
})
export class AddMissionComponent implements OnInit {
  currentStep = 0;
  missionForm: FormGroup= new FormGroup({});
  allServiceDelivery: any[] = []; // Add your service type here
  stageForm : FormGroup = new FormGroup({});
  steps = [
    { label: 'Basic Details', icon: 'bi-info-circle-fill' },
    { label: 'Stages', icon: 'bi-geo-alt-fill' },
    { label: 'Vehicles & Drivers', icon: 'bi-truck' },
    { label: 'Comments', icon: 'bi-chat-left-text-fill' },
    { label: 'Review', icon: 'bi-check-circle-fill' }
  ];
  deliveryPointsByCity: DeliveryPointDto[] =[];
  selectedDeliveryService: ServiceDeliveryDto={}
  etapeDto : EtapeDto={};
  isFormEtapeLoading: boolean = false;
  etapeDtos: EtapeDto[] = [];

  constructor(
    private servDeliveryService:ServiceDeliveryService,
    private dpService:DeliveryPointsService,
    private missionService:MissionService,
    private alertService: AlertService,
    private vehicleService: VehiculeService,
    private driversService: ChauffeurService,
    private fb: FormBuilder) {
    this.initForm();
    this.initStageForm();
  }

  ngOnInit() {
    this.loadInitialData();
  }

  initStageForm() {
    this.stageForm = new FormGroup({
      lieuDepart: new FormControl(),
      lieuArrivee: new FormControl(),
      scheduledDeliveryTime: new FormControl(),

    })
  }
  private findAllServicesDelivery() {
    this.servDeliveryService.findAll2().subscribe({
      next:(sdResponse)=>{
        this.allServiceDelivery=sdResponse;
      },error:(error)=>{
        console.log(error);
      }
    })
  }
  private findAllDeliveryPointsByCity(selectedCity :string){
    if(this.selectedDeliveryService){

      this.dpService.findAllByCity({city:selectedCity}).subscribe({
        next:(rep)=>{
          this.deliveryPointsByCity=rep;
        },
        error:(err)=>{
          console.log(err);
        }
      })
    }
  }

  private initForm() {
    this.missionForm = this.fb.group({
      titre: ['', Validators.required],
      volume: ['', [Validators.required, Validators.min(0)]],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      commentaire: [''],
      etapeDtos: this.fb.array([]),
      missionVehiclesDtos: this.fb.array([]),
      deliveryService:['']
    });
  }

  onServiceChange(selectedDeliveryService : ServiceDeliveryDto) {
/*    const select = event.target as HTMLSelectElement;
    const selectedService = this.allServiceDelivery.find(s => s.id === select.value);
    if (selectedService) {
      // Handle service selection
      console.log('Selected service:', selectedService);
    }*/
    console.log(selectedDeliveryService);
    if(selectedDeliveryService && selectedDeliveryService.pickUpLocation!==null){
      this.selectedDeliveryService = selectedDeliveryService;
      this.missionForm.get('titre')?.setValue("Mission - "+ selectedDeliveryService.destinationCity);
      this.stageForm.get('lieuDepart')?.setValue(selectedDeliveryService.pickUpLocation);
      this.etapeDto.lieuDepart = this.stageForm.value.lieuDepart;
      this.findAllDeliveryPointsByCity(selectedDeliveryService.destinationCity!);
    }else{
      this.stageForm.get('lieuDepart')?.setValue('');
    }
  }

  nextStep() {
    if (this.canProceed()) {
      this.currentStep++;
    }
  }

  previousStep() {
    if (this.currentStep > 0) {
      this.currentStep--;
    }
  }

  canProceed(): boolean {
    const form = this.missionForm;

    switch (this.currentStep) {
      case 0: {
        const titre = form.get('titre');
        const volume = form.get('volume');
        const dateDebut = form.get('dateDebut');
        const dateFin = form.get('dateFin');

        return !!(
          titre?.valid &&
          volume?.valid &&
          dateDebut?.valid &&
          dateFin?.valid
        );
      }
      case 1: {
/*        const etapeDtos = form.get('etapeDtos');
        return !!(etapeDtos?.value?.length > 0);*/
        const etapes = this.etapeDtos;
        return !!(etapes?.length > 0);
      }
      case 2: {
        const missionVehiclesDtos = form.get('missionVehiclesDtos');
        return !!(missionVehiclesDtos?.value?.length > 0);
      }
      case 3:
        return true; // Comments are optional
      default:
        return false;
    }
  }


  isFieldInvalid(fieldName: string): boolean {
    const field = this.missionForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }

  saveMission() {
    if (this.missionForm.valid) {
      const missionData: MissionDto = this.missionForm.value;
      console.log('Saving mission:', missionData);
      // Implement your save logic here
    }
  }

  private loadInitialData() {
    // Load your initial data here (services, vehicles, drivers, etc.)
    this.findAllServicesDelivery();
  }

  addEtape(): EtapeDto {
    const newDeparture = this.stageForm.value.lieuDepart;
    const newArrival = this.stageForm.value.lieuArrivee;

    const newTimeString = this.stageForm.value.scheduledDeliveryTime;

    if (!newTimeString) {
      this.alertService.alertError("Date departure is required.");
      return {};
    }

    const isDuplicate=this.etapeDtos.some(etape=>
      etape.lieuDepart === newDeparture &&
      etape.lieuArrivee === newArrival &&
      etape.scheduledDeliveryTime === newTimeString);

    if(isDuplicate){
      this.alertService.alertWaring("This departure, destination, and time combination already exists.");
      return {};
    }

    const newScheduledDeliveryTimeParts = newTimeString.split(':').map(Number); // ["12", "44"] -> [12, 44]
    const newScheduledDeliveryTime = new Date();
    newScheduledDeliveryTime.setHours(newScheduledDeliveryTimeParts[0], newScheduledDeliveryTimeParts[1], 0, 0);

    const hasInvalidTimeDifference = this.etapeDtos.some(etape => {
      const [existingHours, existingMinutes] = (etape.scheduledDeliveryTime as string).split(':').map(Number);
      const existingScheduledDeliveryTime = new Date();
      existingScheduledDeliveryTime.setHours(existingHours, existingMinutes, 0, 0);

      return Math.abs(newScheduledDeliveryTime.getTime() - existingScheduledDeliveryTime.getTime()) < 30 * 60 * 1000;
    });

    if(hasInvalidTimeDifference){
      this.alertService.alertWaring("The time difference between stages must be at least 30 minutes.")
      return {};
    }



    if(newDeparture && newArrival && newTimeString){
      this.etapeDto = this.stageForm.value;
      this.etapeDtos.push(this.etapeDto);
      //this.stageForm.reset();
      return this.etapeDto;
    } else{
      this.alertService.alertError("All filler are mandatory")
      return {};
    }
  }

  deleteEtape(index: number) {
      this.etapeDtos.splice(index,1);
  }
}
