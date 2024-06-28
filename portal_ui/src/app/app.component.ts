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
  username: string = '';
  pageTitle: string = 'Employee Management Portal';

  constructor(private router: Router, private sessionChecker: SessionCheckerService) {
    // let ele = document.getElementById("dispImg") as HTMLImageElement | null;
    if (typeof (localStorage) !== 'undefined') {
      if (localStorage.getItem("theme")) {
        switch (localStorage.getItem("theme")) {
          case "light":
            console.log("Light theme in local storage");
            const checkbox = document.getElementById("themeSwitcher") as HTMLInputElement;
            checkbox.checked = true;
            document.documentElement.style.setProperty('--currTheme', 'light');
            document.documentElement.style.setProperty('--text-color', 'var(--light-text-color)');
            document.documentElement.style.setProperty('--bg-color', 'var(--light-bg-color)');
            document.documentElement.style.setProperty('--nav-bar-color', 'var(--light-nav-bar-color)');
            document.documentElement.style.setProperty('--table-highlight-row', 'var(--light-table-highlight-row)');
            document.documentElement.style.setProperty('--button-highlight-text', 'var(--light-table-highlight-text)');
            document.documentElement.style.setProperty('--button-highlight-color', 'var(--light-button-highlight-color)');
            document.documentElement.style.setProperty('--accent-color', 'var(--light-accent-color)');
            document.documentElement.style.setProperty('--button-border', 'var(--light-button-border)');
            break;
            case "dark":
              document.documentElement.style.setProperty('--currTheme', 'dark');
              document.documentElement.style.setProperty('--text-color', 'var(--dark-text-color)');
            // document.documentElement.style.setProperty('--bg-color', '#121212');
            document.documentElement.style.setProperty('--bg-color', 'var(--dark-bg-color)');
            document.documentElement.style.setProperty('--nav-bar-color', 'var(--dark-nav-bar-color)');
            document.documentElement.style.setProperty('--table-highlight-row', 'var(--dark-table-highlight-row)');
            document.documentElement.style.setProperty('--button-highlight-text', 'var(--dark-button-highlight-text)');
            document.documentElement.style.setProperty('--button-highlight-color', 'var(--dark-button-highlight-color)');
            document.documentElement.style.setProperty('--accent-color', 'var(--dark-accent-color)');
            document.documentElement.style.setProperty('--button-border', 'var(--dark-button-border)');
            console.log("Dark theme in local storage");
            break;
        }
      }
      else {
        console.log("key not present");
        let currTheme = getComputedStyle(document.documentElement).getPropertyValue('--currTheme');
        localStorage.setItem("theme", currTheme);
        console.log(localStorage.getItem("theme"));
  
      }
    }
    this.setUsername();
  }

  menuToggle() {
    console.log("Menu Button Clicked!!");
    let ele = document.getElementById("navbarMenu");
    let imgele = document.getElementById("dispImg") as HTMLImageElement | null;
    console.log(ele);
    if (ele?.classList.contains("navbgActive")) {
      ele.classList.remove("navbgActive");
      if (imgele) {
        imgele.src = "menu_dark.png";
      }
      else {
        console.log("null in imgele");
      }
    }
    else {
      if (imgele) {
        imgele.src = "close_dark.png";
      }
      console.log("adding navbgactive");
      ele?.classList.add("navbgActive");
    }
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
    localStorage.removeItem("sessionId");
    console.log("Logged Out");
    this.username = '';
    this.pageTitle = "Employee Management Portal";
    this.router.navigate(['/']);
  }

  checkBoxClicked() {
    // alert("Theme switched");
    console.log("Theme Switched");
    let root = document.documentElement;

    if (getComputedStyle(root).getPropertyValue("--currTheme") == "dark") {
      document.documentElement.style.setProperty('--currTheme', 'light');
      localStorage.setItem("theme", "light");
      document.documentElement.style.setProperty('--text-color', 'var(--light-text-color)');
      document.documentElement.style.setProperty('--bg-color', 'var(--light-bg-color)');
      document.documentElement.style.setProperty('--nav-bar-color', 'var(--light-nav-bar-color)');
      document.documentElement.style.setProperty('--table-highlight-row', 'var(--light-table-highlight-row)');
      document.documentElement.style.setProperty('--button-highlight-text', 'var(--light-table-highlight-text)');
      document.documentElement.style.setProperty('--button-highlight-color', 'var(--light-button-highlight-color)');
      document.documentElement.style.setProperty('--accent-color', 'var(--light-accent-color)');
      document.documentElement.style.setProperty('--button-border', 'var(--light-button-border)');
      console.log("in if");
    }
    else {
      document.documentElement.style.setProperty('--currTheme', 'dark');
      localStorage.setItem("theme", "dark");
      document.documentElement.style.setProperty('--text-color', 'var(--dark-text-color)');
      // document.documentElement.style.setProperty('--bg-color', '#121212');
      document.documentElement.style.setProperty('--bg-color', 'var(--dark-bg-color)');
      document.documentElement.style.setProperty('--nav-bar-color', 'var(--dark-nav-bar-color)');
      document.documentElement.style.setProperty('--table-highlight-row', 'var(--dark-table-highlight-row)');
      document.documentElement.style.setProperty('--button-highlight-text', 'var(--dark-button-highlight-text)');
      document.documentElement.style.setProperty('--button-highlight-color', 'var(--dark-button-highlight-color)');
      document.documentElement.style.setProperty('--accent-color', 'var(--dark-accent-color)');
      document.documentElement.style.setProperty('--button-border', 'var(--dark-button-border)');
      console.log("in else");
    }
    console.log(getComputedStyle(root).getPropertyValue("--currTheme"), typeof (getComputedStyle(root).getPropertyValue("--currTheme")));
    // let rootEle = document.querySelector(':root');
    // if (rootEle) {
    // }
  }
}