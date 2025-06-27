import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../../../../../services/alert/alert.service";
import {DateUtilsService} from "../../../../../services/date-utils/date-utils.service";
import {EmploymentContractResponse} from "../../../../../../ds-api/src/models/employment-contract-response";
import {EmploymentContractRequest} from "../../../../../../ds-api/src/models/employment-contract-request";
import {DriverResponse} from "../../../../../../ds-api/src/models/driver-response";
import {EmploymentContractService} from "../../../../../../ds-api/src/services/employment-contract.service";
import {DriverService} from "../../../../../../ds-api/src/services/driver.service";

@Component({
  selector: 'app-new-contract',
  templateUrl: './new-contract.component.html',
  styleUrls: ['./new-contract.component.scss']
})
export class NewContractComponent  implements OnInit{

  selectedContract!: EmploymentContractResponse;
  contract!: EmploymentContractRequest;
  allDrivers: DriverResponse[]=[];
  /**---------------------**/
  contractForm: FormGroup= new FormGroup({});
  contractValue: EmploymentContractResponse={};
  cId:number |null=null;
  /**---------------------**/

  constructor(
    private router:Router,
    private route:ActivatedRoute,
    private contractService:EmploymentContractService,
    private driverService:DriverService,
    private alertService:AlertService,
    private dateUtils:DateUtilsService) {
  }

  ngOnInit(): void {
    this.getAllDrivers();
    this.initForm();


    this.cId=Number(this.route.snapshot.paramMap.get('id'));
    if(this.cId){
      this.loadRepairToEdit(this.cId);
    }
  }

  private getAllDrivers() {
    this.driverService.findAllDriver().subscribe({
      next:(allDriversResponse)=>{
        console.log("-------------> "+allDriversResponse.length);
        this.allDrivers=allDriversResponse  ;
        console.log(this.allDrivers.length);
        this.contractForm.patchValue({
          vehiculeDto: this.contractValue?.driverId || null
        });
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }
  initForm(){
    this.contractForm= new FormGroup({
      id: new FormControl(this.contractValue?.id || null), // Ensure id is included
      chauffeurDto: new FormControl(this.contractValue?.driverId || null,Validators.required),
      typeContrat: new FormControl(this.contractValue?.contractType || '',Validators.required),
      dateDebut: new FormControl(this.contractValue?.startDate || '',Validators.required),
      dateFin: new FormControl(this.contractValue?.endDate || '',Validators.required),
    });
  }

  loadRepairToEdit(rId:number){
    this.contractService.findEmploymentContractById({id:rId}).subscribe({
      next:(contractToEditResponse)=>{
        this.contractValue= contractToEditResponse;

        // Find and set the matching vehicle object reference
        const matchingDriver = this.allDrivers.find(d => d.id === this.contractValue.driverId);
        console.log(matchingDriver);
        this.contractForm.patchValue({
          vehiculeDto: matchingDriver, // Set the vehicle reference
          type: this.contractValue.contractType,
          dateDebut: this.dateUtils.setToInputDate(this.contractValue.startDate),
          dateFin: this.dateUtils.setToInputDate(this.contractValue.endDate),
        });
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  onSubmit() {
    if(this.contractForm.valid){
      const contractData: EmploymentContractRequest=this.contractForm.value;
      if(this.contractValue?.id){
        contractData.id =this.contractValue.id;
        console.log("updating");
        this.saveRepair(contractData,"Updating","updating");
      }else{
        this.saveRepair(contractData,"Registration","registering");
        console.log("register");
      }
    }
  }

  saveRepair(contractData: EmploymentContractRequest, successMsg: string, errorMsg: string){
    this.contractService.saveEmploymentContract({body:contractData}).subscribe({
      next:(response)=>{
        this.alertService.alertSuccess(successMsg+" completed successfully!")
        this.router.navigate(['dirvers','contracts']);
      },
      error:(err)=>{
        console.log(err);
        this.alertService.alertError("An error occurred while "+errorMsg+" the reparation.")

      }
    });
  }
  navigateToContractList() {
    this.router.navigate(['drivers','contracts'])
  }
}
