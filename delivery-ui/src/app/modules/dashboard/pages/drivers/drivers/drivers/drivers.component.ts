import {Component, OnInit} from '@angular/core';
import {ChauffeurService} from "../../../../../../../ds-api/src/services/chauffeur.service";
import {ChauffeurDto} from "../../../../../../../ds-api/src/models/chauffeur-dto";
import {AlertService} from "../../../../../../services/alert/alert.service";
import {AdresseDto} from "../../../../../../../ds-api/src/models/adresse-dto";
import {DatePipe} from "@angular/common";
import {NgForm} from "@angular/forms";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {PageResponseChauffeurDto} from "../../../../../../../ds-api/src/models/page-response-chauffeur-dto";

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.scss'],
})
export class DriversComponent implements OnInit{

  address:AdresseDto={};
  selectedAddress:AdresseDto | undefined={};
  isAddNewFormSubmitted:boolean=false;
  driver:ChauffeurDto={};

  allDrivers:ChauffeurDto[]=[];
  allDriversResponse: PageResponseChauffeurDto={};
  selectedDriver: ChauffeurDto={};
  page: number = 0;
  size: number = 5;



  constructor(
    private confirmBoxService:ConfirmBoxEvokeService,
    private alertService:AlertService,
    private chauffeurService:ChauffeurService
  ) {
  }

  ngOnInit(): void {
    this.getAllDrivers();
  }

  getAllDrivers(){
    this.chauffeurService.findAllDrivers({
      page: this.page,
      size: this.size
    }).subscribe({
      next:(drivers)=>{
        this.allDriversResponse= drivers;
        console.log(this.allDriversResponse.content?.length);
        console.log(drivers.content?.length);
        console.log(drivers.content);
      },
      error:(err)=>{
        console.log(err);
      }
      }

    )
  }

  updateDriverStatus(id: number, isActive: boolean | undefined) {
    this.chauffeurService.updateIsActive2({
      id:id,
      isactive: !isActive
    }).subscribe({
      next:(res)=>{
        console.log(res);
        this.getAllDrivers();
        this.alertService.alertSuccess("Driver successfully deactivated");
      },
      error:(err)=>{
        console.log(err);
        this.alertService.alertWaring("Driver deactivation failed");
      }
    })
  }

  handleDriverActivation(driver_id: number | undefined, isActive: boolean | undefined) {
    const action = isActive? "disable" : "enable";
    if (driver_id === undefined) {
      this.alertService.alertError('Driver ID is missing. Unable to delete driver.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to ${action} this vehicle?`, 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
          this.updateDriverStatus(driver_id,isActive);
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : `Unable to ${action} mission`);
      }
    )
  }


  addNewDriver(form: NgForm) {
    form.form.markAllAsTouched();
    this.isAddNewFormSubmitted = true;
    if(form.valid){
    if(this.driver.id!==null){
      this.driver.adresseDto=this.address;
      this.driver.isActive=false;
      console.log(this.driver.dateNaissance);
      this.chauffeurService.save11$Response({
        body:this.driver
      }) .subscribe({
        next:()=>{
          this.getAllDrivers();
          this.alertService.alertSuccess("Driver saved successfully!")
          this.resetForm(form);
        },
        error:(err)=>{
          console.log(err);
          this.alertService.alertError("Failed to save the driver.")
        }
      })
    }else{
      this.chauffeurService.save11$Response({
        body:this.driver
      }) .subscribe({
        next:()=>{
          this.getAllDrivers();
          this.alertService.alertSuccess("Driver editing successfully!")
        },
        error:(err)=>{
          console.log(err);
          this.alertService.alertError("Failed to edit the driver.")
        }
      })
    }
}else {
      this.alertService.alertError('You must fill in the required fields.');
    }
  }

  updateDriver(driver: ChauffeurDto, editForm: NgForm) {
    this.isAddNewFormSubmitted = true;
    editForm.form.markAllAsTouched();

    if(editForm.valid){
      this.chauffeurService.save11({
        body:driver
      }).subscribe({
        next:(res)=>{
          console.log(res);
          this.getAllDrivers();
          this.resetForm(editForm);
          this.alertService.alertSuccess("Driver editing successfully!");
          setTimeout(()=>{
              window.location.reload();
          },10)
        },error:(err)=>{
          this.alertService.alertError("An error occurred while saving a driver");
          console.log(err);
        }
      })
    }else{
      this.alertService.alertError('You must fill in the required fields.');
    }

  }

  driverEdit(id: number | undefined) {
    this.chauffeurService.findById11({
      id:id as number
    }).subscribe({
      next:(res)=>{
        this.selectedDriver=res;
        const dateString =  JSON.stringify(this.selectedDriver.dateNaissance);
        this.selectedDriver.dateNaissance=this.convertDate(JSON.parse(dateString))
        this.selectedAddress=this.selectedDriver.adresseDto
      },
      error:(err)=>{
        this.alertService.alertError("error no driver with this id : " + id)
        console.log(err);
      }
    });
  }

  convertDate(dateArray: number[] ) :string{
    const year= dateArray[0];
    const month= dateArray[1].toString().padStart(2,'0');
    const day= dateArray[2].toString().padStart(2,'0');
    return `${year}-${month}-${day}`;
  }

  private resetForm(form: NgForm) {
    form.resetForm();  // Reset the form state and clear input values
    this.driver = {};   // Reset the driver object to its initial state
    this.isAddNewFormSubmitted = false; // Reset the submitted state
  }

  goToFirstPage() {
    this.page=0;
    this.getAllDrivers();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllDrivers();
  }

  goToNextPage() {
    this.page++;
    this.getAllDrivers();
  }

  goToLastPage() {
    this.page=this.allDriversResponse.totalPages as number -1;
    this.getAllDrivers();
  }

  goToPage(index: number) {
      this.page = index;
      this.getAllDrivers();
  }

  get isLastPage():boolean{
    return this.page == this.allDriversResponse.totalPages as number -1;
  }
}
