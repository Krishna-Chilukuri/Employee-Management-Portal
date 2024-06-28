import { Injectable } from '@angular/core';
import { response } from 'express';
import { delay } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationServiceService {
  userPriv: string = '';
  loginStat: boolean = false;
  sessionId: string = '';

  constructor() { }

  async loginService(username: string, password: string): Promise<boolean> {
    console.log("IN AUTH SERVICE");
    try {
      const response = await fetch("https://emp-management-portal-23a41acb3a8b.herokuapp.com/api/login/getId?username="+username+"&password="+password);
      // const data = response.json();
      const data = await response.json();
      console.log("Login Done: " + JSON.stringify(data));
      console.log(data.privilege);
      if (data.privilege != null) {
        this.loginStat = true;
        this.userPriv = data.privilege;
        this.sessionId = data.sessionId;
      }
      else {
        this.loginStat = false;
        this.userPriv = '';
        this.sessionId = '';
      }
      localStorage.setItem("loginStat", this.loginStat ? "true" : "false");
      localStorage.setItem("userPriv", this.userPriv);
      localStorage.setItem("sessionId", this.sessionId);
      localStorage.setItem("username", data.username);
      return true;
    }
    catch (error) {
      console.log(error);
      return false;
    }
  }

  logout() {
    this.loginStat = false;
    this.userPriv = '';
    localStorage.removeItem("loginStat");
    localStorage.removeItem("userPriv");
  }

  isAuthenticated() {
    if (typeof(localStorage) !== 'undefined' && localStorage.getItem("loginStat") == "true") {
      return true
    }
    return false;
  }

  getPrivilege() {
    if (typeof(localStorage) !== 'undefined') {
      return localStorage.getItem("userPriv");
    }
    return '';
  }
}
