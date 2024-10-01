import { Component } from '@angular/core';
import {AuthenticationEnregistrementService} from "../../../ds-api/src/services/authentication-enregistrement.service";
import {SigninRequest} from "../../../ds-api/src/models/signin-request";
import {Router} from "@angular/router";
import {TokenService} from "../../services/token/token.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

    isLoading!: boolean;
    authRequest : SigninRequest= {email:'',password:''};
    errorsMsg : Array<string>=[];
    constructor(private router:Router,
                private authServices: AuthenticationEnregistrementService,
                private tokenService:TokenService,
                private toastrServive: ToastrService) {
    }

    login(){
      this.isLoading = true;
      this.authServices.logout();
      this.errorsMsg=[];
      this.authServices.signin({
        body: this.authRequest
      }).subscribe({
        next:(res)=>{
          this.tokenService.token =res.token as string;
          this.isLoading = false;
          this.router.navigate(['/']);
        },
        error:(err)=>{
          console.log(err);
          if(err.error.errors){
            this.toastrServive.warning(err.error.msg, "Oops!")
            this.errorsMsg=err.error.errors;//.validationMessages
            this.isLoading = false;
          }
        }
      })
    }

  register() {
    this.router.navigate(['register']);
  }
}
