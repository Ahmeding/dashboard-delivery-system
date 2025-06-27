import {CanActivateFn, Router} from '@angular/router';
import {TokenService} from "../token/token.service";
import {inject} from "@angular/core";

export const authGuard: CanActivateFn = (route) => {

  const tokenService:TokenService = inject(TokenService);
  const router:Router = inject(Router);

  if(tokenService.isTokenNotValid()){
    router.navigate(['login']);
    return false;
  }

  const requiredRoles = route.data['roles'] || [];

  if(tokenService.hasRoles(requiredRoles)){
    return true;
  }

    router.navigate(['/unauthorized']);

  return false;
};
