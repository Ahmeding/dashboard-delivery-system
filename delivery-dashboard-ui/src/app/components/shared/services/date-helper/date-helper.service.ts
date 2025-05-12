import { Injectable } from '@angular/core';
import {isSameDay, startOfDay} from "date-fns";

@Injectable({
  providedIn: 'root'
})
export class DateHelperService {

  constructor() { }

  isInputDateSameDay(date:Date):boolean{
    const todayDate = startOfDay(new Date());
    const inputDate = startOfDay(date);
    return isSameDay(todayDate,inputDate)
  }
}
