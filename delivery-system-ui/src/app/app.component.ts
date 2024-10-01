import { Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Delivery Missions System';

  constructor(private router: Router) {
  }
  navigate(url?: string):void{
    this.router.navigate([url]);
  }
}
