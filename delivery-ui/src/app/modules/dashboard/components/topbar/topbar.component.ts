import { Component } from '@angular/core';
import {
  AuthenticationEnregistrementService
} from "../../../../../ds-api/src/services/authentication-enregistrement.service";
import {Router} from "@angular/router";
import {TokenService} from "../../../../services/token/token.service";
import {UtilisateurDto} from "../../../../../ds-api/src/models/utilisateur-dto";

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent {

  constructor(
    private router:Router,
    private tokenService:TokenService,
    private authService:AuthenticationEnregistrementService) {
  }
   logout() {
    this.router.navigate(['login'])
    this.tokenService.logout();
  }

  get user():string{
    return localStorage.getItem('user') as string;

  }
}
