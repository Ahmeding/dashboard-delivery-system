import {Component, OnInit} from '@angular/core';
import {UtilisateurDto} from "../../../ds-api/src/models/utilisateur-dto";
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit{
  connectedUser:UtilisateurDto={};
  constructor(private userService:UserService) {

  }

  ngOnInit(): void {
    this.connectedUser = this.userService.getConnectedUser();
  }

}
