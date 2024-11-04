import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateUtilsService {

  constructor() { }


  formatDate(dateArray: number[] | undefined): string {
    if (!dateArray || dateArray.length < 7) {
      return 'Invalid Date'; // Handle undefined or invalid input
    }

    const date = new Date(
      dateArray[0],      // Year
      dateArray[1] - 1,  // Month (0-indexed)
      dateArray[2],      // Day
      dateArray[3],      // Hours
      dateArray[4],      // Minutes
      dateArray[5],      // Seconds
      Math.floor(dateArray[6] / 1000000) // Milliseconds
    );

    // Format date to dd/MM/yyyy HH:mm
    return date.toLocaleString('fr-FR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false // Use 24-hour format
    }).replace(',', ''); // Remove comma between date and time
  }
}
