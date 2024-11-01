import {Component, NgZone, OnInit} from '@angular/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import {CalendarOptions} from "@fullcalendar/core";


import { format } from 'date-fns';
import { fr } from 'date-fns/locale';

@Component({
  selector: 'app-fcalendar',
  templateUrl: './fcalendar.component.html',
  styleUrls: ['./fcalendar.component.scss']
})
export class FcalendarComponent implements OnInit{

  constructor(private ngZone: NgZone) {
  }
  calendarOptions: CalendarOptions={};
  ngOnInit() {
     this.ngZone.run(()=>{
       this.initCalendar()
       }

     )
  }

  initCalendar(){
    this.calendarOptions = {
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      eventOverlap: true,
      initialView: 'timeGridWeek',
      allDaySlot: false,
      editable: true,
      slotMinTime: '06:00:00',
      slotMaxTime: '20:00:00',
      dayHeaderContent: (args) => {
        const weekday = format(args.date, 'EEE'); // Weekday in English
        const date = format(args.date, 'dd/MM'); // Date in French format
        return `${weekday}-${date}`;
      },
      events: [
        {
          title: 'Team Meeting',
          start: '2024-11-02T10:00:00',
          end: '2024-11-02T11:00:00',
          backgroundColor: '#4CAF50',
          textColor: '#fff'
        },
        {
          title: 'Project Deadline',
          start: '2024-10-27T11:00:00',
          backgroundColor: '#FF5722',
          textColor: '#fff'
        },
        {
          title: 'Mission LV Lyon',
          start: '2024-10-27T11:00:00',
          backgroundColor: '#FF5722',
          textColor: '#fff'
        },
        {
          title: 'Project Deadline',
          start: '2024-10-27T13:00:00',
          backgroundColor: '#FF5722',
          textColor: '#fff'
        },
        {
          title: 'Project Deadline',
          start: '2024-10-27T19:00:00',
          backgroundColor: '#FF5722',
          textColor: '#fff'
        },
        {
          title: 'Lunch Break',
          start: '2024-11-03T13:00:00',
          end: '2024-11-03T14:00:00',
          backgroundColor: '#FFC107',
          textColor: '#000'
        }
      ]
    };
}


  renderDayCell(arg: any) {
    // Create your button element
    const button = document.createElement('button');
    button.innerText = '+';
    button.className = 'add-event-button';
    button.style.margin = '5px';

    // Append the button to the cell's content
    arg.el.appendChild(button);

    // Add an event listener for the button click
    button.addEventListener('click', (event) => {
      event.stopPropagation(); // Prevent the dateClick event from firing
      this.openModal(arg.date); // Call your modal opening method here
    });
  }

  handleDateClick(arg: any) {
    console.log('Date clicked: ', arg.date);
    // Handle date click if needed
  }

  openModal(date: Date) {
    // Your logic to open a modal for adding an event
    console.log('Open modal for date: ', date);
  }
  saveEvent() {

  }
}
