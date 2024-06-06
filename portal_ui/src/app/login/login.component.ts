import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
// import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginCreds : LoginCreds;
  checkUrl : string = "http://localhost:8080/api/login/getId";
  jsonString : string;
  constructor(private router: Router) {
    this.loginCreds = new LoginCreds(); 
    this.jsonString = '';
  }


  async checkCredentials() {
    console.log("in checkCredentials");
    console.log("Username : " + this.loginCreds.username);
    console.log("Password : " + this.loginCreds.password);
    // alert("IN CHECK CREDS");
    // document.getElementById("login-form").reset();
    // window.location.reload();
    //+"&password="+JSON.stringify(this.loginCreds.password)
    this.jsonString = JSON.stringify(this.loginCreds);
    fetch ("http://localhost:8080/api/login/getId?id="+(this.loginCreds.username)+"&password="+(this.loginCreds.password), {
      method: 'GET',
      headers: {  
        'Content-Type': 'application/json',
      },
    })
    .then ((response) => response.json())
    .then ((data) => {
      console.log(data);
      var logDetails = Object.assign(new LoginSuccess(), data);
      console.log(logDetails);
      console.log("Success : " + data.length);
      for (let index = 0; index < data.length; index++) {
        const element = data[index];
        console.log(data);
      }
      console.log("After data : " + logDetails.sessionId + ' ' + typeof(logDetails.sessionId));
      if (logDetails.sessionId == null) {
        window.location.reload();
      }
      else {
        console.log("LOGIN SUCCESS KEPS!!!!");
      }
      document.cookie = "sessionId="+logDetails.sessionId+";expires="+logDetails.username+";";
      console.log("COOKIE SET!!!!");
      this.router.navigate(['/home-component']);
    })
    .catch ((error) => {
      console.log("ERROR : " + error);
    });
    // fetch("http://localhost:8080/api/login/getId?id="+this.loginCreds.userId).then((data) => {
    //   data.json().then((obj) => {
    //     console.log(obj);
    //   })
    // })
  }

  listAll() {
    fetch("http://localhost:8080/api/login/getAll", {
      method: 'GET',
    })
    .then((response) => response.json())
    .then ((data) => {
      for (let index = 0; index < data.length; index++) {
        const element = data[index];
        console.log(element);
      }
      // console.log("Success : " + data);
    })
    .catch ((error) => {
      console.log("Error : " + error);
    })
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
