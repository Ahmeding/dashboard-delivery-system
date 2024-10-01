import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { ChangePasswordPageComponent } from './pages/profil/change-password-page/change-password-page.component';
import { DashboardPageComponent } from './pages/dashboard-page/dashboard-page.component';
import {StatisticsPageComponent} from "./pages/statistics-page/statistics-page.component";
import {OverviewPageComponent} from "./pages/overview-page/overview-page.component";
import {ApplicationGuardService} from "./services/guard/application-guard.service";
import {ProfilPageComponent} from "./pages/profil/profil-page/profil-page.component";
import {UtilisateurPageComponent} from "./pages/utilisateur/utilisateur-page/utilisateur-page.component";
import {ChauffeurPagesComponent} from "./pages/chauffeurs/chauffeur-pages/chauffeur-pages.component";
import {AddChauffeurPagesComponent} from "./pages/chauffeurs/add-chauffeur-pages/add-chauffeur-pages.component";
import {AddMissionPagesComponent} from "./pages/missions/add-mission-pages/add-mission-pages.component";
import {MissionPageComponent} from "./pages/missions/mission-page/mission-page.component";
import {FraisMissionPageComponent} from "./pages/missions/frais-mission-page/frais-mission-page.component";
import {AddFraisMissionPageComponent} from "./pages/missions/add-frais-mission-page/add-frais-mission-page.component";
import {ReparationPageComponent} from "./pages/vehicules/reparation/reparation-page/reparation-page.component";
import {
  AddReparationPageComponent
} from "./pages/vehicules/reparation/add-reparation-page/add-reparation-page.component";
import {ContratPageComponent} from "./pages/chauffeurs/contrats/contrat-page/contrat-page.component";
import {AddContratPageComponent} from "./pages/chauffeurs/contrats/add-contrat-page/add-contrat-page.component";
import {VehiculePageComponent} from "./pages/vehicules/vehicule-page/vehicule-page.component";
import {AddVehiculePageComponent} from "./pages/vehicules/add-vehicule-page/add-vehicule-page.component";

const routes: Routes = [
  {
    path: 'login',
    component: LoginPageComponent,

  },
  {
    path: 'register',
    component: RegisterPageComponent
  },
  {
    path: '',
    component: DashboardPageComponent,
    canActivate:[ApplicationGuardService],
    children:[
      {
        path:'overview',
        component: OverviewPageComponent,
        canActivate:[ApplicationGuardService],

      },
      {
        path:'statistics',
        component: StatisticsPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'password',
        component: ChangePasswordPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'profil',
        component: ProfilPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'users',
        component: UtilisateurPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'chauffeurs',
        component: ChauffeurPagesComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'newchauffeur',
        component: AddChauffeurPagesComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'contrats',
        component: ContratPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'newcontrat',
        component: AddContratPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'missions',
        component: MissionPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'newmission',
        component: AddMissionPagesComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'fraismissions',
        component: FraisMissionPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'newfraismission',
        component: AddFraisMissionPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'vehicules',
        component: VehiculePageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'newvehicule',
        component: AddVehiculePageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'reparations',
        component: ReparationPageComponent,
        canActivate:[ApplicationGuardService],
      },
      {
        path: 'newreparation',
        component: AddReparationPageComponent,
        canActivate:[ApplicationGuardService],
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
