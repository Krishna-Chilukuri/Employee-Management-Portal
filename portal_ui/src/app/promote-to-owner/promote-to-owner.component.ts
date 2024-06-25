import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';

@Component({
  selector: 'app-promote-to-owner',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './promote-to-owner.component.html',
  styleUrl: './promote-to-owner.component.scss'
})
export class PromoteToOwnerComponent {
  adminId?: string;

  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.checkSession();
    this.headerComp.pageTitle = "Promote To Owner";
  }

  promoteToOwner() {
    console.log(this.adminId);
    fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/login/promoteToOwner?adminId="+this.adminId)
    .then ((response) => {
      console.log(response);
      window.location.reload();
    })
    .catch ((error) => {
      console.log("Error in Promote to Owner : " + error);
    })
  }
  async checkSession() {
    if (typeof(localStorage) === 'undefined') {
      this.router.navigate(['/']);
    }
    try {
      const response = await fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/login/checkSession?sessionId="+localStorage.getItem("sessionId"));
      const data = await response.json();
      console.log("DATA : " + JSON.stringify(data));
      this.headerComp.username = data.username;
      this.authService.userPriv = data.privilege;
      switch (data.privilege) {
        case "owner":
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
