import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {JwtAuthenticationResponse} from "../../../ds-api/src/models/jwt-authentication-response";

export const loggerInterceptor: HttpInterceptorFn = (req, next) => {
    if(req.url.includes("signin")|| req.url.includes("signup")){
      return next(req);
    }

  let authentificationResponse:JwtAuthenticationResponse={};
  if(localStorage.getItem('accessToken')){
    authentificationResponse = JSON.parse(
      localStorage.getItem('accessToken') as string
    );
  }
  const authReq = req.clone({
   headers: req.headers.set('Authorization','Bearer '+authentificationResponse.token)
  });
  return next(authReq);
};
