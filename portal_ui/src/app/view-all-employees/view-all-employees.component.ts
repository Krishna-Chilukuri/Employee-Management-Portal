import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';
import { SessionCheckerService } from '../session-checker.service';

@Component({
  selector: 'app-view-all-employees',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-all-employees.component.html',
  styleUrl: './view-all-employees.component.scss'
})
export class ViewAllEmployeesComponent {
  data: any = [];

  
  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.headerComp.setUsername();
    this.headerComp.pageTitle = "View All Employees";
    fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/employees/getAllEmployees")
    .then ((response) => response.json())
    .then ((res) => {
      console.log(res);
      this.data = res;
    })
    .catch ((error) => {
      console.log("Error in get all Employees : " + error);
    })
  }
  
}
