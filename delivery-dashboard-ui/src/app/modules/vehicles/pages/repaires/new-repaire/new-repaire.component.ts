import {Component, OnInit} from '@angular/core';
import {ReparationDto} from "../../../../../../ds-api/src/models/reparation-dto";
import {FormControl, FormGroup, NgForm, Validators} from "@angular/forms";
import {VehiculeDto} from "../../../../../../ds-api/src/models/vehicule-dto";
import {VehiculeService} from "../../../../../../ds-api/src/services/vehicule.service";
import {ReparationService} from "../../../../../../ds-api/src/services/reparation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../../../../../services/alert/alert.service";
import {DateUtilsService} from "../../../../../services/date-utils/date-utils.service";

@Component({
  selector: 'app-new-repaire',
  templateUrl: './new-repaire.component.html',
  styleUrls: ['./new-repaire.component.scss']
})
export class NewRepaireComponent implements OnInit{

  typesRepair:string[]=['MECANIQUE' , 'ELECTRIQUE' , 'CARROSSERIE' , 'VIDANGE_COMPLET']
  //;

  repair: ReparationDto = {};
  allVehicles: VehiculeDto[]=[];
  /**---------------------**/
  repairForm: FormGroup= new FormGroup({});
  repairValue: ReparationDto={};
  rId:number |null=null;
  constructor(
    private router:Router,
    private route:ActivatedRoute,
    private repairService:ReparationService,
    private vehiculeService:VehiculeService,
               private alertService:AlertService,
               private dateUtils:DateUtilsService) {
  }

  ngOnInit(): void {
    this.getAllVehicles();
    this.initForm();


  this.rId=Number(this.route.snapshot.paramMap.get('id'));
  if(this.rId){
    this.loadRepairToEdit(this.rId);
  }
  }

  private getAllVehicles() {
    this.vehiculeService.findAll1().subscribe({
      next:(allVehiclesRep)=>{
        this.allVehicles=allVehiclesRep;
        this.repairForm.patchValue({
          vehiculeDto: this.repairValue?.vehiculeDto || null
        });
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }


  initForm(){
    this.repairForm= new FormGroup({
      id: new FormControl(this.repairValue?.id || null), // Ensure id is included
      vehiculeDto: new FormControl(this.repairValue?.vehiculeDto || null,Validators.required),
      type: new FormControl(this.repairValue?.type || '',Validators.required),
      dateDebut: new FormControl(this.repairValue?.dateDebut || '',Validators.required),
      dateFin: new FormControl(this.repairValue?.dateFin || '',Validators.required),
      dateProchainRep: new FormControl(this.repairValue?.dateProchainRep || ''),
      commentaire: new FormControl(this.repairValue?.commentaire),
    });
  }

  loadRepairToEdit(rId:number){
    this.repairService.findById4({id:rId}).subscribe({
      next:(repairToEditResponse)=>{
        this.repairValue= repairToEditResponse;
   /*     if(this.repairValue.dateDebut){
          this.repairValue.dateDebut=this.dateUtils.setToInputDate(this.repairValue.dateDebut);
        }
        if(this.repairValue.dateFin){
          this.repairValue.dateFin=this.dateUtils.setToInputDate(this.repairValue.dateFin)
        }
        if(this.repairValue.dateProchainRep){
          this.repairValue.dateProchainRep=this.dateUtils.setToInputDate(this.repairValue.dateProchainRep)
        }

        this.repairForm.patchValue(this.repairValue as ReparationDto);
        this.repairForm.controls['vehiculeDto'].setValue(this.repairValue.vehiculeDto);
*/
        // Find and set the matching vehicle object reference
        console.log(this.repairValue.vehiculeDto?.id);
        const matchingVehicle = this.allVehicles.find(v => v.id === this.repairValue.vehiculeDto?.id);
        console.log(matchingVehicle);
        this.repairForm.patchValue({

          vehiculeDto: matchingVehicle, // Set the vehicle reference
          type: this.repairValue.type,
          dateDebut: this.dateUtils.setToInputDate(this.repairValue.dateDebut),
          dateFin: this.dateUtils.setToInputDate(this.repairValue.dateFin),
          dateProchainRep: this.dateUtils.setToInputDate(this.repairValue.dateProchainRep),
          commentaire: this.repairValue.commentaire
        });

        console.log(this.repairValue);
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  onSubmit() {
    if(this.repairForm.valid){
      const repairData: ReparationDto=this.repairForm.value;

      if(this.repairValue?.id){
        repairData.id =this.repairValue.id;
        console.log("updating");
        this.saveRepair(repairData,"Updating","updating");
      }else{
        this.saveRepair(repairData,"Registration","registering");
        console.log("register");
      }
    }
  }

  saveRepair(repairData: ReparationDto, successMsg: string, errorMsg: string){
  this.repairService.save4({body:repairData}).subscribe({
    next:(response)=>{
      this.alertService.alertSuccess(successMsg+" completed successfully!")
      this.router.navigate(['vehicles/repairs']);
    },
    error:(err)=>{
      console.log(err);
      this.alertService.alertError("An error occurred while "+errorMsg+" the reparation.")

    }
  });
  }
  navigateToRepairList() {
    this.router.navigate(['vehicles','repairs'])
  }


}
