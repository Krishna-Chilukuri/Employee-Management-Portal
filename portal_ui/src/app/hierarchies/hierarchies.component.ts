import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { error } from 'console';
import { response } from 'express';

@Component({
  selector: 'app-hierarchies',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './hierarchies.component.html',
  styleUrl: './hierarchies.component.scss'
})
export class HierarchiesComponent {
  empId?: number;
  kVal?: number;
  data: any = [];
  gotResult: boolean = false;
  choiceOfHie: string = '';
  getHierarchy() {
    // console.log("Get Hierarchy Req FOR: " + this.empId);
    // console.log("Choice: " + this.choiceOfHie + typeof(this.choiceOfHie));
    console.log("Valid Request");
    switch (this.choiceOfHie) {
      case "upper":
        console.log("Upper Hierarchy Selected for " + this.empId);
        fetch("http://localhost:8080/api/employees/upperHierarchy?empId=" + this.empId, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        })
          .then((response) => response.json())
          .then((dat) => {
            console.log("Here: " +dat + typeof(dat) + dat.length);
            this.data = dat;
            if (dat.length == 0) this.gotResult = false;
            else this.gotResult = true;
          })
          .catch((error) => {
            this.gotResult = false;
            console.log("Error in Upper Hierarchy : " + error);
          })
        break;
      case "lower":
        console.log("Lower Hierarchy Selected for " + this.empId);
        fetch("http://localhost:8080/api/employees/lowerHierarchy?empId=" + this.empId, {
          method: 'GET',
          // headers: {
          //   'Content-Type': 'application/json',
          // },
        })
          .then((response) => response.json())
          .then((data) => {
            console.log(data);
          })
          .catch((error) => {
            console.log("Error in Lower Hierarchy :" + error);
          })
        break;
      case "kStep":
        console.log("kStep Hierarchy selected for " + this.empId + " for " + this.kVal + " steps");
        fetch("http://localhost:8080/api/employees/kStepHierarchy?empId=" + this.empId + "&kVal=" + this.kVal, {
          method: 'GET',
        })
          .then((response) => response.json())
          .then((data) => {
            console.log(data);
          })
          .catch((error) => {
            console.log("Error in K-Step Hierarchy : " + error);
          });
        break;
    }
  }
}
