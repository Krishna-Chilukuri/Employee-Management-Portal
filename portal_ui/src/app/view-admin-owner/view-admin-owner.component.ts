import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { error } from 'console';

@Component({
  selector: 'app-view-admin-owner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-admin-owner.component.html',
  styleUrl: './view-admin-owner.component.scss'
})
export class ViewAdminOwnerComponent {
  data: any = [];

  constructor() {
    fetch("http://localhost:8080/api/login/viewAdminOwner")
    .then ((response) => response.json())
    .then ((obj) => {
      console.log(obj);
      this.data = obj;
      console.log(typeof(obj));
    })
    .catch ((error) => {
      console.log("Error in View Admin Owner : " + error);
    });
  }
}
