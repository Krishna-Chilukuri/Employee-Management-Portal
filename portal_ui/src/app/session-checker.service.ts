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
    if (typeof (localStorage) === 'undefined') {
      this.router.navigate(['/']);
      // console.log("No local storage");
      // return;
    }
    let username = '';
    let data: any = [];
    try {
      if (typeof(localStorage) !== 'undefined') {
        console.log("Before fetch in service : Session CHecker");
        const response = await fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/login/checkSession?sessionId=" + localStorage.getItem("sessionId"));
        console.log("Before await");
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
      // else {
      //   console.log("No local storage : ELSE");
      // }
    }
    catch (error) {
      console.log("Error in page load check Session Checker : " + error);
    }
    if (username == '') this.router.navigate(['/']);
    return data;
  }
}
