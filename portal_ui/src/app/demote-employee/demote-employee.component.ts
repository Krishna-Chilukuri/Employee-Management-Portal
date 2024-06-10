import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-demote-employee',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './demote-employee.component.html',
  styleUrl: './demote-employee.component.scss'
})
export class DemoteEmployeeComponent {
  empId: number = 0;
  numDems: number = 0;

  demoteEmployee() {
    console.log("Demote Request for " + this.empId + " " + this.numDems);
    if (this.empId > 0 && this.numDems > 0) {
      console.log("Valid Demotion request");
      fetch("http://localhost:8080/api/employees/demote?empId="+this.empId+"&numDems="+this.numDems, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then ((response) => {
        if (!response.ok) {
          throw new Error ("Demotion not possible");
        }
        console.log("Demotion Completed");
      })
      .catch ((error) => {
        console.log("Error in Demote: " + error);
      });
    }
  }
}
