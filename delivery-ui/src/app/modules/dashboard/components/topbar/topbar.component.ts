import { Component } from '@angular/core';
import {
  AuthenticationEnregistrementService
} from "../../../../../ds-api/src/services/authentication-enregistrement.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent {

  constructor(
    private router:Router,
    private authService:AuthenticationEnregistrementService) {
  }
  logout() {
    this.router.navigate(['login'])
    this.authService.logout();
  }
}
