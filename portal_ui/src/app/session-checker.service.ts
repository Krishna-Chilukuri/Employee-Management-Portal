import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from './app.component';
import { AuthenticationServiceService } from './authentication-service.service';

@Injectable({
  providedIn: 'root'
})
export class SessionCheckerService {

  constructor(private router: Router, private authService: AuthenticationServiceService) { }

  async checkSession() {
    if (typeof(localStorage) === 'undefined') {
      this.router.navigate(['/']);
    }
    let username = '';
    let data: any = [];
    try {
      const response = await fetch("http://localhost:8080/api/login/checkSession?sessionId="+localStorage.getItem("sessionId"));
    data = await response.json();
    console.log("DATA : " + JSON.stringify(data));
    username = data.username;
    this.authService.userPriv = data.privilege;
    switch (data.privilege) {
      case "owner":
        // localStorage.setItem("userPriv", "owner");
        // this.owner = true;
        break;
        case "admin":
        // localStorage.setItem("userPriv", "admin");
        // this.admin = true;
        break;
        case "priv_user":
        // localStorage.setItem("userPriv", "priv_user");
        // this.privUser = true;
        break;
        case "guest":
        // localStorage.setItem("userPriv", "guest");
        break;
      default:
        // Unknown user type
        localStorage.removeItem("loginStat");
        // localStorage.setItem("loginStat") = "false";
        this.router.navigate(['/']);
        break;
    }
  }
  catch(error) {
    console.log("Error in page load check Session Checker : " + error);
  }
  if (username == '') this.router.navigate(['/']);
  return data;
  }
}
