import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { response } from 'express';

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-employee.component.html',
  styleUrl: './add-employee.component.scss'
})
export class AddEmployeeComponent {
  employee: Employee;
  jsonString: string;
  constructor() {
    this.employee = new Employee();
    this.jsonString = '';
  }

  async addEmployee() {
    console.log(this.employee.employeeName);
    console.log(this.employee.employeeRank);
    this.jsonString = JSON.stringify(this.employee);
    fetch("http://localhost:8080/api/employees/save", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: this.jsonString,
    })
    .then ((response) => response.json())
    .then ((data) => {
      var emp = Object.assign(new Employee(), data);
      console.log("RET VAL: " + emp);
      console.log(emp);
    })
    .catch ((error) => {
      console.log("ERROR IN ADD EMP : " + error);
    });
  }
}


class Employee {
  employeeName: string = '';
  employeeRank: number = 0;
  reportsTo: number = 0;
}