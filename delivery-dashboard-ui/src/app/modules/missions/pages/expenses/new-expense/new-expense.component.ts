import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {FraisMissionDto} from "../../../../../../ds-api/src/models/frais-mission-dto";

@Component({
  selector: 'app-new-expense',
  templateUrl: './new-expense.component.html',
  styleUrls: ['./new-expense.component.scss']
})
export class NewExpenseComponent {
  expense: FraisMissionDto = {};

  onSubmit(expenseForm: NgForm) {

  }
}
