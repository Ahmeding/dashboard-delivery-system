import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {SigninRequest} from "../../../ds-api/src/models/signin-request";
import {AuthenticationEnregistrementService} from "../../../ds-api/src/services/authentication-enregistrement.service";
import {TokenService} from "../../services/token/token.service";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isLoading!: boolean;
  authRequest : SigninRequest= {email:'',password:''};
  errorsMsg : Array<string>=[];
  errorSubject: string='';
  loginForm!: FormGroup;

  constructor(
    private router:Router,
    private fb: FormBuilder,
    private authServices: AuthenticationEnregistrementService,
    private tokenService:TokenService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
   this.initializeForm();
  }
  private initializeForm(){
      this.loginForm = this.fb.group({
        email: ['admin1@gmail.com', [Validators.required, Validators.email]],
        password: ['admin', [Validators.required, Validators.minLength(4)]]
      });
    }

  onSubmit() {
  this.login();
  }



  login() {
    if (this.loginForm.valid) {
      console.log("login()");
      this.isLoading = true;
      this.clearPreviousSession();
      this.errorsMsg = [];
      this.authRequest=this.loginForm.value;

      this.authServices.signin({
        body: this.authRequest
      }).subscribe({
        next: (res) =>this.handleLoginSuccess(res),
        error: (err) => this.handleLoginError(err)
      })
    }else{
      console.log('Form is invalid');
      this.markFormAsTouched();
      return;
    }
  }

  private clearPreviousSession(): void {
    this.tokenService.logout();
    this.errorsMsg = [];
  }
  private handleLoginSuccess(res: any): void {
    // Clear previous state FIRST
    this.tokenService.logout();

    this.tokenService.token = res.access_token;
    this.tokenService.saveLoggedUser(res.access_token);

    // Immediate role refresh
    this.authService.refreshRoles();

    setTimeout(()=>{

      console.log('Final role check:', {
        isAdmin: this.authService.isAdminUser(),
        isUser: this.authService.isRegularUser()
      });

      if (!this.authService.isAdminUser() && !this.authService.isRegularUser()) {
        console.error('Invalid role state after login');
        this.tokenService.logout();
        return;
      }


      this.navigateBasedOnRole();
      this.isLoading = false;
    },1350);// Increased delay for state propagation
    console.log('Login successful:', res);
  }

  private handleLoginError(err: any): void {
    this.isLoading = false;
    if(err.error?.errors){
      this.errorsMsg = err.error.errors;
      this.errorSubject = err.error.msg || 'Error';
    }
  }

  private markFormAsTouched(): void {
    Object.values(this.loginForm.controls).forEach(
      control=>{
        control.markAsTouched();
      }
    )
  }
  private navigateBasedOnRole() {
    if (this.authService.isAdminUser()) {
      this.router.navigate(['/']);
    } else if (this.authService.isRegularUser()) {
      this.router.navigate(['/dplanning/my-weekly-p']);
    } else {
      console.warn('No valid roles detected');
      this.router.navigate(['/unauthorized']);
    }
  }
}
