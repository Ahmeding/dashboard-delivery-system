import {Component, OnInit} from '@angular/core';
import {MissionDto} from "../../../../../ds-api/src/models/mission-dto";
import {ActivatedRoute} from "@angular/router";
import {MissionService} from "../../../../../ds-api/src/services/mission.service";
import { convertResponseToDto } from 'src/app/services/dto-converters/mission-converter';
import {FraisMissionDto} from "../../../../../ds-api/src/models/frais-mission-dto";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AlertService} from "../../../../services/alert/alert.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-manage-trip-mission',
  templateUrl: './manage-trip-mission.component.html',
  styleUrls: ['./manage-trip-mission.component.scss']
})
export class ManageTripMissionComponent implements OnInit {
  mission!: MissionDto;
  expenseTypes = ['CARBURANT', 'ADDBLUE', 'EAU_LAVGLASSE', 'ELECTRICITE', 'AUTRE_SERVICE'];
  editForm!: FormGroup;

  expensesArrayTmp: FraisMissionDto[] = [];
  currentExpenseForm!:FormGroup;


  constructor(
    private alertService:AlertService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private missionService:MissionService) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.loadMission(id);
    this.createForm()
    this.createExpense();
  }

  createForm() {
    this.editForm = this.fb.group({
      kmDebut: ['', [Validators.required, Validators.min(0)]],
      kmFin: ['', [Validators.required, Validators.min(0)]],
      expenses: this.fb.array([])
    });
  }

  get expenses() {
    return this.editForm.get('expenses') as FormArray;
  }

  createExpense(expense?: FraisMissionDto) {
   this.currentExpenseForm= this.fb.group({
      id: [expense?.id || null],
      name: [expense?.name || '', Validators.required],
      quantity: [expense?.quantity || 1, [Validators.required, Validators.min(1)]],
      cost: [expense?.cost || 0, [Validators.required, Validators.min(0)]],
      expenseDate: [expense?.expenseDate || '', Validators.required],
      type: [expense?.type || 'CARBURANT', Validators.required]
    });
  }

  addExpense() {
   this.currentExpenseForm.markAsTouched();
   if(this.currentExpenseForm && this.currentExpenseForm.valid){
     const type = this.currentExpenseForm.value.type;
     const qte = this.currentExpenseForm.value.quantity;
     const cost = this.currentExpenseForm.value.cost;
     const expenseDate= this.currentExpenseForm.value.expenseDate;
     this.expenses.push(this.currentExpenseForm);
     this.expensesArrayTmp.push(this.expenses.value)
   }
  }

  removeExpense(index: number) {
    this.expenses.removeAt(index);
  }

  loadMission(id: number) {
    this.missionService.findById7({id:id}).subscribe(mission => {
      this.mission = mission;
      this.editForm.patchValue({
        kmDebut: mission.kmDebut,
        kmFin: mission.kmFin
      });

      if (mission.fraisMissionDtoList) {
        mission.fraisMissionDtoList.forEach((expense: FraisMissionDto | undefined) => {
          this.expenses.push(this.createExpense(expense));
        });
      }
    });
  }

  saveMission() {
    if (this.editForm.valid) {
      const updatedMission: MissionDto = {
        ...this.mission,
        kmDebut: this.editForm.value.kmDebut,
        kmFin: this.editForm.value.kmFin,
        fraisMissionDtoList: this.editForm.value.expenses
      };

     /* this.missionService.update({id:this.mission.id as number,body:updatedMission}).subscribe({
        next: () => alert('Mission updated successfully!'),
        error: (err) => console.error('Update failed:', err)
      });
    */}
  }
}
