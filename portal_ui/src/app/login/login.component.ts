import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
// import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationServiceService } from '../authentication-service.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginCreds : LoginCreds;
  checkUrl : string = "https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/login/getId";
  jsonString : string;
  constructor(private router: Router, private authService: AuthenticationServiceService) {
    if (typeof(localStorage) !== 'undefined' && localStorage.getItem("loginStat") == "true") {
      this.router.navigate(['/home-component']);
    }
    this.loginCreds = new LoginCreds(); 
    this.jsonString = '';
  }


  async checkCredentials() {
    console.log("in checkCredentials");
    console.log("Username : " + this.loginCreds.username);
    console.log("Password : " + this.loginCreds.password);
    if(await this.authService.loginService(this.loginCreds.username, this.loginCreds.password)) {
      if (localStorage.getItem("loginStat") == "true") {
        console.log("Login Successful!!!!");
        console.log("User privilege: " + localStorage.getItem("userPriv"));
        this.router.navigate(['/home-component']);
      }
    }
    // alert("IN CHECK CREDS");
    // document.getElementById("login-form").reset();
    // window.location.reload();
    //+"&password="+JSON.stringify(this.loginCreds.password)
    // fetch("https://emp-management-portal-server.calmfield-5b49f4b7.eastus.azurecontainerapps.io/api/login/getId?id="+this.loginCreds.userId).then((data) => {
    //   data.json().then((obj) => {
    //     console.log(obj);
    //   })
    // })
  }
}


export class LoginCreds {
  username : string = "";
  password : string = ""; 
}

export class LoginSuccess {
  sessionId : string = "";
  username : string = "";
}
