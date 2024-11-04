import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {SigninRequest} from "../../../ds-api/src/models/signin-request";
import {AuthenticationEnregistrementService} from "../../../ds-api/src/services/authentication-enregistrement.service";
import {TokenService} from "../../services/token/token.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isLoading!: boolean;
  authRequest : SigninRequest= {email:'',password:''};
  errorsMsg : Array<string>=[];
  errorSubject:string='';
  loginForm!: FormGroup;

  constructor(
    private router:Router,
    private fb: FormBuilder,
    private authServices: AuthenticationEnregistrementService,
    private tokenService:TokenService
  ) {}

  ngOnInit(): void {
   this.getLoginForm();
  }
    getLoginForm(){
      return this.loginForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(4)]]
      });
    }
  onSubmit() {
  this.login();
  }



  login() {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.tokenService.logout();
      this.errorsMsg = [];
      this.authRequest=this.loginForm.value;
      this.authServices.signin({
        body: this.authRequest
      }).subscribe({
        next: (res) => {
          this.tokenService.token = res.token as string;
          console.log("mmmmmmmmmmmmmmmmmm");
          this.tokenService.saveLoggedUser(res.token as string);
          this.isLoading = false;
          this.router.navigate(['/']);console.log('Login Successful!', this.loginForm.value);
        },
        error: (err) => {
          console.log(err);
          if (err.error.errors) {
            this.errorsMsg = err.error.errors;//.validationMessages
            console.log(err.error.msg);
            this.errorSubject=err.error.msg;
            this.isLoading = false;
          }
        }
      })
    }else{
      console.log('Form is invalid');
    }
  }
}
