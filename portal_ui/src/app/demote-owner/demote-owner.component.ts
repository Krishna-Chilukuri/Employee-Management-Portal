import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-demote-owner',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './demote-owner.component.html',
  styleUrl: './demote-owner.component.scss'
})
export class DemoteOwnerComponent {
  ownerId?: string;

  demoteOwner() {
    console.log(this.ownerId);

    fetch("http://localhost:8080/api/login/demoteOwner?ownerId="+this.ownerId)
    .then ((response) => {
      console.log(response);
      window.location.reload();
    })
    .catch ((error) => {
      console.log("Error in Demote Owner : " + error);
      window.location.reload();
    })
  }

}
