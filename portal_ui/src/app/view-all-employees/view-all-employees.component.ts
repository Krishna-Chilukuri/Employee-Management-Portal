import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-view-all-employees',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-all-employees.component.html',
  styleUrl: './view-all-employees.component.scss'
})
export class ViewAllEmployeesComponent {
  data: any = [];

  constructor() {  fetch("http://localhost:8080/api/employees/getAllEmployees")
    .then ((response) => response.json())
    .then ((res) => {
      console.log(res);
      this.data = res;
    })
    .catch ((error) => {
      console.log("Error in get all Employees : " + error);
    })
  }
}
