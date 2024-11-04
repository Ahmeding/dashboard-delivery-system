import { Injectable } from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(private toastrService: ToastrService ) { }


  alertInfo(message: string){
    this.toastrService.info(message , "Infos!")
  }
  alertWaring(message: string){
    this.toastrService.warning(message , "Warning!")
  }
  alertError(message: string){
    this.toastrService.error(message , "Error!")
  }
  alertSuccess(message: string){
    this.toastrService.success(message , "Success!")
  }

}
