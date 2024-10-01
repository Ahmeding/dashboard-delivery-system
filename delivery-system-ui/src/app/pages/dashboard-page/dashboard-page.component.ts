import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../services/user/user.service";
import {UtilisateurDto} from "../../../ds-api/src/models/utilisateur-dto";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss',
})
export class DashboardPageComponent implements OnInit{

  connectedUser:UtilisateurDto={};
constructor(private router: Router,
            private userService: UserService) {}

  ngOnInit(): void {
  this.connectedUser= this.userService.getConnectedUser();
  }

  navigate(url?: string):void{
    this.router.navigate([url]);
  }
}
