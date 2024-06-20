import { Component } from '@angular/core';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-about-component',
  standalone: true,
  imports: [],
  templateUrl: './about-component.component.html',
  styleUrl: './about-component.component.scss'
})
export class AboutComponentComponent {

  constructor(private headerComp: AppComponent) {
    this.headerComp.setUsername();
    this.headerComp.pageTitle = "About Page";
  }
}
