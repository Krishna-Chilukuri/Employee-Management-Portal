import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { SessionCheckerService } from '../session-checker.service';

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

  constructor(private headerComp: AppComponent) {
    this.headerComp.setUsername();
    this.headerComp.pageTitle = "Promote Employee";
  }


  promoteEmployee() {
    console.log("Promote Request for " + this.empId + " " + this.numProms);
    console.log("Valid Promotion request");
    fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/employees/promote?empId=" + this.empId + "&numProms=" + this.numProms + "&sessionId=" + localStorage.getItem("sessionId"), {
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
