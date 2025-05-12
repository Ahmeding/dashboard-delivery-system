import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {MainComponent} from "./modules/main/main.component";
import {authGuard} from "./services/guard/auth.guard";
import {UnauthorizedComponent} from "./pages/unauthorized/unauthorized.component";
import {RoleRedirectGuard} from "./services/guard/role-redirect.guard";


const routes: Routes = [
 /* { path: '', redirectTo: '', pathMatch: 'full' },*/
  { path: 'login', component: LoginComponent },
  { path: 'unauthorized', component: UnauthorizedComponent },

  {
    path:'',
    component: MainComponent,
    children:[
      { path: 'main',
        loadChildren: () => import('./modules/main/main.module').then(m => m.MainModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_ADMIN','ROLE_USER']}
      },
      {
        path: 'missions',  loadChildren: () => import('./modules/missions/missions.module').then(m => m.MissionsModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_ADMIN']}
      },
      {
        path: 'drivers',
        loadChildren: () => import('./modules/drivers/drivers.module').then(m => m.DriversModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_ADMIN']}
      },
      {
        path: 'vehicles',
        loadChildren: () => import('./modules/vehicles/vehicles.module').then(m => m.VehiclesModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_ADMIN']}
      },
      {
        path: 'stat',
        loadChildren: () => import('./modules/statistics/statistics.module').then(m => m.StatisticsModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_ADMIN']}
      },
      {
        path: 'clients',
        loadChildren: () => import('./modules/clients/clients.module').then(m => m.ClientsModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_ADMIN']}
      },
      {
        path: 'settings',
        loadChildren: () => import('./modules/settings/settings.module').then(m => m.SettingsModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_ADMIN','ROLE_USER']}
      },
      {
        path: 'dplanning',
        loadChildren: () => import('././modules/driver-planning/driver-planning.module').then(m => m.DriverPlanningModule),
        canActivate:[authGuard],
        data:{roles:['ROLE_USER']}
      },
      { path: '',
        loadChildren: () =>
          import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule),
        canActivate:[authGuard,RoleRedirectGuard],
        data:{roles:['ROLE_ADMIN','ROLE_USER']}
      },
    ]
  },
  { path: '**', redirectTo: '' } // Handle 404
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
