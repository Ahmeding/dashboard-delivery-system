import { Component } from '@angular/core';

interface Notification {
  id: number;
  message: string;
  time: string;
  type: 'info' | 'warning' | 'success' | 'danger';
  isRead: boolean;
}

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent {
  notifications: Notification[] = [
    {
      id: 1,
      message: 'New delivery request from Client A',
      time: '5 min ago',
      type: 'info',
      isRead: false
    },
    {
      id: 2,
      message: 'Driver John reported delay in delivery',
      time: '10 min ago',
      type: 'warning',
      isRead: false
    },
    {
      id: 3,
      message: 'Delivery #123 completed successfully',
      time: '1 hour ago',
      type: 'success',
      isRead: true
    }
  ];

  unreadCount = this.notifications.filter(n => !n.isRead).length;

  markAsRead(notification: Notification) {
    notification.isRead = true;
    this.unreadCount = this.notifications.filter(n => !n.isRead).length;
  }

  markAllAsRead() {
    this.notifications.forEach(n => n.isRead = true);
    this.unreadCount = 0;
  }

  clearAll() {
    this.notifications = [];
    this.unreadCount = 0;
  }
}
