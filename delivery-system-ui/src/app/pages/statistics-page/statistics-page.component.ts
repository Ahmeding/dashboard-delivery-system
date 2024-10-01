import {Component, OnInit} from '@angular/core';
import {Chart} from "chart.js";
@Component({
  selector: 'app-statistics-page',
  templateUrl: './statistics-page.component.html',
  styleUrl: './statistics-page.component.scss',
})
export class StatisticsPageComponent implements OnInit{

  public canvas: any;
  public ctx:any;



  ngOnInit(): void {
    this.createChart();
    this.createLineChart();
    this.createRadarChart();
    this.createPolarChart();
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



  createLineChart(){

    this.canvas=document.getElementById('myLineChart');
    this.ctx=this.canvas.getContext('2d');
    let chart = new Chart(this.ctx, {
      type: 'line', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: ['2022-05-10', '2022-05-11', '2022-05-12','2022-05-13',
          '2022-05-14', '2022-05-15', '2022-05-16','2022-05-17', ],
        datasets: [
          {
            label: "Sales",
            data: ['467','576', '572', '79', '92',
              '574', '573', '576'],
            backgroundColor: 'blue'
          },
          {
            label: "Profit",
            data: ['542', '542', '536', '327', '17',
              '0.00', '538', '541'],
            backgroundColor: 'limegreen'
          }
        ]
      },
      options: {
        aspectRatio:2.5
      }

    });
  }
  createChart(){

    this.canvas=document.getElementById('myChart');
    this.ctx=this.canvas.getContext('2d');

    let chart= new Chart(this.ctx, {
      type: 'bar', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: ['2022-01-10', '2022-02-11', '2022-03-12','2022-04-13',
          '2022-05-14', '2022-06-15', '2022-07-16','2022-08-17', ],
        datasets: [
          {
            label: "Sales",
            data: ['467','576', '572', '79', '92',
              '574', '573', '576'],
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 159, 64, 0.2)',
              'rgba(255, 205, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(201, 203, 207, 0.2)'
            ],

          borderColor: [
      'rgb(255, 99, 132)',
      'rgb(255, 159, 64)',
      'rgb(255, 205, 86)',
      'rgb(75, 192, 192)',
      'rgb(54, 162, 235)',
      'rgb(153, 102, 255)',
      'rgb(201, 203, 207)'
    ],
      borderWidth: 1
          },
          {
            label: "Profit",
            data: ['542', '542', '536', '327', '17',
              '0.00', '538', '541'],
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 159, 64, 0.2)',
              'rgba(255, 205, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(201, 203, 207, 0.2)'
            ],

            borderColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgb(255, 159, 64)',
              'rgb(255, 205, 86)',
              'rgb(75, 192, 192)',
              'rgb(54, 162, 235)',
              'rgb(153, 102, 255)',
              'rgb(201, 203, 207)'
            ],
            borderWidth: 1
          }
        ]
      },
      options: {
        aspectRatio:2.5,
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }

    });
  }

  private createPolarChart() {

    this.canvas=document.getElementById('myChartPolar');
    this.ctx=this.canvas.getContext('2d');
    let chart = new Chart(this.ctx, {
      type: 'polarArea', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: ['Red',
          'Green',
          'Yellow',
          'Grey',
          'Blue' ],
        datasets: [
          {
            label: "label: 'My First Dataset'",
            data: ['467','576', '572', '79', '92',],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(75, 192, 192)',
              'rgb(255, 205, 86)',
              'rgb(201, 203, 207)',
              'rgb(54, 162, 235)']
          },
        ]
      },
      options: {
        aspectRatio:2.5
      }

    });

  }


}
