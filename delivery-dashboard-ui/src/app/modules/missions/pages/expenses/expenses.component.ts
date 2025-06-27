import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {
  PageResponseMissionExpenseResponse
} from "../../../../../ds-api/src/models/page-response-mission-expense-response";
import {MissionExpenseRequest} from "../../../../../ds-api/src/models/mission-expense-request";
import {MissionExpenseResponse} from "../../../../../ds-api/src/models/mission-expense-response";
import {MissionExpenseService} from "../../../../../ds-api/src/services/mission-expense.service";

@Component({
  selector: 'app-expenses',
  templateUrl: './expenses.component.html',
  styleUrls: ['./expenses.component.scss']
})
export class ExpensesComponent implements OnInit{

  allExpensesResponse: PageResponseMissionExpenseResponse= {};
  selectedExpenses: MissionExpenseResponse={};
  page: number=0;
  size: number=4;

  constructor(
    private expensesService:MissionExpenseService,
    private router:Router
  ){}
  ngOnInit(): void {
    this.getAllExpenses();
  }


  getAllExpenses(){
    this.expensesService.findAllExpenseMissionsPageable({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(expensesResp)=>{
        this.allExpensesResponse=expensesResp;
      },error:(err)=>{
        err.log(err);
      }
    })
  }
  navigateToNewExpenses() {
    this.router.navigate(['missions','new-expense']);
  }

  editVehicle(id:number) {

  }

  deleteVehicle(id:number) {

  }

  goToPreviousPage() {
    this.page--;
    this.getAllExpenses();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllExpenses();
  }

  goToNextPage() {
    this.page++;
    this.getAllExpenses();
  }


  get  isLastPage():boolean{
    return this.page == this.allExpensesResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allExpensesResponse.totalPages as number -1;
  }

  goToFirstPage() {
    this.page=0;
    this.getAllExpenses();
  }
}
