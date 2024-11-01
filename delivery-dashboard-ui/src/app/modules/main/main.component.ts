import {Component, HostListener} from '@angular/core';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent {
  isSidebarCollapsed = false;
  isMobileMenuOpen = false;

  @HostListener('window:resize', ['$event'])
  onResize() {
    if (window.innerWidth <= 991.98) {
      this.isSidebarCollapsed = false;
    }
  }

  toggleSidebar() {
    if (window.innerWidth <= 991.98) {
      this.isMobileMenuOpen = !this.isMobileMenuOpen;
    } else {
      this.isSidebarCollapsed = !this.isSidebarCollapsed;
    }
  }
}
