import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {ClientService} from "./client.service";

@Component({
  selector: 'app-new-client',
  templateUrl: './new-client.component.html',
  styleUrls: ['./new-client.component.scss']
})
export class NewClientComponent implements OnInit {
  clientForm: FormGroup= new FormGroup({});
  isEditMode = false;
  isSubmitting = false;
  clientId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.initForm();
  }

  ngOnInit() {
    this.clientId = this.route.snapshot.paramMap.get('id');
    if (this.clientId) {
      this.isEditMode = true;
      this.loadClient(this.clientId);
    }
  }

  private initForm() {
    this.clientForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNum: ['', Validators.required],
      siren: [''],
      siret: [''],
      description: [''],
      enabled: [true],
      address: this.fb.group({
        street: [''],
        city: [''],
        postalCode: [''],
        country: ['']
      })
    });
  }

  private loadClient(id: string) {
    this.clientService.getClient(id).subscribe({
      next: (client) => {
        this.clientForm.patchValue(client);
      },
      error: (error) => {
        console.error('Error loading client:', error);
        // Show error notification
      }
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.clientForm.get(fieldName);
    return field ? (field.invalid && (field.dirty || field.touched)) : false;
  }

  onSubmit() {
    if (this.clientForm.valid) {
      this.isSubmitting = true;
      const clientData = this.clientForm.value;

      const request$ = this.isEditMode
        ? this.clientService.updateClient(this.clientId!, clientData)
        : this.clientService.createClient(clientData);

      request$.subscribe({
        next: () => {
          this.router.navigate(['/clients']);
          // Show success notification
        },
        error: (error) => {
          console.error('Error saving client:', error);
          this.isSubmitting = false;
          // Show error notification
        }
      });
    } else {
      Object.keys(this.clientForm.controls).forEach(key => {
        const control = this.clientForm.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });
    }
  }
}
