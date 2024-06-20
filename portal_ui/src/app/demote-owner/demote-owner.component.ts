import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppComponent } from '../app.component';
import { SessionCheckerService } from '../session-checker.service';

@Component({
  selector: 'app-demote-owner',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './demote-owner.component.html',
  styleUrl: './demote-owner.component.scss'
})
export class DemoteOwnerComponent {
  ownerId?: string;

  constructor(private headerComp: AppComponent, private sessionChecker: SessionCheckerService) {
    this.headerComp.setUsername();
    this.headerComp.pageTitle = "Demote Owner";
  }

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
