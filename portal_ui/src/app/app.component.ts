import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { error } from 'console';
import { userInfo } from 'os';
import { SessionCheckerService } from './session-checker.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, RouterModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'portal_ui';
  username: string;
  pageTitle: string = 'Employee Management Portal';

  constructor(private router:Router, private sessionChecker: SessionCheckerService) {
    this.username = '';
  }

  async setUsername() {
    let data = await this.sessionChecker.checkSession();
    this.username = data.username;
  }

  async getUsername() {
    let data = await this.sessionChecker.checkSession();
    this.username = data.username;
    // return await this.sessionChecker.checkSession();
  }

  onLogout() {
    console.log("LogOut clicked");
    localStorage.removeItem("loginStat");
    localStorage.removeItem("userPriv");
    console.log("Logged Out");
    this.username = '';
    this.pageTitle = "Employee Management Portal";
    this.router.navigate(['/']);
  }

  checkBoxClicked() {
    alert("Theme switched");
    console.log("Theme Switched");
  }
}