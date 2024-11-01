import { Component, Input, Output, EventEmitter } from '@angular/core';

interface MenuItem {
  label: string;
  icon: string;
  route?: string;
  children?: MenuItem[];
  expanded?: boolean;
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  @Input() isCollapsed = false;
  @Input() isMobileMenuOpen = false;
  @Output() isMobileMenuOpenChange = new EventEmitter<boolean>();

  menuItems: MenuItem[] = [
    {
      label: 'Dashboard',
      icon: 'bi-speedometer2',
      route: ''
    },
    {
      label: 'Missions',
      icon: 'bi-briefcase',
      children: [
        { label: 'All Missions', icon: 'bi-list-check', route: '/missions/all' },
        { label: 'My Planning', icon: 'bi-calendar3', route: '/missions/planning' },
        { label: 'Expenses', icon: 'bi-cash', route: '/missions/expenses' }
      ]
    },
    {
      label: 'Drivers',
      icon: 'bi-person',
      children: [
        { label: 'All Drivers', icon: 'bi-people', route: '/drivers/all' },
        { label: 'Contracts', icon: 'bi-file-text', route: '/drivers/contracts' }
      ]
    },
    {
      label: 'Vehicles',
      icon: 'bi-truck',
      children: [
        { label: 'All Vehicles', icon: 'bi-truck', route: '/vehicles/all' },
        { label: 'Repairs', icon: 'bi-tools', route: '/vehicles/repairs' }
      ]
    },
    {
      label: 'Clients',
      icon: 'bi-building',
      children: [
        { label: 'All Clients', icon: 'bi-people', route: '/clients/all' },
        { label: 'Orders', icon: 'bi-cart', route: '/clients/orders' },
        { label: 'Quotes', icon: 'bi-file-earmark-text', route: '/clients/quotes' }
      ]
    },
    {
      label: 'Settings',
      icon: 'bi-gear',
      children: [
        { label: 'My Profile', icon: 'bi-person', route: '/settings/profile' },
        { label: 'Users', icon: 'bi-people', route: '/settings/users' },
        { label: 'Services', icon: 'bi-truck', route: '/settings/services' }
      ]
    }
  ];

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    this.isMobileMenuOpenChange.emit(this.isMobileMenuOpen);
  }

  closeMobileMenu() {
    this.isMobileMenuOpen = false;
    this.isMobileMenuOpenChange.emit(false);
  }
}
