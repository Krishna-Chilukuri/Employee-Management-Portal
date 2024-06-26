import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { SessionCheckerService } from '../session-checker.service';

@Component({
  selector: 'app-demote-employee',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './demote-employee.component.html',
  styleUrl: './demote-employee.component.scss'
})
export class DemoteEmployeeComponent {
  empId?: number;
  numDems?: number;

  constructor(private headerComp: AppComponent) {
    this.headerComp.setUsername();
    this.headerComp.pageTitle = "Demote Employee";
  }

  demoteEmployee() {
    console.log("Demote Request for " + this.empId + " " + this.numDems);
    console.log("Valid Demotion request");
    fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/employees/demote?empId=" + this.empId + "&numDems=" + this.numDems + "&sessionId=" + localStorage.getItem("sessionId"), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Demotion not possible");
        }
        console.log("Demotion Completed");
      })
      .catch((error) => {
        console.log("Error in Demote: " + error);
      });
  }
}
