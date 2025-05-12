import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";
import {BehaviorSubject, Subscription, timer} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class TokenService {

  private jwtHelper = new JwtHelperService();
  private tokenSubject =  new BehaviorSubject<string|null>(this.getToken())
  private expirationCheckSubscription: Subscription | null=null;

  constructor() {
    if(this.getToken()){
      this.tokenSubject.next(this.getToken());
      this.monitorTokenExpiration();// Start monitoring token expiration
    }
  }

  set token(token: string) {
    localStorage.setItem('access_token', token);
    this.tokenSubject.next(token);
    this.monitorTokenExpiration();
/*    console.log('Token SET:', token); // Add debug log*/
  }
  get token():string | null{
    return this.getToken();
  }

  getToken():string | null{
    return localStorage.getItem("access_token");
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (!token) {
   /*   console.warn('No token available for role decoding');*/
      return [];
    }

    try {
      const decoded = this.jwtHelper.decodeToken(token);
      //console.log('Decoded token:', decoded); // Debug decoded content
      return Array.isArray(decoded?.roles) ? decoded.roles : [];
    } catch (error) {
      //console.error('Token decoding error:', error);
      return [];
    }
  }
  getUserId(): number {
    const token = this.getToken();
    if (!token) return 0;
    try{
    const decoded = this.jwtHelper.decodeToken(token);
    return decoded?.userId || 0;
    }catch (error){
     /* console.error('Failed to decode token:', error);*/
      return 0;
    }
  }

  hasRoles(requiredRoles :string[]): boolean{
    const userRoles = this.getRoles();
    return requiredRoles.some(role => userRoles.includes(role));
  }


  isTokenExpired():boolean{
    const token= this.token;
    return !token || this.jwtHelper.isTokenExpired(token);
  }

  isTokenExpiringSoon(minutes: number = 5): boolean{
    const token = this.token;
    if (!token) return false;
    const expirationDate = this.jwtHelper.getTokenExpirationDate(token);
    if(expirationDate){
      const now =  new Date();
      const timeDifference = expirationDate.getTime()- now.getTime();
      return timeDifference <= (minutes * 60 * 1000);
    }
    return false;
  }

    isTokenNotValid() {
        return !this.isTockenValid();
    }

  private isTockenValid() {
    const token =this.token;
    if(!token){
      return false;
    }
    //checkexpiredDate
    const isTokenExpired = this.jwtHelper.isTokenExpired(token);
   /* console.log("TOKEN--> : "+isTokenExpired);*/
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
    this.tokenSubject.next(null);
    this.clearExpirationCheck();// Stop checking for expiration
   /* console.log("User has been logged out");*/
    //window.location.reload();
  }

  monitorTokenExpiration(){
    this.clearExpirationCheck();// Clear any existing subscription
    // Schedule a check based on the token expiration time
    const expirationDate = this.jwtHelper.getTokenExpirationDate(this.token as string);
    if(expirationDate){
      const now = new Date();
      const timeUtilExpiration = expirationDate.getTime()-
        now.getTime();

      // Set a timer to check for expiration a little before it expires (e.g., 1 minute before)
      const checkTime = Math.max(timeUtilExpiration - 1 * 60 * 1000, 0);
      //Create a timer to call logout when the token is about to expire
      this.expirationCheckSubscription = timer(checkTime).subscribe(() => {
       /* console.log('Token has expired, logging out...');*/
        alert("Logout soon !");
        this.logout();
      });
    }
    }

    get tokenChanges(){
    return this.tokenSubject.asObservable();
    }


  private clearExpirationCheck(){
    if(this.expirationCheckSubscription){
      this.expirationCheckSubscription.unsubscribe();
      this.expirationCheckSubscription = null;
    }
  }
}
