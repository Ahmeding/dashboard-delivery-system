import {Component, OnInit} from '@angular/core';
import {VehiculeDto} from "../../../../../../ds-api/src/models/vehicule-dto";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService} from "../../../../../services/alert/alert.service";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {AdresseDto} from "../../../../../../ds-api/src/models/adresse-dto";
import {ChauffeurService} from "../../../../../../ds-api/src/services/chauffeur.service";
import {DateUtilsService} from "../../../../../services/date-utils/date-utils.service";
interface RandomCity {
  city: string;
  postalCode: string;
}
@Component({
  selector: 'app-new-driver',
  templateUrl: './new-driver.component.html',
  styleUrls: ['./new-driver.component.scss']
})
export class NewDriverComponent  implements OnInit {


  driverValue: ChauffeurDto = {};
  dId: number | null = null;
  driverForm!: FormGroup;
  isLoading: boolean = false;
  private isNewDriver: boolean = true;

  constructor(
    private dateUtils: DateUtilsService,
    private route: ActivatedRoute,
    private router: Router,
    private driverService: ChauffeurService,
    private alertService: AlertService) {
  }


  ngOnInit(): void {
    this.initForm();
    this.dId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.dId) {
      this.loadDriverToEditing(this.dId);
    }
  }


  private initForm() {
    this.driverForm = new FormGroup({
      id: new FormControl(null),
      firstname: new FormControl(this.driverValue?.firstname || '', [Validators.required]),
      lastname: new FormControl(this.driverValue?.lastname || '', [Validators.required]),
      email: new FormControl(this.driverValue?.email || '', [Validators.required, Validators.email]),
      password: new FormControl(this.driverValue?.password || 'password'),
      phoneNum: new FormControl(this.driverValue?.phoneNum || '', [Validators.required, Validators.pattern('^[0-9]*$')]),
      dateOfBirth: new FormControl(this.driverValue?.dateOfBirth || '', [Validators.required]),
      drivinglicenseNbr: new FormControl(this.driverValue?.drivinglicenseNbr || '', [Validators.required, Validators.pattern(/^\d{12}$/)]),
      drivingLicenseType: new FormControl(this.driverValue?.drivingLicenseType || '', [Validators.required]),
      rib: new FormControl(this.driverValue?.rib || '', [Validators.required, Validators.pattern(/^[A-Z]{2}\d{2}\d{5}\d{5}\d{11}\d{2}$/)]),
      numSecu: new FormControl(this.driverValue?.numSecu || '', [Validators.required, Validators.pattern(/^\d{13}$/)]),
      address: new FormGroup({
        adresse1: new FormControl(this.driverValue.address?.adresse1 || '', [Validators.required]),
        adresse2: new FormControl(this.driverValue.address?.adresse2 || ''),
        codepostal: new FormControl(this.driverValue.address?.codepostal || '', [Validators.required, Validators.pattern('^[0-9]{5}$')]),
        pays: new FormControl(this.driverValue.address?.pays || '', [Validators.required]),
        ville: new FormControl(this.driverValue.address?.ville || '', [Validators.required])
      })
    })
  }

  private loadDriverToEditing(vId: number) {
    this.isNewDriver = false;
    this.driverService.findById17({
      id: vId
    }).subscribe({
      next: (responseD) => {
        this.driverValue = responseD;
        if (this.driverValue.dateOfBirth) {
          this.driverValue.dateOfBirth = this.dateUtils.setToInputDate(this.driverValue.dateOfBirth);
        }
        this.driverForm.patchValue(this.driverValue as ChauffeurDto);

      }, error: (err) => {
        console.log(err);
      }
    });
  }


  onSubmit() {
    if (this.driverForm.valid) {
      const driverData: ChauffeurDto = this.driverForm.value;
      if (this.driverValue?.id) {
        this.saveDriver(driverData, "Updating", "updating");
      } else {
        this.saveDriver(driverData, "Registration", "registering");
      }
    }

  }

  private saveDriver(driverValue: ChauffeurDto, successMsg: string, errorMesg: string) {
    this.driverService.save17({body: driverValue}).subscribe({
      next: (response) => {
        console.log(response);
        this.alertService.alertSuccess(successMsg + " completed successfully!")
        this.router.navigate(['drivers/all']);
      }, error: (err) => {
        console.log(err);
        this.alertService.alertError("An error occurred while " + errorMesg + " the driver. [" + err.error.msg + "]")

      }
    })
  }

  navigateToDriverList() {
    this.router.navigate(['drivers', 'all'])
    console.log("tolist");
  }


  generateRandomFrenchAddress(): string {
    // List of common French street names
    const streetNames: string[] = [
      "Rue de la Paix", "Avenue des Champs-Élysées", "Boulevard Saint-Germain", "Rue de Rivoli", "Place de la Concorde",
      "Rue Montmartre", "Avenue Victor Hugo", "Rue de la République", "Boulevard Haussmann", "Rue de l'Université",
      "Avenue Foch", "Rue des Martyrs", "Rue de l'Église", "Rue de la Liberté", "Boulevard des Capucines"
    ];
    // Generate a random street number (between 1 and 9999)
    const streetNumber = Math.floor(Math.random() * 9999) + 1;
    // Choose a random street name from the list
    const randomStreet = streetNames[Math.floor(Math.random() * streetNames.length)];
    // Combine the street number with the street name
    return `${streetNumber} ${randomStreet}`;
  }
  generateIBAN(): string {
    // List of some valid country codes for IBAN (2 letters)
    const countryCodes = ['FR', 'DE', 'GB', 'IT', 'ES', 'NL', 'BE', 'PT', 'LU', 'CH'];

    // Randomly select a country code from the list
    const countryCode = countryCodes[Math.floor(Math.random() * countryCodes.length)];

    // Generate random numbers for each section
    const randomTwoDigits = this.generateRandomDigits(2);
    const randomFiveDigits1 = this.generateRandomDigits(5);
    const randomFiveDigits2 = this.generateRandomDigits(5);
    const randomElevenDigits = this.generateRandomDigits(11);
    const randomTwoDigitsEnd = this.generateRandomDigits(2);

    // Construct the IBAN with a random country code
    return `${countryCode}${randomTwoDigits}${randomFiveDigits1}${randomFiveDigits2}${randomElevenDigits}${randomTwoDigitsEnd}`;
  }
  generateRandomDigits(length: number): string {
    let result = '';
    for (let i = 0; i < length; i++) {
      result += Math.floor(Math.random() * 10); // Random digit from 0 to 9
    }
    return result;
  }
  generateRandomNumSecu(): string {
    let num = Math.floor(Math.random() * 1e13).toString();
    return num.padStart(13, '0');
  }
  generateDrivingLicenseNumber() {
    return (Math.floor(100000000000 + Math.random() * 900000000000)).toString();
  }
  generateRandomCityAndPostalCode(): RandomCity {
    const citiesAndPostCodes = [
      { city: 'Paris', postalCode: '75000' },
      { city: 'Boulogne-Billancourt', postalCode: '92100' },
      { city: 'Saint-Denis', postalCode: '93200' },
      { city: 'Argenteuil', postalCode: '95100' },
      { city: 'Montreuil', postalCode: '93100' },
      { city: 'Nanterre', postalCode: '92000' },
      { city: 'Vitry-sur-Seine', postalCode: '94400' },
      { city: 'Créteil', postalCode: '94000' },
      { city: 'Asnières-sur-Seine', postalCode: '92600' },
      { city: 'Versailles', postalCode: '78000' },
      { city: 'Colombes', postalCode: '92700' },
      { city: 'Aubervilliers', postalCode: '93300' },
      { city: 'Aulnay-sous-Bois', postalCode: '93600' },
      { city: 'Courbevoie', postalCode: '92400' },
      { city: 'Rueil-Malmaison', postalCode: '92500' },
      { city: 'Champigny-sur-Marne', postalCode: '94500' },
      { city: 'Saint-Maur-des-Fossés', postalCode: '94100' },
      { city: 'Drancy', postalCode: '93700' },
      { city: 'Issy-les-Moulineaux', postalCode: '92130' },
      { city: 'Noisy-le-Grand', postalCode: '93160' },
      { city: 'Levallois-Perret', postalCode: '92300' },
      { city: 'Cergy', postalCode: '95000' },
      { city: 'Antony', postalCode: '92160' },
      { city: 'Neuilly-sur-Seine', postalCode: '92200' },
      { city: 'Clichy', postalCode: '92110' },
      { city: 'Ivry-sur-Seine', postalCode: '94200' },
      { city: 'Sarcelles', postalCode: '95200' },
      { city: 'Villejuif', postalCode: '94800' },
      { city: 'Le Blanc-Mesnil', postalCode: '93150' },
      { city: 'Pantin', postalCode: '93500' },
      { city: 'Maisons-Alfort', postalCode: '94700' },
      { city: 'Épinay-sur-Seine', postalCode: '93800' },
      { city: 'Évry-Courcouronnes', postalCode: '91000' },
      { city: 'Chelles', postalCode: '77500' },
      { city: 'Meaux', postalCode: '77100' },
      { city: 'Fontenay-sous-Bois', postalCode: '94120' },
      { city: 'Bondy', postalCode: '93140' },
      { city: 'Clamart', postalCode: '92140' },
      { city: 'Sartrouville', postalCode: '78500' },
      { city: 'Bobigny', postalCode: '93000' },
      { city: 'Sevran', postalCode: '93270' },
      { city: 'Corbeil-Essonnes', postalCode: '91100' },
      { city: 'Vincennes', postalCode: '94300' },
      { city: 'Montrouge', postalCode: '92120' },
      { city: 'Suresnes', postalCode: '92150' },
      { city: 'Saint-Ouen-sur-Seine', postalCode: '93400' },
      { city: 'Massy', postalCode: '91300' },
      { city: 'Meudon', postalCode: '92190' },
      { city: 'Puteaux', postalCode: '92800' },
      { city: 'Mantes-la-Jolie', postalCode: '78200' },
      { city: 'Rosny-sous-Bois', postalCode: '93110' },
      { city: 'Gennevilliers', postalCode: '92230' },
      { city: 'Livry-Gargan', postalCode: '93190' },
      { city: 'Alfortville', postalCode: '94140' },
      { city: 'Choisy-le-Roi', postalCode: '94600' },
      { city: 'Noisy-le-Sec', postalCode: '93130' },
      { city: 'Garges-lès-Gonesse', postalCode: '95140' },
      { city: 'La Courneuve', postalCode: '93120' },
      { city: 'Saint-Germain-en-Laye', postalCode: '78100' },
      { city: 'Melun', postalCode: '77000' },
      { city: 'Gagny', postalCode: '93220' },
      { city: 'Stains', postalCode: '93240' },
      { city: 'Bagneux', postalCode: '92220' },
      { city: 'Pontault-Combault', postalCode: '77340' },
      { city: 'Châtillon', postalCode: '92320' },
      { city: 'Poissy', postalCode: '78300' },
      { city: 'Villepinte', postalCode: '93420' },
      { city: 'Savigny-sur-Orge', postalCode: '91600' },
      { city: 'Bagnolet', postalCode: '93170' },
      { city: 'Sainte-Geneviève-des-Bois', postalCode: '91700' },
      { city: 'Tremblay-en-France', postalCode: '93290' },
      { city: 'Conflans-Sainte-Honorine', postalCode: '78700' },
      { city: 'Franconville', postalCode: '95130' },
      { city: 'Neuilly-sur-Marne', postalCode: '93330' },
      { city: 'Le Perreux-sur-Marne', postalCode: '94170' },
      { city: 'Châtenay-Malabry', postalCode: '92290' },
      { city: 'Montigny-le-Bretonneux', postalCode: '78180' },
      { city: 'Palaiseau', postalCode: '91120' },
      { city: 'Athis-Mons', postalCode: '91200' },
      { city: 'Villeneuve-Saint-Georges', postalCode: '94190' },
      { city: 'Trappes', postalCode: '78190' },
      { city: 'Les Mureaux', postalCode: '78130' },
      { city: 'Houilles', postalCode: '78800' },
      { city: 'Plaisir', postalCode: '78370' },
      { city: 'Nogent-sur-Marne', postalCode: '94130' },
      { city: 'Chatou', postalCode: '78400' },
      { city: 'Goussainville', postalCode: '95190' },
      { city: 'Viry-Châtillon', postalCode: '91170' },
      { city: 'L\'Haÿ-les-Roses', postalCode: '94240' },
      { city: 'Vigneux-sur-Seine', postalCode: '91270' },
      { city: 'Charenton-le-Pont', postalCode: '94220' },
      { city: 'Pontoise', postalCode: '95000' },
      { city: 'Cachan', postalCode: '94230' },
      { city: 'Savigny-le-Temple', postalCode: '77176' },
      { city: 'Clichy-sous-Bois', postalCode: '93390' },
      { city: 'Malakoff', postalCode: '92240' },
      { city: 'Villemomble', postalCode: '93250' },
      { city: 'La Garenne-Colombes', postalCode: '92250' },
      { city: 'Pierrefitte-sur-Seine', postalCode: '93380' },
      { city: 'Saint-Cloud', postalCode: '92210' },
      { city: 'Draveil', postalCode: '91210' },
      { city: 'Le Plessis-Robinson', postalCode: '92350' },
      { city: 'Thiais', postalCode: '94320' },
      { city: 'Yerres', postalCode: '91330' },
      { city: 'Ermont', postalCode: '95120' },
      { city: 'Bezons', postalCode: '95870' },
      { city: 'Le Chesnay-Rocquencourt', postalCode: '78150' },
      { city: 'Villiers-sur-Marne', postalCode: '94350' },
      { city: 'Grigny', postalCode: '91350' },
      { city: 'Herblay-sur-Seine', postalCode: '95220' },
      { city: 'Bois-Colombes', postalCode: '92270' },
      { city: 'Sannois', postalCode: '95110' },
      { city: 'Le Raincy', postalCode: '93340' },
      { city: 'La Verrière', postalCode: '78320' },
      { city: 'Saint-Maurice', postalCode: '94410' },
      { city: 'Les Lilas', postalCode: '93260' },
      { city: 'Bussy-Saint-Georges', postalCode: '77600' },
      { city: 'Cesson', postalCode: '77240' },
      { city: 'Combs-la-Ville', postalCode: '77380' },
      { city: 'Chanteloup-les-Vignes', postalCode: '78570' },
      { city: 'Claye-Souilly', postalCode: '77410' },
      { city: 'Bailly', postalCode: '78870' },
      { city: 'Saint-Brice-sous-Forêt', postalCode: '95350' },
      { city: 'Marly-le-Roi', postalCode: '78160' },
      { city: 'Andilly', postalCode: '95580' },
      { city: 'La Frette-sur-Seine', postalCode: '95530' },
      { city: 'Sainte-Marie', postalCode: '95810' },
      { city: 'Eaubonne', postalCode: '95600' },
      { city: 'Montmorency', postalCode: '95160' },
      { city: 'Écouen', postalCode: '95440' },
      { city: 'Louvres', postalCode: '95380' },
      { city: 'Beaumont-sur-Oise', postalCode: '95260' },
      { city: 'Domont', postalCode: '95330' },
      { city: 'Sevran', postalCode: '93270' },
    ];
    const randomIndex = Math.floor(Math.random() * citiesAndPostCodes.length);
    const randomCity = citiesAndPostCodes[randomIndex];
    return randomCity;
  }
  setEmailOnKeyUp($event: KeyboardEvent) {
    const fullname= this.driverForm.get('firstname')?.value +"."+this.driverForm.get('lastname')?.value;
    if($event.key === 'Backspace' || fullname === ""){
      this.driverForm.get('email')?.setValue("");
    }else{
      this.driverForm.get('email')?.setValue(fullname+"@gmail.com");
    }
  }
  generateFrenchPhoneNumber(): string {
    // Define the possible prefixes for mobile and landline numbers
    const prefixes = ['06', '07'];
    // Randomly select a prefix
    const prefix = prefixes[Math.floor(Math.random() * prefixes.length)];
    // Generate the 8 remaining digits
    const digits = Array.from({ length: 8 }, () => Math.floor(Math.random() * 10)).join('');
    // Format and return the phone number
    return `${prefix}${digits.slice(0, 2)}${digits.slice(2, 4)}${digits.slice(4, 6)}${digits.slice(6)}`;
  }
  onGeneratePhoneNumClick() {
    const phoneNum= this.generateFrenchPhoneNumber();
    this.driverForm.get('phoneNum')?.setValue(phoneNum);

    const generatedIBAN = this.generateIBAN();
    this.driverForm.get('rib')?.setValue(generatedIBAN);

    const random13DigitNumber = this.generateRandomNumSecu();
    this.driverForm.get('numSecu')?.setValue(random13DigitNumber);

    const generatedNumber = this.generateDrivingLicenseNumber();
    this.driverForm.get('drivinglicenseNbr')?.setValue(generatedNumber);

    const randomCity : RandomCity=this.generateRandomCityAndPostalCode();
    const randomAddress = this.generateRandomFrenchAddress();
    this.driverForm.get('address.adresse1')?.setValue(randomAddress);
    this.driverForm.get('address.codepostal')?.setValue(randomCity.postalCode);
    this.driverForm.get('address.ville')?.setValue(randomCity.city);
    this.driverForm.get('address.pays')?.setValue("France");
  }
}
