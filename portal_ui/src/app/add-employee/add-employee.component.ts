import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { response } from 'express';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';
import { SessionCheckerService } from '../session-checker.service';

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-employee.component.html',
  styleUrl: './add-employee.component.scss'
})
export class AddEmployeeComponent {
  employee: Employee;
  jsonString: string;
  // callStat: boolean = false;
  resultSection: string = '';

  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService) {
    // this.checkSession();
    this.headerComp.setUsername();
    this.employee = new Employee();
    this.jsonString = '';
  }

  async addEmployee() {
    console.log(this.employee.employeeName);
    console.log(this.employee.employeeRank);
    this.jsonString = JSON.stringify(this.employee);
    fetch("http://localhost:8080/api/employees/save?sessionId=" + localStorage.getItem("sessionId"), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: this.jsonString,
    })
      .then((response) => response.json())
      .then((data) => {
        var emp = Object.assign(new Employee(), data);
        console.log("RET VAL: " + emp);
        if (emp.employeeId == 0) {
          this.resultSection = "This Insertion is not Possible as it is Invalid";
          return;
        }
        // alert("EMP INSERTED");
        console.log("EMP INSERTED CONSOLE TEXT CHECK");
        this.resultSection = "Employee Inserted !!";
        console.log(emp);
        // alert("Employee Insertion Done");
        // window.location.reload();
        this.employee = new Employee();
      })
      .catch((error) => {
        console.log("ERROR IN ADD EMP : " + error);
      });
  }
}


export class Employee {
  employeeName?: string;
  employeeRank?: number;
  reportsTo: number = 0;
}