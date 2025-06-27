import {Component, OnInit} from '@angular/core';
import {TokenService} from "./services/token/token.service";
import { Router, NavigationStart, Event as RouterEvent } from '@angular/router';
import {filter} from "rxjs/operators";
import {AuthService} from "./services/auth/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'delivery-ui';

  constructor(private tokenService: TokenService,
    private authService:AuthService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.handleTokenChanges();
    this.preventLoginWhenAuthenticated();
  }

  private handleTokenChanges() {
    this.tokenService.tokenChanges.subscribe(token => {
      if (!token) {
        console.log('User logged out');
        this.router.navigate(['/login']);
      } else {
        console.log('Token updated, refreshing roles');
        this.authService.refreshRoles();

        // Add navigation logic only if not already on a valid route
        const currentRoute = this.router.url;
        if (currentRoute === '/login' || currentRoute === '/') {
          this.navigateBasedOnRole();
        }
      }
    });

  }
  private preventLoginWhenAuthenticated() {
    this.router.events
      .pipe(
        filter((event: RouterEvent): event is NavigationStart =>
          event instanceof NavigationStart))
      .subscribe((event: NavigationStart) => {
        if (event.url === '/login' && this.tokenService.token) {
          this.router.navigate(['/']);
        }
      });
  }

  private navigateBasedOnRole() {
    setTimeout(() => {
      if (this.authService.isAdminUser()) {
        this.router.navigate(['/']);
      } else if (this.authService.isRegularUser()) {
        this.router.navigate(['/dplanning/my-weekly-p']);
      }
    }, 100); // Allow time for role propagation
  }
}
