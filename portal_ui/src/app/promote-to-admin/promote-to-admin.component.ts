import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { error } from 'console';

@Component({
  selector: 'app-promote-to-admin',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './promote-to-admin.component.html',
  styleUrl: './promote-to-admin.component.scss'
})
export class PromoteToAdminComponent {
  empId: number = 0;
  promoteToAdmin() {
    console.log(this.empId);
    fetch("http://localhost:8080/api/employees/promoteToAdmin?empId="+this.empId)
    .then ((response) => {
      console.log(response);
    })
    .catch ((error) => {
      console.log(error);
    });
  }
}
