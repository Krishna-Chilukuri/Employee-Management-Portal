import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-hierarchies',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './hierarchies.component.html',
  styleUrl: './hierarchies.component.scss'
})
export class HierarchiesComponent {
  empId: number = 0;
  kVal: number = 0;
  choiceOfHie: string = '';
  getHierarchy() {
    // console.log("Get Hierarchy Req FOR: " + this.empId);
    // console.log("Choice: " + this.choiceOfHie + typeof(this.choiceOfHie));
    if (this.empId > 0) {
      console.log("Valid Request");
      switch (this.choiceOfHie) {
        case "upper":
          console.log("Upper Hierarchy Selected for " + this.empId);
          break;
        case "lower":
          console.log("Lower Hie")
          break;
        case "kStep":
          break;
      }
    }
  }
}
