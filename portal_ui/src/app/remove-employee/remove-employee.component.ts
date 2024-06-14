import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

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
  employeeId: number;
  constructor() {
    // this.employee = new Employee();
    this.jsonString = '';
    this.employeeId = 0;
  }

  async removeEmployee() {
    console.log(this.employeeId);
    fetch("http://localhost:8080/api/employees/delete?empId="+this.employeeId+"&sessionId="+localStorage.getItem("sessionId"), {
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