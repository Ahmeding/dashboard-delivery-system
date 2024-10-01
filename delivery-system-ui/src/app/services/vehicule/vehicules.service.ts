import { Injectable } from '@angular/core';
import {VehiculeService} from "../../../ds-api/src/services/vehicule.service";
import {VehiculeDto} from "../../../ds-api/src/models/vehicule-dto";
import {Observable, of} from "rxjs";
import {Save$Params} from "../../../ds-api/src/fn/vehicule/save";
import {FindById$Params} from "../../../ds-api/src/fn/vehicule/find-by-id";

@Injectable({
  providedIn: 'root'
})
export class VehiculesService {

  constructor(
    private vehiculeService: VehiculeService
  ) { }

  saveVehicule(params : Save$Params): Observable<VehiculeDto>{
    return this.vehiculeService.save(params);
  }

  findVehiculeByID(idVehicle: FindById$Params): Observable<any>{
    if(idVehicle){
    return this.vehiculeService.findById$Response(idVehicle);
    }
    return of();
  }
}
