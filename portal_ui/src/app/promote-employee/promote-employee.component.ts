import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-promote-employee',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './promote-employee.component.html',
  styleUrl: './promote-employee.component.scss'
})
export class PromoteEmployeeComponent {
  empId: number = 0;
  numProms: number = 0;
  promoteEmployee() {
    console.log("Promote Request for " + this.empId + " " + this.numProms);
    if (this.empId > 0 && this.numProms > 0) {
      console.log("Valid Promotion request");
      fetch("http://localhost:8080/api/employees/promote?empId="+this.empId+"&numProms="+this.numProms, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then ((response) => {
        if (!response.ok) {
          throw new Error('Promotion not possible');
        }
        console.log("Promotion Completed");
      })
      .catch ((error) => {
        console.log("Error in Promote: " + error);
      });
    }
  }
}
