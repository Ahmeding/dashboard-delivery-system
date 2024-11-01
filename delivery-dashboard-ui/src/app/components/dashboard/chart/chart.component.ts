import {Component, Input, OnInit, OnDestroy, AfterViewInit} from '@angular/core';
import {Chart, ChartConfiguration, registerables} from 'chart.js';
import {Subscription} from "rxjs";
import {ThemeService} from "../../../services/theme/theme.service";

Chart.register(...registerables);



 @Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements AfterViewInit, OnDestroy,OnInit {
  @Input() chartId!: string;
  @Input() chartConfig!: ChartConfiguration;
  private chart: Chart | undefined;

  ngAfterViewInit() {
    console.log(this.chartId);
    console.log(this.chartConfig);
    this.initChart();
  }

  private initChart() {
    const ctx = document.getElementById(this.chartId) as HTMLCanvasElement;
    if (ctx) {
      this.chart = new Chart(ctx, {
        type: this.chartConfig.type,
        data: this.chartConfig.data,
        options: {
          ...this.chartConfig.options,
          responsive: true,
          maintainAspectRatio: false
        }
      });
    }
  }

  ngOnDestroy() {
    if (this.chart) {
      this.chart.destroy();
    }
  }

  ngOnInit(): void {
  }
/*

  @Input() chartId!: string;
  @Input() chartConfig!: ChartConfiguration;
  private chart: Chart | undefined;
  private themeSubscription: Subscription | undefined;

  constructor(private themeService: ThemeService) {}

  ngOnInit() {
    if (this.chartId && this.chartConfig) {
      this.initChart();
      this.themeSubscription = this.themeService.isDarkTheme$.subscribe(() => {
        this.updateChartTheme();
      });
    }
  }

  private initChart() {
    const ctx = document.getElementById(this.chartId) as HTMLCanvasElement;
    if (ctx) {
      this.chart = new Chart(ctx, {
        type: this.chartConfig.type,
        data: this.chartConfig.data,
        options: {
          ...this.chartConfig.options,
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            ...this.chartConfig.options?.plugins,
            legend: {
              ...this.chartConfig.options?.plugins?.legend,
              labels: {
                color: getComputedStyle(document.documentElement).getPropertyValue('--text-color')
              }
            },
            title: {
              ...this.chartConfig.options?.plugins?.title,
              color: getComputedStyle(document.documentElement).getPropertyValue('--text-color')
            }
          },
          scales: {
            x: {
              ...this.chartConfig.options?.scales?.x,
              grid: {
                color: getComputedStyle(document.documentElement).getPropertyValue('--border-color')
              },
              ticks: {
                color: getComputedStyle(document.documentElement).getPropertyValue('--text-color')
              }
            },
            y: {
              ...this.chartConfig.options?.scales?.y,
              grid: {
                color: getComputedStyle(document.documentElement).getPropertyValue('--border-color')
              },
              ticks: {
                color: getComputedStyle(document.documentElement).getPropertyValue('--text-color')
              }
            }
          }
        }
      });
    }
  }

  private updateChartTheme() {
    if (this.chart) {
      const textColor = getComputedStyle(document.documentElement).getPropertyValue('--text-color');
      const borderColor = getComputedStyle(document.documentElement).getPropertyValue('--border-color');

      // Update legend text color
      if (this.chart.options.plugins?.legend) {
        this.chart.options.plugins.legend.labels = {
          ...this.chart.options.plugins.legend.labels,
          color: textColor
        };
      }

      // Update title color
      if (this.chart.options.plugins?.title) {
        this.chart.options.plugins.title.color = textColor;
      }

      // Update scales colors
      if (this.chart.options.scales?.x) {
        this.chart.options.scales.x.grid = {
          ...this.chart.options.scales.x.grid,
          color: borderColor
        };
        this.chart.options.scales.x.ticks = {
          ...this.chart.options.scales.x.ticks,
          color: textColor
        };
      }

      if (this.chart.options.scales?.y) {
        this.chart.options.scales.y.grid = {
          ...this.chart.options.scales.y.grid,
          color: borderColor
        };
        this.chart.options.scales.y.ticks = {
          ...this.chart.options.scales.y.ticks,
          color: textColor
        };
      }

      this.chart.update();
    }
  }

  ngOnDestroy() {
    if (this.chart) {
      this.chart.destroy();
    }
    if (this.themeSubscription) {
      this.themeSubscription.unsubscribe();
    }
  }

  ngAfterViewInit(): void {
  }
*/

}
