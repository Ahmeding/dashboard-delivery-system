import {AbstractControl, FormGroup} from "@angular/forms";

export class FormUtils{

   isFieldInvalid(form: FormGroup, fieldName: string): boolean {
    const field: AbstractControl | null = form.get(fieldName);
    return field ? (field.invalid && (field.dirty || field.touched)) : false;
  }
}
