import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { error } from 'console';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';

@Component({
  selector: 'app-promote-to-admin',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './promote-to-admin.component.html',
  styleUrl: './promote-to-admin.component.scss'
})
export class PromoteToAdminComponent {
  empId?: number;
  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.checkSession();
    this.headerComp.pageTitle = "Promote To Admin";
  }
  promoteToAdmin() {
    console.log(this.empId);
    fetch("https://emp-management-portal-23a41acb3a8b.herokuapp.com/api/employees/promoteToAdmin?empId="+this.empId)
    .then ((response) => {
      console.log(response);
    })
    .catch ((error) => {
      console.log(error);
    });
  }

  async checkSession() {
    if (typeof(localStorage) === 'undefined') {
      this.router.navigate(['/']);
    }
    try {
      const response = await fetch("https://emp-management-portal-23a41acb3a8b.herokuapp.com/api/login/checkSession?sessionId="+localStorage.getItem("sessionId"));
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
