import { Component } from '@angular/core';
import {UserRequest} from "../../../../../../ds-api/src/models/user-request";

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrls: ['./new-user.component.scss']
})
export class NewUserComponent {

  user!:UserRequest;
  showPassword: any;
  isSubmitting!: boolean;

  onSubmit() {

  }

  goBack() {

  }


  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

}
