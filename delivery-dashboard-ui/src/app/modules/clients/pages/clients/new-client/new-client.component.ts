import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {InputFillUtilsService} from "../../../../../services/input-fill-utils/input-fill-utils.service";
import {ClientDto} from "../../../../../../ds-api/src/models/client-dto";
import {AlertService} from "../../../../../services/alert/alert.service";
import {ClientService} from "../../../../../../ds-api/src/services/client.service";
import {fileName} from "ng-openapi-gen/lib/gen-utils";
import {DeliveryPointDto} from "../../../../../../ds-api/src/models/delivery-point-dto";


@Component({
  selector: 'app-new-client',
  templateUrl: './new-client.component.html',
  styleUrls: ['./new-client.component.scss']
})
export class NewClientComponent implements OnInit {


  clientValue: ClientDto={};
  clientForm: FormGroup= new FormGroup({});
  isEditMode = false;
  isSubmitting = false;
  clientId: number | null = null;
  DeliveryPoint! :DeliveryPointDto;


  constructor(
    private fillInputService: InputFillUtilsService,
    private fb: FormBuilder,
    private clientService: ClientService,
    private router: Router,
    private route: ActivatedRoute,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.initForm();
    this.clientId = Number( this.route.snapshot.paramMap.get('id'));
    if (this.clientId) {
      this.isEditMode = true;
      this.loadClient(this.clientId);
    }
  }

  private initForm() {
    this.clientForm = this.fb.group({
      id: [this.clientValue.id||''],
      firstname: [this.clientValue.firstname||'', Validators.required],
      lastname: [this.clientValue.lastname||'', Validators.required],
      email: [this.clientValue.email||'', [Validators.required, Validators.email]],
      phoneNum: [this.clientValue.phoneNum||'', Validators.required],
      siren: [this.clientValue.siren||''],
      siret: [this.clientValue.siret||''],
      description: [this.clientValue.description||''],
      address: this.fb.group({
        adresse1: [this.clientValue.address?.adresse1||'', Validators.required],
        adresse2: [this.clientValue.address?.adresse2||''],
        ville: [this.clientValue.address?.ville||'', Validators.required],
        codepostal: [this.clientValue.address?.codepostal||'', Validators.required],
        pays: [this.clientValue.address?.pays||'', Validators.required]
      })
    });
  }

  private loadClient(id: number) {
    this.clientService.findById16({id:id as number}).subscribe({
      next: (client) => {
        this.clientValue=client;
        this.clientForm.patchValue(this.clientValue as ClientDto);
      },
      error: (error) => {
        console.error('Error loading client:', error);
      }
    });
  }
  onSubmit() {
    if (this.clientForm.valid) {
      this.isSubmitting = true;
      const clientData = this.clientForm.value;

      const request$ = this.isEditMode
        ? this.clientService.save16({body:clientData})
        : this.clientService.save16({body:clientData});

      request$.subscribe({
        next: () => {
          this.router.navigate(['clients/all']);
          this.isEditMode
            ?this.alertService.alertSuccess("Editing completed successfully!")
            :this.alertService.alertSuccess("Registration completed successfully!")

        },
        error: (error) => {
          console.error('Error saving client:', error);
          this.isSubmitting = false;
          // Show error notification
          this.isEditMode
            ?this.alertService.alertError("An error occurred while updating the Client. [" + error.error.msg + "]")
            :this.alertService.alertError("Registration completed registring!")
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

  saveClient(clientValue: ClientDto, sucessAction: string, failedAction:string){
    this.clientService.save16({body:clientValue}).subscribe({
      next:(response)=>{
        this.alertService.alertSuccess(sucessAction + " completed successfully!")
        this.router.navigate(['clients/all']);
      },
      error:(err)=>{
        console.log(err);
        this.alertService.alertError("An error occurred while " + failedAction + " the Client. [" + err.error.msg + "]")

      }
    })
  }
  isFieldInvalid(fieldName: string): boolean {
    const field = this.clientForm.get(fieldName);
    return field ? (field.invalid && (field.dirty || field.touched)) : false;
  }
  inputFillDataOnClick() {
   const name = this.fillInputService.generateRandomName();
   this.clientForm.get('firstname')?.setValue(name.firstName)
   this.clientForm.get('lastname')?.setValue(name.lastName)
   this.clientForm.get('email')?.setValue(name.email)

   const numPhone= this.fillInputService.generateFrenchPhoneNumber();
   this.clientForm.get('phoneNum')?.setValue(numPhone)

    const numNameStreet = this.fillInputService.generateRandomFrenchAddress();
    this.clientForm.get('address.adresse1')?.setValue(numNameStreet)

    const randomCity = this.fillInputService.generateRandomCityAndPostalCode();
    this.clientForm.get('address.codepostal')?.setValue(randomCity.postalCode)
    this.clientForm.get('address.ville')?.setValue(randomCity.city)
    this.clientForm.get('address.pays')?.setValue("France")

  }
}
