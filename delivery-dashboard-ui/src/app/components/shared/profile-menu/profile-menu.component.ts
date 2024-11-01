import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-menu',
  templateUrl: './profile-menu.component.html',
  styleUrls: ['./profile-menu.component.scss']
})
export class ProfileMenuComponent {
  user = {
    name: 'John Doe',
    email: 'john.doe@example.com',
    avatar: 'assets/avatar.png'
  };

  constructor(private router: Router) {}

  navigateToProfile() {
    this.router.navigate(['/settings/profile']);
  }

  logout() {
    // Implement logout logic here
    console.log('Logging out...');
  }
}
