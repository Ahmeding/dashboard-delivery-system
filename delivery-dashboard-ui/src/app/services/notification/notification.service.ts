import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

export interface Notification {
  id: number;
  message: string;
  type: 'info' | 'warning' | 'success' | 'danger';
  timestamp: Date;
  isRead: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private wsSubject: WebSocketSubject<any>;
  private notifications = new BehaviorSubject<Notification[]>([]);

  constructor() {
    this.wsSubject = webSocket('ws://your-api-url/notifications');
    this.wsSubject.subscribe(
      (msg) => this.handleNewNotification(msg),
      (err) => console.error('WebSocket error:', err),
      () => console.log('WebSocket connection closed')
    );
  }

  private handleNewNotification(msg: any) {
    const notification: Notification = {
      id: msg.id,
      message: msg.message,
      type: msg.type,
      timestamp: new Date(msg.timestamp),
      isRead: false
    };

    const current = this.notifications.value;
    this.notifications.next([notification, ...current]);

    // Store in localStorage for persistence
    this.persistNotifications();
  }

  getNotifications(): Observable<Notification[]> {
    return this.notifications.asObservable();
  }

  markAsRead(id: number) {
    const updated = this.notifications.value.map(n =>
      n.id === id ? { ...n, isRead: true } : n
    );
    this.notifications.next(updated);
    this.persistNotifications();
  }

  markAllAsRead() {
    const updated = this.notifications.value.map(n => ({ ...n, isRead: true }));
    this.notifications.next(updated);
    this.persistNotifications();
  }

  private persistNotifications() {
    localStorage.setItem('notifications', JSON.stringify(this.notifications.value));
  }

  private loadPersistedNotifications() {
    const stored = localStorage.getItem('notifications');
    if (stored) {
      const notifications = JSON.parse(stored);
      this.notifications.next(notifications);
    }
  }
}
