import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MenuMissionCardService {
private openMenuIdSource =  new Subject<number |null>();
openMenuId$= this.openMenuIdSource.asObservable();
  constructor() { }
  setOpenMenu(missionId: number | null){
   this.openMenuIdSource.next(missionId);
  }
}
