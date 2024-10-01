import { Injectable } from '@angular/core';
import {Router} from "@angular/router";

import {Signin$Params} from "../../../ds-api/src/fn/authentication-enregistrement/signin";
import {AuthenticationEnregistrementService} from "../../../ds-api/src/services/authentication-enregistrement.service";
import {Observable, of} from "rxjs";
import {JwtAuthenticationResponse} from "../../../ds-api/src/models/jwt-authentication-response";
import {UtilisateurDto} from "../../../ds-api/src/models/utilisateur-dto";
import {UtilisateurService} from "../../../ds-api/src/services/utilisateur.service";
import {FindByEmail$Params} from "../../../ds-api/src/fn/utilisateur/find-by-email";
import {Signup$Params} from "../../../ds-api/src/fn/authentication-enregistrement/signup";

@Injectable({
  providedIn: 'root'
})
export class UserService {



  constructor(
    private authenticationEnregistrement: AuthenticationEnregistrementService,

    private utilisateurService: UtilisateurService,
    private router: Router

  ) {}

  login(authenticationRequest: Signin$Params): Observable<JwtAuthenticationResponse>{
    return this.authenticationEnregistrement.signin(authenticationRequest);
  }

  register(signUpRequest: Signup$Params){
    return this.authenticationEnregistrement.signup(signUpRequest);
  }

  isUSerLoggedAndAccessTockenValid():boolean{
    if(localStorage.getItem("accessToken")){
      return true;
    }
    this.router.navigate(['login']);
    return false;
  }
  getUserByEmail(email?: FindByEmail$Params): Observable<UtilisateurDto> {
    if (email !== undefined) {
      return this.utilisateurService.findByEmail(email);
    }
     return of();
  }

  findAll(){
    return this.utilisateurService.findAll1();
  }

  setAccessToken(resp: JwtAuthenticationResponse):void{
    localStorage.setItem("accessToken", JSON.stringify(resp));
  }
  setConnectedUser(user: UtilisateurDto) {
    localStorage.setItem("connectedUser", JSON.stringify(user));
  }
  getConnectedUser() :UtilisateurDto {
     if(localStorage.getItem("connectedUser")&&localStorage.getItem("connectedUser")!=null){
        return JSON.parse(localStorage.getItem('connectedUser') as string);
    }
     return {};
  }
}
