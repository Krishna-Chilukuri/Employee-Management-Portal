import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';
import { SessionCheckerService } from '../session-checker.service';

@Component({
  selector: 'app-view-employee',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './view-employee.component.html',
  styleUrl: './view-employee.component.scss'
})
export class ViewEmployeeComponent {
  employeeId?: number;
  resEmpId?: number;
  employeeName?: string;
  employeeRank?: string;
  reportsTo?: string;
  reportees?: string;
  jsonString: string;
  data: object;
  empReceived: boolean = false;
    // this.employeeId = 0;
  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    this.headerComp.setUsername();
    this.headerComp.pageTitle = "View Employee";
    this.jsonString = '';
    this.data = new Object();
  }
  

  async viewEmployee() {
    console.log("Viewing : " + this.employeeId);
    
    fetch("https://emp-management-portal-23a41acb3a8b.herokuapp.com/api/employees/view?empId="+this.employeeId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then ((response) => response.json())
    .then ((dat) => {
      console.log(dat);
      this.data = dat;
      this.employeeId = dat?.employee?.employeeId ?? 0;
      if (this.employeeId == 0) {
        console.log("Employee is not Present");
        this.empReceived = false;
        return;
      }
      else {
        this.resEmpId = this.employeeId;
        this.employeeName = dat?.employee?.employeeName ?? '';
        this.employeeRank = dat?.employee?.employeeRank ?? '';
        this.reportsTo = dat?.employee?.reportsTo ?? 'None';
        if(this.reportsTo == '0') this.reportsTo = 'None';
        this.reportees = dat?.reportees ?? '';
        this.empReceived = true;
      }
    })
    .catch ((error) => {
      console.log("Error in View EMP: " + error);
    });
  }
}
