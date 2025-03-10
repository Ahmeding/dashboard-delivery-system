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
    console.log("TOKEN--> : "+isTokenExpired);
    if(isTokenExpired){
      localStorage.clear();
      return false;
    }
    return true;
  }

  saveLoggedUser(token : string){
    const jwtHelper = new JwtHelperService();
    const loggedUser = jwtHelper.decodeToken(token);
    localStorage.setItem('user',loggedUser?.fullName );
  }
  logout(){
    localStorage.clear();
    console.log("clear storage");
  }
}
