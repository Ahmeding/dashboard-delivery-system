import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {MainComponent} from "./modules/main/main.component";
import {authGuard} from "./services/guard/auth.guard";


const routes: Routes = [
  { path: '', redirectTo: '', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  {
    path:'',
    component: MainComponent,
    children:[
      { path: 'main',
        loadChildren: () => import('./modules/main/main.module').then(m => m.MainModule),
        canActivate:[authGuard]
      },
      { path: '',
        loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule),
        canActivate:[authGuard]
      },
      {
        path: 'missions',  loadChildren: () => import('./modules/missions/missions.module').then(m => m.MissionsModule),
        canActivate:[authGuard]
      },
      {
        path: 'drivers',
        loadChildren: () => import('./modules/drivers/drivers.module').then(m => m.DriversModule),
        canActivate:[authGuard]
      },
      {
        path: 'vehicles',
        loadChildren: () => import('./modules/vehicles/vehicles.module').then(m => m.VehiclesModule),
        canActivate:[authGuard]
      },
      {
        path: 'clients',
        loadChildren: () => import('./modules/clients/clients.module').then(m => m.ClientsModule),
        canActivate:[authGuard]
      },
      {
        path: 'settings',
        loadChildren: () => import('./modules/settings/settings.module').then(m => m.SettingsModule),
        canActivate:[authGuard]
      },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
