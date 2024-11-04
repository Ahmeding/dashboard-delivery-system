import {Component, OnInit} from '@angular/core';
import {UtilisateurDto} from "../../../../../ds-api/src/models/utilisateur-dto";
import {UtilisateurService} from "../../../../../ds-api/src/services/utilisateur.service";

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit{
  user: UtilisateurDto={};

  constructor(private userService:UtilisateurService) {
  }
getUserById(){
    this.userService.findById1({id:3}).subscribe(
      {
        next:(userRep)=>{
          this.user=userRep;
        },error:(err)=>{
          console.log(err);
        }
      }
    )
}
  ngOnInit(): void {
    this.getUserById();
  }


  editProfile() {

  }
}
