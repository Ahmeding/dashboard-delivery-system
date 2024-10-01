import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../services/user/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-change-password-page',
  templateUrl: './change-password-page.component.html',
  styleUrl: './change-password-page.component.scss',
  host: {ngSkipHydration: 'true'},
})
export class ChangePasswordPageComponent implements OnInit{

  constructor(private users: UserService,
              private router:Router) {}

  ngOnInit(): void {
    this.users.findAll().subscribe(
      res=>{
        console.log(JSON.stringify(res)+" l'inscription a été effectué avec succssé!");
      },
      error=>{

        console.error(error.errors)
    }
    );
  }

}
