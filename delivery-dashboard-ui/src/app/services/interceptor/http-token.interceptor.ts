import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpHeaders, HttpErrorResponse
} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {TokenService} from "../token/token.service";
import {Router} from "@angular/router";

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(private router:Router,
    private tokenService:TokenService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token:string =this.tokenService.token as string;
    console.log("interceptor start ");
    let authReq = request;
    if(token){
       authReq=request.clone({
        headers : new HttpHeaders({
          'Content-type' : 'application/json',
          Accept : 'application/json',
          Authorization : 'Bearer '+ token
        })
      })

      console.log("interceptor : Bearer + token");

    }
    return next.handle(authReq).pipe(
      catchError((err)=>{
        console.log("Interceptor error", err);
/*        if(err.status === 401 || err.status === 403){
          console.log("Unauthorized or forbiden: Redirecting to login");
          this.tokenService.logout();
          this.router.navigate(['login']);
        }*/
        return throwError(()=>err);
      })
    );
  }
}
