import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, NgForm, Validators} from "@angular/forms";
import {ContratDto} from "../../../../../../ds-api/src/models/contrat-dto";
import {VehicleDto} from "../../../../../../ds-api/src/models/vehicle-dto";
import {ActivatedRoute, Router} from "@angular/router";
import {VehiculeService} from "../../../../../../ds-api/src/services/vehicule.service";
import {AlertService} from "../../../../../services/alert/alert.service";
import {DateUtilsService} from "../../../../../services/date-utils/date-utils.service";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {ChauffeurService} from "../../../../../../ds-api/src/services/chauffeur.service";
import {ContratService} from "../../../../../../ds-api/src/services/contrat.service";
import {CreatedByUserDto} from "../../../../../../ds-api/src/models/created-by-user-dto";

@Component({
  selector: 'app-new-contract',
  templateUrl: './new-contract.component.html',
  styleUrls: ['./new-contract.component.scss']
})
export class NewContractComponent  implements OnInit{

  selectedContract: ContratDto = {};
  contract: ContratDto = {};
  allDrivers: ChauffeurDto[]=[];
  /**---------------------**/
  contractForm: FormGroup= new FormGroup({});
  contractValue: ContratDto={};
  cId:number |null=null;
  /**---------------------**/

  constructor(
    private router:Router,
    private route:ActivatedRoute,
    private contractService:ContratService,
    private driverService:ChauffeurService,
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
    this.driverService.findAll18().subscribe({
      next:(allDriversResponse)=>{
        console.log("-------------> "+allDriversResponse.length);
        this.allDrivers=allDriversResponse  ;
        console.log(this.allDrivers.length);
        this.contractForm.patchValue({
          vehiculeDto: this.contractValue?.chauffeurDto || null
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
      chauffeurDto: new FormControl(this.contractValue?.chauffeurDto || null,Validators.required),
      typeContrat: new FormControl(this.contractValue?.typeContrat || '',Validators.required),
      dateDebut: new FormControl(this.contractValue?.dateDebut || '',Validators.required),
      dateFin: new FormControl(this.contractValue?.dateFin || '',Validators.required),
    });
  }

  loadRepairToEdit(rId:number){
    this.contractService.findById15({id:rId}).subscribe({
      next:(contractToEditResponse)=>{
        this.contractValue= contractToEditResponse;

        // Find and set the matching vehicle object reference
        const matchingDriver = this.allDrivers.find(d => d.id === this.contractValue.chauffeurDto?.id);
        console.log(matchingDriver);
        this.contractForm.patchValue({
          vehiculeDto: matchingDriver, // Set the vehicle reference
          type: this.contractValue.typeContrat,
          dateDebut: this.dateUtils.setToInputDate(this.contractValue.dateDebut),
          dateFin: this.dateUtils.setToInputDate(this.contractValue.dateFin),
        });
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  onSubmit() {
    if(this.contractForm.valid){
      const contractData: ContratDto=this.contractForm.value;
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

  saveRepair(contractData: ContratDto, successMsg: string, errorMsg: string){
    this.contractService.save15({body:contractData}).subscribe({
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
