import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {MissionExpenseRequest} from "../../../../../../ds-api/src/models/mission-expense-request";

@Component({
  selector: 'app-new-expense',
  templateUrl: './new-expense.component.html',
  styleUrls: ['./new-expense.component.scss']
})
export class NewExpenseComponent {
  expense!: MissionExpenseRequest;

  onSubmit(expenseForm: NgForm) {

  }
}
