import { Injectable } from '@angular/core';
import {AbstractControl, FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class FormUtilsService {

  constructor() { }

  isFieldInvalid(form: FormGroup, fieldName: string): boolean {
    const field = form.get(fieldName);

    return field ? (field.invalid && (field.dirty || field.touched))
      : false;
  }
}
