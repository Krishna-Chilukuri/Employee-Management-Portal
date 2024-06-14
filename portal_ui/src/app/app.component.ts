import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { error } from 'console';
import { userInfo } from 'os';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'portal_ui';
  username: string;

  constructor(private router:Router) {
    this.username = '';
  }

  onLogout() {
    console.log("LogOut clicked");
    localStorage.removeItem("loginStat");
    localStorage.removeItem("userPriv");
    console.log("Logged Out");
    this.username = '';
    this.router.navigate(['/']);
  }
}