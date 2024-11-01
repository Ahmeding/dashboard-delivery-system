import { Component, Input } from '@angular/core';

interface StatisticData {
  title: string;
  value: string;
  icon: string;
  trend: string;
  type: 'missions' | 'drivers' | 'orders' | 'revenue';
}

@Component({
  selector: 'app-statistics-card',
  templateUrl: './statistics-card.component.html',
  styleUrls: ['./statistics-card.component.scss']
})
export class StatisticsCardComponent {
  @Input() data!: StatisticData;

  isTrendPositive(): boolean {
    return this.data.trend.startsWith('+');
  }
}
