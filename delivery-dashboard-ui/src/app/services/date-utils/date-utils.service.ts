import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateUtilsService {

  constructor() { }

  setToInputDate(datePMC: string | undefined):string{
    let convertedDate: string = '';
    if (datePMC) {
      const dateArray = JSON.stringify(datePMC)
      convertedDate=this.convertToInputDate(JSON.parse(dateArray));
    }
    return convertedDate;
  }
  private convertToInputDate(dateArray: number[] ) :string{
    const year= dateArray[0];
    const month= dateArray[1].toString().padStart(2,'0');
    const day= dateArray[2].toString().padStart(2,'0');
    return `${year}-${month}-${day}`;
  }

  formatToDate(dateString: string | undefined):string{
    let convertedDate: string = '';
    if (dateString) {
      const dateArray = JSON.stringify(dateString)
      convertedDate=this.convertToDisplay(JSON.parse(dateArray));
    }else{
      convertedDate="dd-MM-yyyy"
    }
    return convertedDate;
  }

  private convertToDisplay(dateArray: number[] ) :string{
    const year= dateArray[0];
    const month= dateArray[1].toString().padStart(2,'0');
    const day= dateArray[2].toString().padStart(2,'0');
    return `${day}-${month}-${year}`;
  }

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

  formatDateTime(dateTimeString: string | undefined):string {
    const dateArray =JSON.parse(JSON.stringify(dateTimeString));
    // Convert to Date
    const convertedDate = new Date(
      dateArray[0],              // Year
      dateArray[1] - 1,          // Month (JavaScript uses 0-based index)
      dateArray[2],              // Day
      dateArray[3],              // Hour
      dateArray[4],              // Minute
      dateArray[5],              // Second
      Math.floor(dateArray[6] / 1000000) // Milliseconds (from nanoseconds)
    );


    const formatter = new Intl.DateTimeFormat('en-GB', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false, // 24-hour format
    });

// Format the date and time
    let formattedDate = formatter.format(convertedDate);

// Replace slashes with dashes
    formattedDate = formattedDate.replace(/\//g, '-');

// Adjust to match your desired format (DD-MM-YYYY HH:MM)
    formattedDate = formattedDate.replace(',', '');
return formattedDate;
  }
}
