import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {MainComponent} from "./modules/main/main.component";


const routes: Routes = [
  { path: '', redirectTo: '/', pathMatch: 'full' },

  {
    path:'',
    component: MainComponent,
    children:[
      { path: 'main',
        loadChildren: () => import('./modules/main/main.module').then(m => m.MainModule)
      },
      { path: '',
        loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'missions',  loadChildren: () => import('./modules/missions/missions.module').then(m => m.MissionsModule)
      },
      {
        path: 'drivers',
        loadChildren: () => import('./modules/drivers/drivers.module').then(m => m.DriversModule)
      },
      {
        path: 'vehicles',
        loadChildren: () => import('./modules/vehicles/vehicles.module').then(m => m.VehiclesModule)
      },
      {
        path: 'clients',
        loadChildren: () => import('./modules/clients/clients.module').then(m => m.ClientsModule)
      },
      {
        path: 'settings',
        loadChildren: () => import('./modules/settings/settings.module').then(m => m.SettingsModule)
      },
    ]
  },


  { path: 'login', component: LoginComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
