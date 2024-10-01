import {Component, OnInit} from '@angular/core';
import {UtilisateurDto} from "../../../../ds-api/src/models/utilisateur-dto";
import {UtilisateurService} from "../../../../ds-api/src/services/utilisateur.service";
import {UserService} from "../../../services/user/user.service";

@Component({
  selector: 'app-profil-page',
  templateUrl: './profil-page.component.html',
  styleUrl: './profil-page.component.scss'
})
export class ProfilPageComponent implements OnInit{

  connectedUser:UtilisateurDto={};
  constructor(private userService:UserService) {
  }
  ngOnInit(): void {

    this.connectedUser= this.userService.getConnectedUser();
  }

}
