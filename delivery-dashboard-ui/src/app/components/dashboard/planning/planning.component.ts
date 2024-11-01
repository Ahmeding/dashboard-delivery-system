import { Component, OnInit } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {EventModalComponent} from "./event-modal/event-modal/event-modal.component";

@Component({
  selector: 'app-planning',
  templateUrl: './planning.component.html',
  styleUrls: ['./planning.component.scss']
})
export class PlanningComponent implements OnInit {
  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    events: [
      {
        title: 'Delivery to Client A',
        start: new Date().toISOString(),
        backgroundColor: '#3498db'
      },
      {
        title: 'Pickup from Warehouse',
        start: this.addDays(new Date(), 1).toISOString(),
        backgroundColor: '#2ecc71'
      },
      {
        title: 'Express Delivery',
        start: this.addDays(new Date(), 2).toISOString(),
        backgroundColor: '#e74c3c'
      }
    ],
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    eventClick: this.handleEventClick.bind(this),
    dateClick: this.handleDateClick.bind(this),
    eventDidMount: (info) => {
      // Enable event stacking
      if (info.event.allDay) {
        info.el.style.position = 'relative';
      }
    }
  };

  constructor(private modalService: NgbModal) {}

  ngOnInit() {}

  private addDays(date: Date, days: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  }

  handleEventClick(arg: any) {
    const modalRef = this.modalService.open(EventModalComponent);
    modalRef.componentInstance.event = arg.event;

    modalRef.result.then((result) => {
      if (result) {
        if (result.delete) {
          arg.event.remove();
        } else {
          arg.event.setProp('title', result.title);
          arg.event.setStart(result.start);
          arg.event.setEnd(result.end);
          arg.event.setProp('backgroundColor', result.backgroundColor);
          arg.event.setExtendedProp('description', result.description);
          arg.event.setExtendedProp('location', result.location);
        }
      }
    }).catch(() => {
      // Modal dismissed
    });
  }

  handleDateClick(arg: any) {
    const modalRef = this.modalService.open(EventModalComponent);
    modalRef.componentInstance.date = arg.date;

    modalRef.result.then((result) => {
      if (result) {
        this.calendarOptions.events = [
          ...(this.calendarOptions.events as any[]),
          {
            title: result.title,
            start: result.start,
            end: result.end,
            backgroundColor: result.backgroundColor,
            extendedProps: {
              description: result.description,
              location: result.location
            }
          }
        ];
      }
    }).catch(() => {
      // Modal dismissed
    });
  }
}
