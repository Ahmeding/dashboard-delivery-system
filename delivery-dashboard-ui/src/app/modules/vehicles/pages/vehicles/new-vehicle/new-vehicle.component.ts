import {Component, Input, OnInit} from '@angular/core';
import {VehicleDto} from "../../../../../../ds-api/src/models/vehicle-dto";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {VehiculeService} from "../../../../../../ds-api/src/services/vehicule.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {HttpClient} from "@angular/common/http";
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
  vehicleValue: VehicleDto ={};  // Input for selected vehicle when editing
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
              private vehicleService: VehiculeService,
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
        marque : new FormControl(this.vehicleValue?.marque||'',Validators.required),
        modele : new FormControl(this.vehicleValue?.modele||'',Validators.required),
        immatriculation : new FormControl(this.vehicleValue?.immatriculation||'',[Validators.required, Validators.pattern(/^[A-Z]{2}-\d{3}-[A-Z]{2}$/)]),
        numVin : new FormControl(this.vehicleValue?.numVin||'',[Validators.required,Validators.pattern(/^[A-HJ-NPR-Z0-9]{17}$/)]),
        datePMC : new FormControl(this.formatDateToSetInInputDateType(this.vehicleValue?.datePMC)||'',Validators.required),
        kilometrage : new FormControl(this.vehicleValue?.kilometrage||null,[Validators.required,Validators.minLength(2),Validators.maxLength(7)]),
        volume : new FormControl(this.vehicleValue?.volume||null,[Validators.required,Validators.minLength(2),Validators.maxLength(3)]),
        typecarburant : new FormControl(this.vehicleValue?.typecarburant||'',Validators.required),
        dateExpCtrlTec : new FormControl(this.vehicleValue?.dateExpCtrlTec||'',Validators.required),

      })
  }

  private loadVehicleToEditing(vId: number) {
    this.isNewVehicle=false;
    this.vehicleService.findById1({
      id:vId
    }).subscribe({
      next:(responseV)=>{
        this.vehicleValue=responseV;
        if (this.vehicleValue.datePMC) {
          console.log("Ensure datePMC is properly formatted before patching");
          this.vehicleValue.datePMC = this.formatDateToSetInInputDateType(this.vehicleValue.datePMC);
          this.vehicleValue.dateExpCtrlTec = this.formatDateToSetInInputDateType(this.vehicleValue.dateExpCtrlTec);
        }
        this.vehicleForm.patchValue(this.vehicleValue as VehicleDto);
        this.onTypeChange();
        this.onMarqueChange();
      },error:(err)=>{
        console.log(err);
      }
    });
  }

  formatDateToSetInInputDateType(datePMC: string | undefined):string{
    let convertedDate: string = '';
    if (datePMC) {
      console.log("Original DatePMC: " + JSON.stringify(datePMC));  // Check what the input looks like
      const dateArray = JSON.stringify(datePMC)
      convertedDate=this.convertDate(JSON.parse(dateArray));
      console.log("formateeeed datePMC"+convertedDate);
    }
    return convertedDate;
}

  convertDate(dateArray: number[] ) :string{
    const year= dateArray[0];
    const month= dateArray[1].toString().padStart(2,'0');
    const day= dateArray[2].toString().padStart(2,'0');
    return `${year}-${month}-${day}`;
  }

  onSubmit() {
    if(this.vehicleForm.valid){
      const vData: VehicleDto = this.vehicleForm.value;
      if(this.vehicleValue?.id){
        this.saveVehicle(vData,"Updating","updating");
      }else{
        this.saveVehicle(vData,"Registration","registering");
      }
    }

  }

  private saveVehicle(vehicleValue: VehicleDto, successMsg: string, errorMesg:string) {
    this.vehicleService.save1({body:vehicleValue}).subscribe({
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
    if (this.vehicleValue.marque) {
      this.vehicleForm.controls['marque'].setValue(this.vehicleValue.marque);
      this.onMarqueChange();
    } else {
      this.vehicleForm.controls['marque'].setValue('');
      this.vehicleForm.controls['modele'].setValue('');
      this.models = [];
    }

  }
  onMarqueChange(){
    this.vehicleForm.controls['modele'].setValue('');
    const selectMarque = this.vehicleForm.controls['marque']?.value;
    const vehicule= this.marques.find(v=>v.marque === selectMarque);
    this.models = vehicule?vehicule.models:[];
    if (this.vehicleValue.modele) {
      this.vehicleForm.controls['modele'].setValue(this.vehicleValue.modele);
    }

  }


}
