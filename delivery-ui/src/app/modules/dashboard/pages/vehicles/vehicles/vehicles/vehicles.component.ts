import {Component, OnInit} from '@angular/core';
import {VehiculeService} from "../../../../../../../ds-api/src/services/vehicule.service";
import {VehiculeDto} from "../../../../../../../ds-api/src/models/vehicule-dto";
import {AlertService} from "../../../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {NgForm} from "@angular/forms";
import {PageResponseVehiculeDto} from "../../../../../../../ds-api/src/models/page-response-vehicule-dto";

@Component({
  selector: 'app-vehicles',
  templateUrl: './vehicles.component.html',
  styleUrls: ['./vehicles.component.scss']
})
export class VehiclesComponent implements OnInit{

  allVehicles:VehiculeDto[]=[];
  allVehiclesResponse: PageResponseVehiculeDto={};
  selectedVehicle: VehiculeDto={};
  newVehicle: VehiculeDto={};

  isFormAddNewSubmitted: boolean=false;
  page: number=0;
  size:number =5;

  constructor(
    private confirmBoxService:ConfirmBoxEvokeService,
    private alertService:AlertService,
    private vehicleService: VehiculeService) {
  }

  ngOnInit(): void {
    this.getAllVehicles();
  }
  private getAllVehicles() {
    this.vehicleService.findAllVehicules({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(vehicules)=>{
        this.allVehiclesResponse=vehicules;
      },
      error:(err)=>{
      console.log(err);
    }
    });
  }
  formatDateDDMMYYYY(datePMC: string | undefined) : string{
    const dateString= JSON.stringify(datePMC);
    const dateArray=JSON.parse(dateString);
    const year=dateArray[0];
    const month=dateArray[1].toString().padStart(2,'0');
    const day=dateArray[2].toString().padStart(2,'0');
    return `${day}-${month}-${year}`;
  }

  handleVehicleActivation(vehicle_id: number | undefined, isActive: boolean | undefined) {
    const action = isActive? "disable" : "enable";
    if (vehicle_id === undefined) {
      console.log("mission_id : "+ vehicle_id);
      this.alertService.alertError('Mission ID is missing. Unable to delete mission.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to ${action} this vehicle?`, '', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.vehicleService.updateIsActive(
            {
              id:vehicle_id,
              isactive:!isActive
            }
          ).subscribe(() => {
            this.getAllVehicles();
            this.alertService.alertSuccess(`${action} successfully completed`);
          });
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : `Unable to ${action} mission`);
      }
    )
  }
  addNewVehicle(form: NgForm) {
    form.form.markAllAsTouched();
    this.isFormAddNewSubmitted= true;
    console.log(this.newVehicle);
    console.log(form.valid);
    if(form.valid)
    {
      this.saveVehicle(this.newVehicle,form);
    }else{
      this.alertService.alertError('You must fill in the required fields.');
    }
  }
  private resetForm(form: NgForm) {
    form.resetForm();
    this.newVehicle={}
    this.isFormAddNewSubmitted=false;
  }
  deleteVehicle(vehicle_id: number) {
    if (vehicle_id === undefined) {
      this.alertService.alertError('Vehicle ID is missing. Unable to delete vehicle.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this vehicle?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.vehicleService.delete({id:vehicle_id}).subscribe(() => {
            this.getAllVehicles();
            this.alertService.alertSuccess('Deletion successfully completed');
          });
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : 'Unable to delete vehicle');
      }
    )
  }
  editVehicle(vehicle_id: number | undefined) {
    this.vehicleService.findById({
      id:vehicle_id as number
    }).subscribe({
      next:(res)=>{
        this.selectedVehicle=res;
        const stringDate=JSON.stringify(this.selectedVehicle.datePMC);
        this.selectedVehicle.datePMC=this.convertDateToInput(JSON.parse(stringDate))
        this.selectedVehicle.dateExpCtrlTec?this.selectedVehicle.dateExpCtrlTec=this.convertDateToInput(JSON.parse(stringDate)):'';

      }
    })
  }
  convertDateToInput(dateArray: number[] ) :string{
    const year= dateArray[0];
    const month= dateArray[1].toString().padStart(2,'0');
    const day= dateArray[2].toString().padStart(2,'0');
    return `${year}-${month}-${day}`;
  }
  saveEditingVehicle(editForm: NgForm) {
    editForm.form.markAllAsTouched();
    this.isFormAddNewSubmitted= true;
    if(editForm.valid){
      this.saveVehicle(this.selectedVehicle,editForm);
    }else{
      this.alertService.alertError('You must fill in the required fields.');
    }

  }
  private saveVehicle(vehicleToSave: VehiculeDto, form: NgForm) {
    this.vehicleService.save({
      body:vehicleToSave
    }).subscribe({
      next:(res)=>{
        console.log(res);
        this.alertService.alertSuccess("Vehicle saved successfully!");
        this.getAllVehicles();
        this.resetForm(form);
      },
      error:(err)=>{
        console.log(err);
        this.alertService.alertError("An error occurred while saving a vehicle.");
      }})
  }

  goToFirstPage() {
    this.page=0;
    this.getAllVehicles();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllVehicles();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllVehicles();
  }

  goToNextPage() {
    this.page++;
    this.getAllVehicles();
  }


  get  isLastPage():boolean{
    return this.page == this.allVehiclesResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allVehiclesResponse.totalPages as number -1;
  }
}
