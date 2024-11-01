import {Component, OnInit} from '@angular/core';
import {
  AuthenticationEnregistrementService
} from "../../../../../ds-api/src/services/authentication-enregistrement.service";
import {Router} from "@angular/router";
import {TokenService} from "../../../../services/token/token.service";
import {UtilisateurDto} from "../../../../../ds-api/src/models/utilisateur-dto";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit{

  socketClient : any=null;
  private notificationSubscribtion: any;

  constructor(
    private router:Router,
    private tokenService:TokenService,
    private authService:AuthenticationEnregistrementService) {
  }
   logout() {
    this.router.navigate(['login'])
    this.tokenService.logout();
  }

  get user():string{
    return localStorage.getItem('user') as string;

  }

  ngOnInit(): void {
    let ws = new SockJS('http://localhost:8080/api/v1/ws');
    this.socketClient=Stomp.over(ws);
    this.socketClient.connect({'Authorization':'Bearer '+this.tokenService.token},()=>{
      console.log("connect to ws");

      this.notificationSubscribtion = this.socketClient.subscribe(
         `/user/connecteduser/notification`,
        (message:any)=>{
          console.log(message);
        }
      )
    })
  }
}
