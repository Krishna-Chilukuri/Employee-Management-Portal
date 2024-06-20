import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.scss'
})
export class ProfilePageComponent {
  userName: string = '';
  userPrivilege: string = '';
  userRank?: string;
  userReportsTo?: string;
  userReportees?: string;
  isPrivUserStat: boolean = false;
  empReceived: boolean = false;
  newPassword?: string;

  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.checkSession();
    this.headerComp.pageTitle = this.userName + " Profile";
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
      this.userName = data.username;
      switch (data.privilege) {
        case "owner":
          this.userPrivilege = "Owner";
          break;
        case "admin":
          this.userPrivilege = "Admin";
          break;
        case "priv_user":
          this.userPrivilege = "Privileged User";
          const response2 = await fetch("http://localhost:8080/api/employees/view?empId="+this.userName);
          const data2 = await response2.json();
          console.log("EMP DATA : " + JSON.stringify(data2));
          if (data2?.employee?.employeeId != this.userName) {
            console.log("NO EMP REC");
            break;
          }
          else {
            console.log("IN else emp received");
            this.userRank = data2?.employee?.employeeRank ?? '';
            this.userReportsTo = data2?.employee?.reportsTo ?? 'None';
            this.userReportees = data2?.reportees ?? '';
            this.empReceived = true;
          }
          break;
        case "guest":
          this.userPrivilege = "Guest User";
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
      console.log("Error in page load check : " + error);
    }
  }

  async changePassword() {
    console.log("new password: " + this.newPassword);
    const response = await fetch("http://localhost:8080/api/login/changePassword?username="+this.userName+"&password="+this.newPassword);
    const retVal = await response.text();
    console.log(retVal);
    alert(retVal);
    window.location.reload();
  }
}
