import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { error } from 'console';

@Component({
  selector: 'app-remove-admin-owner',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './remove-admin-owner.component.html',
  styleUrl: './remove-admin-owner.component.scss'
})
export class RemoveAdminOwnerComponent {
  username: string = '';

  removeAdminOwner() {
    console.log(this.username);

    fetch("http://localhost:8080/api/login/removeAdminOwner?username="+this.username)
    .then ((response) => {
      console.log(response);
    })
    .catch ((error) => {
      console.log("Error in Remove Admin / Owner : " + error);
    })
  }
}
