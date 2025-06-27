import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from "../auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class RoleRedirectGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {

  }
  canActivate(){
   if(this.auth.isAdminUser()){
     return true;
   }else{
     this.router.navigate(['missions/w-planning']);
     return false;
   }
  }

}
