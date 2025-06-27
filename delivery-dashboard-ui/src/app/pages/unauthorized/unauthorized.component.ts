import { Component } from '@angular/core';
import {AuthService} from "../../services/auth/auth.service";
import {TokenService} from "../../services/token/token.service";

@Component({
  selector: 'app-unauthorized',
  templateUrl: './unauthorized.component.html',
  styleUrls: ['./unauthorized.component.scss']
})
export class UnauthorizedComponent {

constructor(public authService:AuthService,private tokenService:TokenService) {
}

  clearAll() {
    this.tokenService.logout();
  }
}
