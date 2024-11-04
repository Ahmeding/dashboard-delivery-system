import { Component } from '@angular/core';
import {UtilisateurDto} from "../../../../../../ds-api/src/models/utilisateur-dto";

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrls: ['./new-user.component.scss']
})
export class NewUserComponent {

  user:UtilisateurDto={}
  onSubmit() {

  }
}
