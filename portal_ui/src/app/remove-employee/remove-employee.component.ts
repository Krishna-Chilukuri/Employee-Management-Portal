import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';
import { SessionCheckerService } from '../session-checker.service';

@Component({
  selector: 'app-remove-employee',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './remove-employee.component.html',
  styleUrl: './remove-employee.component.scss'
})
export class RemoveEmployeeComponent {
  // employee: Employee;
  jsonString: string;
  employeeId?: number;
  
  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.headerComp.setUsername();
    this.headerComp.pageTitle = "Remove Employee";
    this.jsonString = '';
    // this.employee = new Employee();
    this.jsonString = '';
  }
  

  async removeEmployee() {
    console.log(this.employeeId);
    fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/employees/delete?empId="+this.employeeId+"&sessionId="+localStorage.getItem("sessionId"), {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then ((response) => {
      if (!response.ok) {
        throw new Error('Employee is not available');
      }
      console.log("Employee Removed");
      // window.location.reload();
    })
    .catch ((error) => {
      console.log("ERROR IN REMOVE EMP : " + error);
    });
  }
}

// class Employee {
//   employeeId: number = 0;
//   employeeName: string = '';
//   employeeRank: number = 0;
//   reportsTo: number = 0;
// }