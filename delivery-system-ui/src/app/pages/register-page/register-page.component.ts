import { Component } from '@angular/core';
import {Signup$Params} from "../../../ds-api/src/fn/authentication-enregistrement/signup";
import {UserService} from "../../services/user/user.service";
import {Router} from "@angular/router";
import {UtilisateurDto} from "../../../ds-api/src/models/utilisateur-dto";

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.scss'
})
export class RegisterPageComponent {

  signupRequest: Signup$Params={body:{}};
  successMsg:string ='';
  errorsMsg: Array<string>=[];
  userDto:UtilisateurDto={}

  constructor(
    private userService: UserService,
    private router:Router
    ) {}

  signup(){
    this.userService.register(this.signupRequest).subscribe(
      userDto => {
        this.successMsg=" l'inscription a été effectué avec succssé!"
        this.router.navigate(['password']);
    },error=>{

        this.errorsMsg = error.error.errors;
    }
    );
  }
}
