import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-promote-to-owner',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './promote-to-owner.component.html',
  styleUrl: './promote-to-owner.component.scss'
})
export class PromoteToOwnerComponent {
  adminId: string = '';

  promoteToOwner() {
    console.log(this.adminId);
    fetch("http://localhost:8080/api/login/promoteToOwner?adminId="+this.adminId)
    .then ((response) => {
      console.log(response);
    })
    .catch ((error) => {
      console.log("Error in Promote to Owner : " + error);
    })
  }
}
