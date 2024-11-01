import { Component, OnInit } from '@angular/core';
import { ChartConfiguration } from 'chart.js';

interface StatisticCard {
  title: string;
  value: string;
  icon: string;
  trend: string;
  type: 'missions' | 'drivers' | 'orders' | 'revenue';
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  statistics: StatisticCard[] = [
    { title: 'Active Missions', value: '24', icon: 'bi-truck', trend: '+12%', type: 'missions' },
    { title: 'Available Drivers', value: '18', icon: 'bi-person', trend: '-5%', type: 'drivers' },
    { title: 'Total Orders', value: '156', icon: 'bi-cart', trend: '+25%', type: 'orders' },
    { title: 'Revenue', value: '$12,450', icon: 'bi-currency-dollar', trend: '+18%', type: 'revenue' }
  ];

  revenueChartConfig: ChartConfiguration = {
    type: 'line',
    data: {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
      datasets: [{
        label: 'Revenue',
        data: [12, 19, 15, 25, 22, 30],
        borderColor: '#3498db',
        tension: 0.4,
        fill: false
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          display: true,
          position: 'top'
        },
        title: {
          display: true,
          text: 'Monthly Revenue'
        }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  };

  deliveriesChartConfig: ChartConfiguration = {
    type: 'bar',
    data: {
      labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      datasets: [{
        label: 'Deliveries',
        data: [65, 59, 80, 81, 56, 40, 30],
        backgroundColor: '#2ecc71'
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          display: true,
          position: 'top'
        },
        title: {
          display: true,
          text: 'Weekly Deliveries'
        }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  };

  ngOnInit() {}
}
