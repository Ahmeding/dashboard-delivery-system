import {Component, Input, OnInit} from '@angular/core';
import {MissionDto} from "../../../../../../ds-api/src/models/mission-dto";
import {VehicleDto} from "../../../../../../ds-api/src/models/vehicle-dto";
import {ChauffeurDto} from "../../../../../../ds-api/src/models/chauffeur-dto";
import {AlertService} from "../../../../../services/alert/alert.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";
import {TokenService} from "../../../../../services/token/token.service";
import {FraisMissionDto} from "../../../../../../ds-api/src/models/frais-mission-dto";
import {format, isAfter, isBefore, isValid, parseISO} from "date-fns";
import {isWithinInterval} from "date-fns";
import {FormUtils} from "../../../../../components/shared/services/form-utils/form-utils";
import {FormUtilsService} from "../../../../../components/shared/services/form-utils/form-utils.service";

@Component({
  selector: 'app-manage-trip-mission-modal',
  templateUrl: './manage-trip-mission-modal.component.html',
  styleUrls: ['./manage-trip-mission-modal.component.scss']
})
export class ManageTripMissionModalComponent implements OnInit {

  @Input() mission!: MissionDto;
  @Input() vehicles!: VehicleDto[];
  @Input() drivers!: ChauffeurDto[];
  @Input() isStartEndKm!: boolean;
  private myAssignedVehicle!: VehicleDto;


  expenseTypes = ['CARBURANT', 'ADDBLUE', 'EAU_LAVGLASSE', 'ELECTRICITE', 'AUTRE_SERVICE'];
  expensesArrayTmp: FraisMissionDto[] = [];
  editForm!: FormGroup;
  expenseForm!: FormGroup;
  expDate!: string;
  expense!: FraisMissionDto;

  constructor(
    private formUtils:FormUtilsService,
    private tokenServer: TokenService,
    private alertService: AlertService,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private missionService: MissionService) {
    this.connectedDriverID = this.tokenServer.getUserId() as number;
  }

  oldVehicleKM: number = 0;
  connectedDriverID !: number;

  private getVehicleKm() {
    if (this.mission.missionVehiclesDtos) {
      const driverVehicle = this.mission.missionVehiclesDtos
        .find(mv => mv.chauffeursVehicleDto?.some(cv => cv?.chauffeurDto?.id === this.connectedDriverID));

      if (driverVehicle?.vehicleDto?.kilometrage) {
        this.myAssignedVehicle = driverVehicle.vehicleDto;
        this.oldVehicleKM = driverVehicle?.vehicleDto?.kilometrage;
      }
    }

  }

  ngOnInit(): void {
    this.initForm();
    this.createExpensesFrom()
  }


  private initForm() {
    this.getVehicleKm();
    this.editForm = this.fb.group({
      kmDebut: [this.oldVehicleKM, [Validators.required, Validators.min(1)]],
      kmFin: [this.oldVehicleKM, [Validators.required, Validators.min(1)]]
    });
  }


  private createExpensesFrom(expense?: FraisMissionDto) {
    this.expenseForm = this.fb.group({
      id: [ null],
      type: [expense?.type || "CARBURANT" , Validators.required],
      quantity: [expense?.quantity || 12 , [Validators.required, Validators.min(1)]],
      cost: [expense?.cost || 80, [Validators.required, Validators.min(0)]],
      expenseDate: [expense?.expenseDate || "2025-05-04T08:00:00", Validators.required],
      missionId: this.mission.id
    })
  }

  isFieldInvalid(form: FormGroup, fieldName: string):boolean{
    return this.formUtils.isFieldInvalid(form,fieldName);
  }

  addExpense(): FraisMissionDto {
    this.expenseForm.markAllAsTouched();

    if (this.expenseForm && this.expenseForm.valid) {
      const type = this.expenseForm.value.type;
      const quantity = this.expenseForm.value.quantity;
      const cost = this.expenseForm.value.cost;
      const expenseDateValue = this.expenseForm.value.expenseDate;
      this.expDate =expenseDateValue;
     if(!this.checkValidDateInRange()){
       this.showDateError();
       return {};
     }

      if (this.isDuplicate(type, quantity, cost, expenseDateValue)) {
        this.alertService.alertWarning(
          'This expenses type, quantity,cost, and Expense ' +
          'Date combination already exists.');
        return {};
      }
      this.expense= this.expenseForm.value;
      this.expensesArrayTmp.push(this.expense)
      return this.expense
    }else {
      this.alertService.alertError("All filler are mandatory")
      return {};
    }
    return {};

  }
// Full example with date formatting
  private showDateError() {
    const start = format(parseISO(this.mission.dateDebut!), 'MMM d, yyyy');
    const end = format(parseISO(this.mission.dateFin!), 'MMM d, yyyy');
    this.alertService.alertWarning(
      `Please select a date between ${start} and ${end}`
    );
  }
  save() {
    if(this.isStartEndKm){
    this.updateMileageAndMissionStatus();
    }else{
    this.updateMissionExpenses();
    }


  }

  private updateStartKM(startKm: number) {
    this.missionService.updateKilometrage(
      {
        flag: "START_KM",
        id: this?.mission.id as number,
        vehicleId: this.myAssignedVehicle.id as number,
        kilometrage: startKm
      }).subscribe({
      next: (res) => {
        console.log("SUCCESS :", res);
        this.activeModal.close(res);
      },
      error: (err) => {
        console.log("FAILED :", err);
      }
    });
  }

  private updateEndKM(endKm: number) {
    this.missionService.updateKilometrage(
      {
        flag: "END_KM",
        id: this?.mission.id as number,
        vehicleId: this.myAssignedVehicle.id as number,
        kilometrage: endKm
      }).subscribe({
      next: (res) => {
        console.log("SUCCESS :", res);
        this.activeModal.close(res)
      },
      error: (err) => {
        this.alertService.alertWarning(err.error.msg);
        console.log("FAILED :", err);
      }
    });
  }

  isMissionStarts(mission: MissionDto): boolean {
    const status = mission.etatMission;
    return !(status === ("PLANNED"));
  }


  private isDuplicate(newType: string, newQuantity: number, newCost: number, newExpenseDate: string) {
    const isDuplicate = this.expensesArrayTmp.some(exp =>
      exp.type === newType &&
      exp.quantity === newQuantity &&
      exp.cost === newCost &&
      exp.expenseDate === newExpenseDate
    );
    return isDuplicate;
  }


   checkValidDateInRange() : boolean {// out => false, in => true
    const expenseDateValue = this.expenseForm.value.expenseDate;
    const missionStartDate = parseISO(this.mission.dateDebut!);
    const missioEndDate = parseISO(this.mission.dateFin!);


    const safeParse= (date:string |Date): Date | null=>{
      try{
        const parsed= typeof date == 'string' ? parseISO(date):new Date(date);
        return isValid(parsed)?parsed : null;
      }catch {
      return null;
      }
     }

     const entredDate = safeParse(expenseDateValue);
     const startDate = safeParse(missionStartDate);
     const endDate = safeParse(missioEndDate);

     if(!entredDate || !startDate || !endDate){
       console.log('Invalid date format detected');
       return false;
     }

      const isInRange= isWithinInterval(entredDate,{
        start:startDate,
        end: endDate
      })
     console.log('isInRange',isInRange);
     return isInRange;
  }

  removeExpense(index: number) {
    this.expensesArrayTmp.splice(index,1);
  }

  private updateMileageAndMissionStatus() {
    const startKm = this.editForm.get("kmDebut")?.value;
    const endKm = this.editForm.get("kmFin")?.value;
    switch (this.mission.etatMission) {
      case "PLANNED":
        // set init km & change status to in_progress
        this.updateStartKM(startKm);
        break;
      case "IN_PROGRESS":
        // set end km , set mission status to COMPLETE , update vehcile KM
        this.updateEndKM(endKm);
        break;
      default:
        this.alertService.alertWarning("Mission alaeredy completed")

    }

  }

  private updateMissionExpenses() {
    const allCurrentExpenses = this.expensesArrayTmp;
    if(allCurrentExpenses.length>0){
    this.mission.fraisMissionDtoList = allCurrentExpenses;
    this.missionService.updateExpensesMission({id: this.mission.id as number, body: allCurrentExpenses}).subscribe({
      next:(rep)=>{
        this.alertService.alertInfo("success update expenses");
        this.activeModal.close(rep)
        console.log(rep);
      },
      error:(err)=>{
        this.alertService.alertWarning("failed update expenses")
        console.log(err);}
    })
    }
  }
}
