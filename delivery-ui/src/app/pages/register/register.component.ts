import { Component } from '@angular/core';
import {SignupRequest} from "../../../ds-api/src/models/signup-request";
import {Router} from "@angular/router";
import {AuthenticationEnregistrementService} from "../../../ds-api/src/services/authentication-enregistrement.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  registerRequest: SignupRequest={
    email:'',
    firstName:'',
    lastName:'',
    password:'',
    role:'ADMIN'
  }
  errorsMsg :Array<string>=[];

  constructor(private router:Router,
              private registerService:AuthenticationEnregistrementService) {
  }

  login(){
    this.router.navigate(['login'])
  }
  register(){
    this.errorsMsg=[];
      this.registerService.signup(
        {
          body:this.registerRequest
        }
      ).subscribe({
        next:(res)=>{
          console.log(res.email);
          this.router.navigate(['login']);
        },
        error:(err)=>{

          console.log(err.error);
          this.errorsMsg=err.error.errors
          //this.errorsMsg.push("Error title : "+err.error.msg )
        }
      })
  }
}
