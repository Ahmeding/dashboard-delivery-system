import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TokenService} from "../../../services/token/token.service";
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs'
import {AlertService} from "../../../services/alert/alert.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit{

  @Input() isSidebarCollapsed = false;
  @Output() toggleSidebar = new EventEmitter<void>();

  private socketClient: any =null;
  private notificationsSubscruptions:any;

  constructor(private tokenService:TokenService, private alertService:AlertService) {
  }
  ngOnInit(): void {
    if(this.tokenService.getUserId()){
      let ws = new SockJS('http://localhost:8080/api/v1/ws');
      this.socketClient = Stomp.over(ws);
      this.socketClient.connect(
        {
          'Authorization:': 'Bearer '+this.tokenService.getToken()
        },()=>{
          console.log("connect to web sokect ...........");
          this.notificationsSubscruptions =  this.socketClient.subscribe(
            `/user/${this.tokenService.getUserId()}/notification`,
            (message:any)=>{
              console.log("dfgdfgfdgdfgdf"+ message);
              const notification = JSON.parse(message.body) ;

              this.alertService.alertInfo(notification.title)
            }
          )


        }
      );
    }
  }

  onToggleSidebar() {
    this.toggleSidebar.emit();
  }



}
