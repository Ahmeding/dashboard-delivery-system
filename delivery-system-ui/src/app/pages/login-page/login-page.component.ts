import {Component, OnInit} from '@angular/core';
import {SigninRequest} from "../../../ds-api/src/models/signin-request";
import {Signin$Params} from "../../../ds-api/src/fn/authentication-enregistrement/signin";
import {UserService} from "../../services/user/user.service";
import {Router} from "@angular/router";
import {FindByEmail$Params} from "../../../ds-api/src/fn/utilisateur/find-by-email";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
  host: {ngSkipHydration: 'true'},
})
export class LoginPageComponent implements OnInit{


  signinRequest: SigninRequest={};
  signinRequest$parms: Signin$Params={body:{}};
  userEmail:FindByEmail$Params={email:''};
  errorMessage: string='';

  ngOnInit(): void {
  }
  constructor(
    private userService: UserService,
    private router: Router
    ) {}

    login(){
      this.signinRequest$parms.body=this.signinRequest;
      this.userService.login(this.signinRequest$parms).subscribe((data) => {
        this.userService.setAccessToken(data);

        console.log("----------------> {" +this.signinRequest.email);
        this.getUserByEmail();

        this.router.navigate(['']);
      }, error => {
        this.errorMessage = 'Login et / ou mot de passe incorrecte';
      });

  }
getUserByEmail(){
  if (this.signinRequest.email != null) {
    console.log("----------------> {" +this.signinRequest.email);
    this.userEmail.email = this.signinRequest.email
  }
  this.userService.getUserByEmail(this.userEmail).subscribe(

    user=>{
      console.log("-------getUserByEmail---------> {" +user);
      this.userService.setConnectedUser(user);
    }
  );
}
}


