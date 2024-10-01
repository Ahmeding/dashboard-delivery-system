import {Component, OnInit} from '@angular/core';
import {VehiculesService} from "../../../services/vehicule/vehicules.service";
import {ActivatedRoute, Router} from "@angular/router";
import {VehiculeDto} from "../../../../ds-api/src/models/vehicule-dto";
import {Save$Params} from "../../../../ds-api/src/fn/vehicule/save";

@Component({
  selector: 'app-add-vehicule-page',
  templateUrl: './add-vehicule-page.component.html',
  styleUrl: './add-vehicule-page.component.scss'
})
export class AddVehiculePageComponent implements OnInit{

  vehiculeDto: VehiculeDto={};
  paramsVehicule :Save$Params={body:{}};
  typeCarburant : Array<string>=[];
  errorMsgs : Array<string>=[];

  constructor(
    private vehiculeService:VehiculesService,
    private router:Router,
    private activatedRoute: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.typeCarburant.push('Diesel','Essence','Electrique','Hybride')
  }

  saveVehicule(){
    this.paramsVehicule.body=this.vehiculeDto;
    this.vehiculeService.saveVehicule(this.paramsVehicule).subscribe(
      vehicule =>{
        this.router.navigate(['vehicules']);
      },error => {
        this.errorMsgs=error.error.errors;
      }
    );
  }

  selcttc($event: any){
    console.log("----"+$event)
  }
}
