import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StringUtilsService {

  constructor() { }


  capitalizeName(name: string): string {
    if (!name) return name; // Return if the name is empty or undefined
    return name
      .split(' ')  // Split by spaces in case there are multiple words
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())  // Capitalize first letter
      .join(' '); // Join back into a single string
  }


}
