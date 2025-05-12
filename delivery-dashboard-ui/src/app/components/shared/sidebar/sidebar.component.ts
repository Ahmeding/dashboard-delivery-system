import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';
import {AuthService} from "../../../services/auth/auth.service";

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
export class SidebarComponent implements OnInit{

  @Input() isCollapsed = false;
  @Input() isMobileMenuOpen = false;
  @Output() isMobileMenuOpenChange = new EventEmitter<boolean>();
  menuItems: MenuItem[] = [];
  constructor(private authService: AuthService) {
  }
  /*menuItems: MenuItem[] = [
    {
      label: 'Dashboard',
      icon: 'bi-speedometer2',
      route: ''
    },
    {
      label: 'Planning',
      icon: 'bi-briefcase',
      children: [
        { label: 'Weekly Planning', icon: 'bi-calendar3', route: '/missions/w-planning' },
        { label: 'Expenses', icon: 'bi-cash', route: '/missions/expenses' },
        { label: 'All Missions', icon: 'bi-list-check', route: '/missions/all' },
        { label: 'My Planning', icon: 'bi-calendar3', route: '/missions/planning' }
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
        { label: 'Services', icon: 'bi-truck', route: '/settings/services' },
        { label: 'D-Point Categories', icon: 'bi-tags', route: '/settings/categories' }
      ]
    }
  ];*/

  buildMenu(){
    if(this.authService.isAdminUser()){
      this.menuItems = this.getAdminMenu();
    }else {
      this.menuItems = this.getUserMenu();
    }


}

private getAdminMenu(): MenuItem[]{
        return [
          {
            label: 'Dashboard',
            icon: 'bi-speedometer2',
            route: ''
          },
          {
            label: 'Statistics',
            icon: 'bi bi-clipboard-data',
            route: 'stat/statistics'
          },
          {
            label: 'Planning',
            icon: 'bi-briefcase',
            children: [
              { label: 'Weekly Planning', icon: 'bi-calendar3', route: '/missions/w-planning' },
              { label: 'Expenses', icon: 'bi-cash', route: '/missions/expenses' },
              { label: 'All Missions', icon: 'bi-list-check', route: '/missions/all' }
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
              { label: 'Services', icon: 'bi-truck', route: '/settings/services' },
              { label: 'D-Point Categories', icon: 'bi-tags', route: '/settings/categories' }
            ]
          }
        ];


}
  private getUserMenu(): MenuItem[] {
    return [
      {
        label: 'My Planning',
        icon: 'bi-briefcase',
        route: '/dplanning/my-weekly-p',
      },
      {
        label: 'Expenses',
        icon: 'bi-cash',
        route: '/missions/expenses',
      },
      {
        label: 'Settings',
        icon: 'bi-gear',
        children: [
          { label: 'My Profile', icon: 'bi-person', route: '/settings/profile' }
        ]
      }
    ];
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
    this.isMobileMenuOpenChange.emit(this.isMobileMenuOpen);
  }

  closeMobileMenu() {
    this.isMobileMenuOpen = false;
    this.isMobileMenuOpenChange.emit(false);
  }

  ngOnInit(): void {

    this.buildMenu();
  }
}
