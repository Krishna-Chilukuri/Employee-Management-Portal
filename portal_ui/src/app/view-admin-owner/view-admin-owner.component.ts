import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { error } from 'console';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';

@Component({
  selector: 'app-view-admin-owner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-admin-owner.component.html',
  styleUrl: './view-admin-owner.component.scss'
})
export class ViewAdminOwnerComponent {
  data: any = [];

  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.checkSession();
    this.headerComp.pageTitle = "View Admins & Owners";
    fetch("http://localhost:8080/api/login/viewAdminOwner")
    .then ((response) => response.json())
    .then ((obj) => {
      console.log(obj);
      this.data = obj;
      console.log(typeof(obj));
    })
    .catch ((error) => {
      console.log("Error in View Admin Owner : " + error);
    });
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
          break;
        case "admin":
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
