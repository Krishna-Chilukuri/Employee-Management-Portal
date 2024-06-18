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
  empId?: number;
  numProms?: number;
  promoteEmployee() {
    console.log("Promote Request for " + this.empId + " " + this.numProms);
    console.log("Valid Promotion request");
    fetch("http://localhost:8080/api/employees/promote?empId=" + this.empId + "&numProms=" + this.numProms + "&sessionId=" + localStorage.getItem("sessionId"), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Promotion not possible');
        }
        console.log("Promotion Completed");
        alert("Promotion Completed !!");
        window.location.reload();
      })
      .catch((error) => {
        alert("Promotion is not Possible");
        console.log("Error in Promote: " + error);
        window.location.reload();
      });
  }
}
