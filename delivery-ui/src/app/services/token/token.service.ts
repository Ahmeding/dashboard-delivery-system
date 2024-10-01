import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  set token(token:string){
    localStorage.setItem('token',token);
  }
  get token(){
    return localStorage.getItem('token') as string;
  }

    isTokenNotValid() {
        return !this.isTockenValid();
    }

  private isTockenValid() {
    const token:string=this.token;
    if(!token){
      return false;
    }
    // decode the token
    const jwtHelper = new JwtHelperService();
    //checkexpiredDate
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if(isTokenExpired){
      localStorage.clear();
      return false;
    }
    return true;
  }
}
