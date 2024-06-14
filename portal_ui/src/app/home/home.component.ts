import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { response } from 'express';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  owner: boolean = false;
  admin: boolean = false;
  privUser: boolean = false;
  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.checkSession();
  }
  async checkSession() {
    if (typeof(localStorage) === 'undefined') {
      this.router.navigate(['/']);
    }
    try {
      const response = await fetch("http://localhost:8080/api/login/checkSession?sessionId="+localStorage.getItem("sessionId"));
      const data = await response.json();
      console.log("DATA : " + JSON.stringify(data));
      this.headerComp.username = data.username;
      this.authService.userPriv = data.privilege;
      switch (data.privilege) {
        case "owner":
          this.owner = true;
          break;
        case "admin":
          this.admin = true;
          break;
        case "priv_user":
          this.privUser = true;
          break;
        case "guest":
          break;
        default:
          // Unknown user type
          this.router.navigate(['/']);
          break;
      }
    }
    catch(error) {
      console.log("Error in page load check : " + error);
    }
  }
}
