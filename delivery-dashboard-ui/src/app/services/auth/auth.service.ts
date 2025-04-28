import {Injectable, OnDestroy} from '@angular/core';
import {TokenService} from "../token/token.service";
import {Subscription} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService implements OnDestroy{

  private isAdmin : boolean = false;
  private isUser : boolean = false;
  private tokenSub: Subscription; // add subscription

  constructor(private tokenService: TokenService) {
    this.tokenSub = this.tokenService.tokenChanges.subscribe(()=>{
    this.updateRoles(); // refresh roles on token change
    });
    this.updateRoles();
  }

  private updateRoles() {
    const token = this.tokenService.token;
    console.log('UpdateRoles - Current token:', token);

    this.isAdmin = this.tokenService.hasRoles(['ROLE_ADMIN']);
    this.isUser = this.tokenService.hasRoles(['ROLE_USER']);

    console.log('Role update results:', {
      admin: this.isAdmin,
      user: this.isUser
    });
  }

  isAdminUser(){
    return this.isAdmin;
  }
  isRegularUser(){
    return this.isUser;
  }
  public refreshRoles() {
    console.log('Refreshing roles...');
    const previousAdmin = this.isAdmin;
    const previousUser = this.isUser;

    this.updateRoles();

    console.log(`Role change - Admin: ${previousAdmin}->${this.isAdmin}, User: ${previousUser}->${this.isUser}`);
  }

  ngOnDestroy(): void {
    this.tokenSub.unsubscribe();
  }
}
