import {Component, OnInit} from '@angular/core';
import {AdresseDto} from "../../../../../ds-api/src/models/adresse-dto";
import {ChauffeurDto} from "../../../../../ds-api/src/models/chauffeur-dto";
import {PageResponseChauffeurDto} from "../../../../../ds-api/src/models/page-response-chauffeur-dto";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {AlertService} from "../../../../services/alert/alert.service";
import {ChauffeurService} from "../../../../../ds-api/src/services/chauffeur.service";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {
  ViewVehiculeModalComponent
} from "../../../vehicles/pages/vehicles/view-vehicule-modal/view-vehicule-modal.component";
import {ViewDriverModalComponent} from "./view-driver-modal/view-driver-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {StringUtilsService} from "../../../../services/string-utils/string-utils.service";

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.scss']
})
export class DriversComponent implements OnInit{

  address:AdresseDto={};
  selectedAddress:AdresseDto | undefined={};
  isAddNewFormSubmitted:boolean=false;
  driver:ChauffeurDto={};
  allDriversResponse: PageResponseChauffeurDto={};
  selectedDriver: ChauffeurDto={};
  page: number = 0;
  size: number = 5;



  constructor(
    private modalService: NgbModal,
    private confirmBoxService:ConfirmBoxEvokeService,
    private alertService:AlertService,
    private stringUtilsService:StringUtilsService,
    private chauffeurService:ChauffeurService,
    private router:Router
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
        },
        error:(err)=>{
          console.log(err);
        }
      }

    )
  }

  updateDriverStatus(id: number, isActive: boolean) {console.log("====================== "+ isActive);
    this.chauffeurService.updateIsActive3({
      id:id,isactive:isActive
    }).subscribe({
      next:(res)=>{

        this.getAllDrivers();
        this.alertService.alertSuccess("Driver successfully deactivated");
      },
      error:(err)=>{
        this.alertService.alertWarning("Driver deactivation failed");
      }
    })
  }

  handleDriverActivation(driver_id: number | undefined, isActive: boolean | undefined) {
    console.log("===========before=========== "+ isActive);
    const action = isActive? "disable" : "enable"; const updateIsActive : boolean = !(isActive);
    if (driver_id === undefined) {
      this.alertService.alertError('Driver ID is missing. Unable to delete driver.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to ${action} this vehicle?`, 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        console.log("============updateIsActive========== "+ updateIsActive);

        this.updateDriverStatus(driver_id,updateIsActive);
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
        this.driver.address=this.address;
        this.driver.enabled=false;
        console.log(this.driver.dateOfBirth);
        this.chauffeurService.save18({
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
        this.chauffeurService.save18({
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
      this.chauffeurService.save18({
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
    this.chauffeurService.findById18({
      id:id as number
    }).subscribe({
      next:(res)=>{
        this.selectedDriver=res;
        const dateString =  JSON.stringify(this.selectedDriver.dateOfBirth);
        this.selectedDriver.dateOfBirth=this.convertDate(JSON.parse(dateString))
        this.selectedAddress=this.selectedDriver.address
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

  goToNewDriver() {
    this.router.navigate(['drivers','new-driver']);
  }

  openDriverModal(driver: ChauffeurDto) {
    const modalRef = this.modalService.open(ViewDriverModalComponent, { size: 'xl' });
    modalRef.componentInstance.selectedDriver = driver;  // Pass data to modal
  }

  goToUpdateDriver(id: number) {
    this.router.navigate(['drivers','edit-driver',id])
  }

  handleDeleteDriver(driver_id: number | undefined) {
    if (driver_id === undefined) {
      this.alertService.alertError('Driver ID is missing. Unable to delete driver.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to delete this driver ?`, 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        this.deleteDriver(driver_id);
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : `Unable to delete driver`);
      }
    )
  }

  private deleteDriver(driver_id: number) {
    this.chauffeurService.delete16({
      id:driver_id
    }).subscribe({
      next:(res)=>{
        console.log(res);
        this.getAllDrivers();
        this.alertService.alertInfo("Driver deleted successfully.")
      },error:(err)=>{
        console.log(err);
        this.alertService.alertWarning("An error occurred while deleting the driver. Please try again.")
      }
    })
  }

  capitalizeName(name: string | undefined) {
    return this.stringUtilsService.capitalizeName(name!);
  }
}
