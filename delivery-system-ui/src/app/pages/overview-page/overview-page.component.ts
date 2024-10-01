import {Component, OnInit} from '@angular/core';
import {Chart} from "chart.js";

@Component({
  selector: 'app-overview-page',
  templateUrl: './overview-page.component.html',
  styleUrl: './overview-page.component.scss'
})
export class OverviewPageComponent implements OnInit{

  public canvas: any;
  public ctx:any;



  ngOnInit(): void {
    this.createRadarChart();
    this.createDonutChart();
  }

  private createDonutChart() {

    this.canvas=document.getElementById('myChartDonut');
    this.ctx=this.canvas.getContext('2d');
    let chart = new Chart(this.ctx, {
      type: 'doughnut', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: ['Red',
          'Green',
          'Yellow',
          'Blue' ],
        datasets: [
          {
            label: "label: 'My First Dataset'",
            data: ['467','576', '572', '79',],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
              'rgb(255, 205, 86)',
              'rgb(87,235,54)'],
            hoverOffset: 4
          },
        ]
      },
      options: {
        aspectRatio:2.5
      }

    });

  }
  createRadarChart(){

    this.canvas=document.getElementById('myChartRadar');
    this.ctx=this.canvas.getContext('2d');

    let chart = new Chart(this.ctx, {
      type: 'radar', //this denotes tha type of chart

      data: {
        labels: ['Eating',
          'Drinking',
          'Sleeping',
          'Designing',
          'Coding',
          'Cycling',
          'Running' ],
        datasets: [
          {
            label: "My First Dataset",
            data: [65, 59, 90, 81, 56, 55, 40],
            fill:true,
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgb(255, 99, 132)',
            pointBackgroundColor: 'rgb(255, 99, 132)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgb(255, 99, 132)'
          }, {
            label: 'My Second Dataset',
            data: [28, 48, 40, 19, 96, 27, 100],
            fill: true,
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgb(54, 162, 235)',
            pointBackgroundColor: 'rgb(54, 162, 235)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgb(54, 162, 235)'
          }
        ]
      },
      options: {
        elements:{
          line:{
            borderWidth:3
          }
        },
        aspectRatio:2.5
      }

    });
  }

}
