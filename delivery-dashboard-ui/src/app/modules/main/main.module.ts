import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MainRoutingModule } from './main-routing.module';
import { MainComponent } from './main.component';
import {NavbarComponent} from "../../components/shared/navbar/navbar.component";
import {SidebarComponent} from "../../components/shared/sidebar/sidebar.component";
import {NotificationsComponent} from "../../components/shared/notifications/notifications.component";
import {ProfileMenuComponent} from "../../components/shared/profile-menu/profile-menu.component";
import {FooterComponent} from "../../components/shared/footer/footer.component";
import {ThemeToggleComponent} from "../../components/shared/theme-toggle/theme-toggle.component";
import {NgbDropdown, NgbDropdownMenu, NgbDropdownToggle} from "@ng-bootstrap/ng-bootstrap";
import {RouterModule} from "@angular/router";


@NgModule({
  declarations: [
    MainComponent,
    NavbarComponent,
    SidebarComponent,
    NotificationsComponent,
    ProfileMenuComponent,
    FooterComponent,
    ThemeToggleComponent,
  ],
  imports: [
    RouterModule, // Required for <router-outlet>
    CommonModule,
    MainRoutingModule,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu
  ]
})
export class MainModule { }
