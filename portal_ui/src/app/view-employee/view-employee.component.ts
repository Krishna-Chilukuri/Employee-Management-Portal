import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-view-employee',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './view-employee.component.html',
  styleUrl: './view-employee.component.scss'
})
export class ViewEmployeeComponent {
  employeeId: number;
  jsonString: string;
  constructor() {
    this.employeeId = 0;
    this.jsonString = '';
  }

  async viewEmployee() {
    console.log("Viewing : " + this.employeeId);
    
    fetch("http://localhost:8080/api/employees/view?empId="+this.employeeId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then ((response) => response.json())
    .then ((data) => {
      console.log(data);
    })
    .catch ((error) => {
      console.log("Error in View EMP: " + error);
    });
  }
}
