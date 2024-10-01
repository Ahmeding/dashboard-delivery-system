import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { ChangePasswordPageComponent } from './pages/profil/change-password-page/change-password-page.component';
import { DashboardPageComponent } from './pages/dashboard-page/dashboard-page.component';
import { StatisticsPageComponent } from './pages/statistics-page/statistics-page.component';
import { SideBarMenuComponent } from './composants/side-bar-menu/side-bar-menu.component';
import { OverviewPageComponent } from './pages/overview-page/overview-page.component';
import {NgChartsModule} from "ng2-charts";
import { ClientPageComponent } from './pages/client/client-page/client-page.component';
import { UtilisateurPageComponent } from './pages/utilisateur/utilisateur-page/utilisateur-page.component';
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";
import {loggerInterceptor} from "./services/interceptor/logger.interceptor";
import { HeaderComponent } from './composants/header/header.component';
import { ProfilPageComponent } from './pages/profil/profil-page/profil-page.component';
import { ChauffeurPagesComponent } from './pages/chauffeurs/chauffeur-pages/chauffeur-pages.component';
import { AddChauffeurPagesComponent } from './pages/chauffeurs/add-chauffeur-pages/add-chauffeur-pages.component';
import { AddMissionPagesComponent } from './pages/missions/add-mission-pages/add-mission-pages.component';
import { MissionPageComponent } from './pages/missions/mission-page/mission-page.component';
import { VehiculePageComponent } from './pages/vehicules/vehicule-page/vehicule-page.component';
import { FraisMissionPageComponent } from './pages/missions/frais-mission-page/frais-mission-page.component';
import { AddFraisMissionPageComponent } from './pages/missions/add-frais-mission-page/add-frais-mission-page.component';
import { AddVehiculePageComponent } from './pages/vehicules/add-vehicule-page/add-vehicule-page.component';
import { ReparationPageComponent } from './pages/vehicules/reparation/reparation-page/reparation-page.component';
import { AddReparationPageComponent } from './pages/vehicules/reparation/add-reparation-page/add-reparation-page.component';
import { ContratPageComponent } from './pages/chauffeurs/contrats/contrat-page/contrat-page.component';
import { AddContratPageComponent } from './pages/chauffeurs/contrats/add-contrat-page/add-contrat-page.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    RegisterPageComponent,
    ChangePasswordPageComponent,
    DashboardPageComponent,
    StatisticsPageComponent,
    SideBarMenuComponent,
    OverviewPageComponent,
    ClientPageComponent,
    UtilisateurPageComponent,
    HeaderComponent,
    ProfilPageComponent,
    ChauffeurPagesComponent,
    AddChauffeurPagesComponent,
    AddMissionPagesComponent,
    MissionPageComponent,
    VehiculePageComponent,
    FraisMissionPageComponent,
    AddFraisMissionPageComponent,
    AddVehiculePageComponent,
    ReparationPageComponent,
    AddReparationPageComponent,
    ContratPageComponent,
    AddContratPageComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        NgChartsModule,
        FormsModule
    ],
  providers: [
    provideHttpClient(withFetch(),withInterceptors([loggerInterceptor])),
    provideClientHydration(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
