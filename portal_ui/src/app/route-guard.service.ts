import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Route, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationServiceService } from './authentication-service.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService{

  constructor(private authService: AuthenticationServiceService, private router: Router) { }
}

export const canNavigate: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  if (inject(AuthenticationServiceService).isAuthenticated()) {
    return true;
  } else {
    inject(Router).navigate(['/']);
    return false;
  }
}

export const canOwnerNavigate: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  if (inject(AuthenticationServiceService).isAuthenticated()) {
    if (inject(AuthenticationServiceService).userPriv == 'owner') {
      return true;
    }
    else {
      inject(Router).navigate(['home-component']);
      return true;
    }
  }
  else {
    inject(Router).navigate(['/']);
    return false;
  }
}

export const canAdminNavigate: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  if (inject(AuthenticationServiceService).isAuthenticated()) {
    let userPrivilege = inject(AuthenticationServiceService).getPrivilege();
    if (userPrivilege == 'owner' || userPrivilege == 'admin') {
      return true;
    }
    else {
      console.log("KP" + inject(AuthenticationServiceService).userPriv);
      alert("KP");
      inject(Router).navigate(['home-component']);
      return false;
    }
  }
  else {
    inject(Router).navigate(['/']);
    return false;
  }
}

export const canPrivNavigate: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  if (inject(AuthenticationServiceService).isAuthenticated()) {
    if (inject(AuthenticationServiceService).userPriv == 'priv_user' || inject(AuthenticationServiceService).userPriv == 'admin' || inject(AuthenticationServiceService).userPriv == 'owner') {
      return true;
    }
    else {
      console.log("KP");
      inject(Router).navigate(['home-component']);
      return true;
    }
  }
  else {
    inject(Router).navigate(['/']);
    return false;
  }
}