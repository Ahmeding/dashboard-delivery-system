import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../../../../../services/alert/alert.service";
import {HttpClient} from "@angular/common/http";
import {VehicleRequest} from "../../../../../../ds-api/src/models/vehicle-request";
import {VehicleService} from "../../../../../../ds-api/src/services/vehicle.service";
import {VehicleResponse} from "../../../../../../ds-api/src/models/vehicle-response";
interface Vehicle {
  marque: string;
  logo: string;
  type: string;
  models: string[];
}

@Component({
  selector: 'app-new-vehicle',
  templateUrl: './new-vehicle.component.html',
  styleUrls: ['./new-vehicle.component.scss']
})
export class NewVehicleComponent implements OnInit{
  vehicleValue!: VehicleRequest;  // Input for selected vehicle when editing
  vId:number | null=null;
  /**/
  types: string[] = [];
  marques: any[] = [];
  vehicles: Vehicle[] = [];
  models: string[] = [];
  /**/
  vehicleForm!: FormGroup;
  isLoading:boolean=false;
  carburantOptions = ['GASOIL', 'ESSENCE', 'ELECTRIC', 'HYBRIDE'];
  private isNewVehicle: boolean =true;

  constructor(
    private route:ActivatedRoute,
    private http: HttpClient,private router:Router,
              private vehicleService: VehicleService,
              private alertService:AlertService) {
  }


  ngOnInit(): void {
    this.initForm();
    this.loadData();

    this.onTypeChange();
    this.onMarqueChange();

    this.vId= Number(this.route.snapshot.paramMap.get('id'));
    if(this.vId){
      this.loadVehicleToEditing(this.vId);
    }
  }


  private initForm() {
      this.vehicleForm=new FormGroup({
        vehicleType : new FormControl(this.vehicleValue?.vehicleType|| '',Validators.required),
        brand : new FormControl(this.vehicleValue?.brand||'',Validators.required),
        model : new FormControl(this.vehicleValue?.model||'',Validators.required),
        registrationNbr : new FormControl(this.vehicleValue?.registrationNbr||'',[Validators.required, Validators.pattern(/^[A-Z]{2}-\d{3}-[A-Z]{2}$/)]),
        vinNbr : new FormControl(this.vehicleValue?.vinNbr||'',[Validators.required,Validators.pattern(/^[A-HJ-NPR-Z0-9]{17}$/)]),
        dfcDate : new FormControl(this.vehicleValue?.dfcDate||'',Validators.required),
        mileage : new FormControl(this.vehicleValue?.mileage||null,[Validators.required,Validators.minLength(2),Validators.maxLength(7)]),
        volumeCapacity : new FormControl(this.vehicleValue?.volumeCapacity||null,[Validators.required,Validators.minLength(0),Validators.maxLength(3)]),
        weightCapacity : new FormControl(this.vehicleValue?.weightCapacity||null,[Validators.required,Validators.minLength(0),Validators.maxLength(3)]),
        energyType : new FormControl(this.vehicleValue?.energyType||'',Validators.required),
        ctExpDate : new FormControl(this.vehicleValue?.ctExpDate||'',Validators.required),

      })
  }

  private loadVehicleToEditing(vId: number) {
    this.isNewVehicle=false;
    this.vehicleService.findVehicleById({
      id:vId
    }).subscribe({
      next:(responseV)=>{
        this.vehicleValue=responseV;
        this.vehicleForm.patchValue(this.vehicleValue as VehicleResponse);
        this.onTypeChange();
        this.onMarqueChange();
      },error:(err)=>{
        console.log(err);
      }
    });
  }



  onSubmit() {
    if(this.vehicleForm.valid){
      const vData: VehicleRequest = this.vehicleForm.value;
      if(this.vehicleValue?.id){
        this.saveVehicle(vData,"Updating","updating");
      }else{
        this.saveVehicle(vData,"Registration","registering");
      }
    }

  }

  private saveVehicle(vehicleValue: VehicleRequest, successMsg: string, errorMesg:string) {
    this.vehicleService.saveVehicle({body:vehicleValue}).subscribe({
      next:(response)=>{
        console.log(response);
        this.alertService.alertSuccess(successMsg+" completed successfully!")
        this.router.navigate(['vehicles/all']);
      },error:(err)=>{
        console.log(err);
        this.alertService.alertError("An error occurred while "+errorMesg+" the vehicle.")

      }
    })
  }
  navigateToVehicleList() {
    this.router.navigate(['vehicles','all'])
    console.log("tolist");
  }
  onGenerateVinClick() {
    this.vehicleForm.controls['numVin'].setValue(this.generateVin())
  }
  private generateVin() {
    const characters='ABCDEFGHJKLMNPRSTUVWXYZ0123456789';
    let vin = '';
    for(let i=0;i<17;i++){
      vin+=characters.charAt(Math.floor(Math.random()*characters.length));
    }
    return vin;
  }
  generateLicensePlate(): string {
    const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const numbers = '0123456789';

    const randomLetter = () => letters.charAt(Math.floor(Math.random() * letters.length));
    const randomDigit = () => numbers.charAt(Math.floor(Math.random() * numbers.length));

    // Generate format "AB-123-CD"
    return `${randomLetter()}${randomLetter()}-${randomDigit()}${randomDigit()}${randomDigit()}-${randomLetter()}${randomLetter()}`;
  }
  onGeneratelicensePlateClick(): void {
    this.vehicleForm.controls['immatriculation'].setValue(this.generateLicensePlate())
  }


  loadData(){
    this.http.get<any[]>('/assets/vehicles.json').subscribe(data=>{
      this.vehicles=data;
      this.types= Array.from(new Set(this.vehicles.map(v=>v.type)));
    })
  }
  onTypeChange(){
    const selectedType = this.vehicleForm.get('vehicleType')?.value;
    this.marques = this.vehicles.filter(v=>v.type === selectedType);
    // Set marque and reset modele if vehicleValue has marque
    if (this.vehicleValue.brand) {
      this.vehicleForm.controls['brand'].setValue(this.vehicleValue.brand);
      this.onMarqueChange();
    } else {
      this.vehicleForm.controls['brand'].setValue('');
      this.vehicleForm.controls['model'].setValue('');
      this.models = [];
    }

  }
  onMarqueChange(){
    this.vehicleForm.controls['model'].setValue('');
    const selectMarque = this.vehicleForm.controls['marque']?.value;
    const vehicule= this.marques.find(v=>v.marque === selectMarque);
    this.models = vehicule?vehicule.models:[];
    if (this.vehicleValue.model) {
      this.vehicleForm.controls['model'].setValue(this.vehicleValue.model);
    }

  }


}
