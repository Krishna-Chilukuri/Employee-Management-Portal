import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { response } from 'express';
import { AppComponent } from '../app.component';
import { AuthenticationServiceService } from '../authentication-service.service';
import { SessionCheckerService } from '../session-checker.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  owner: boolean = false;
  admin: boolean = false;
  privUser: boolean = false;
  constructor(private router: Router, private headerComp: AppComponent, private authService: AuthenticationServiceService, private sessionChecker: SessionCheckerService) {
    this.sessionCheck();
    // this.headerComp.getUsername();
    // this.checkSession();
  }
  async sessionCheck() {
    let data = await this.sessionChecker.checkSession();
    this.headerComp.username = data.username;
    switch(data.privilege) {
      case "owner":
        this.owner = true;
        break;
      case "admin":
        this.admin = true;
        break;
      case "priv_user":
        this.privUser = true;
        break;
      // default:
      //   this.router.navigate(['/']);
    }
  }
}
