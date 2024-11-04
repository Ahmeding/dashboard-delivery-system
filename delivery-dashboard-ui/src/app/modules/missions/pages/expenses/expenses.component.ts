import {Component, OnInit} from '@angular/core';
import {FraisMissionDto} from "../../../../../ds-api/src/models/frais-mission-dto";
import {PageResponseClientDto} from "../../../../../ds-api/src/models/page-response-client-dto";
import {PageResponseFraisMissionDto} from "../../../../../ds-api/src/models/page-response-frais-mission-dto";
import {FraisDeMissionService} from "../../../../../ds-api/src/services/frais-de-mission.service";
import {Router} from "@angular/router";
import {ex} from "@fullcalendar/core/internal-common";

@Component({
  selector: 'app-expenses',
  templateUrl: './expenses.component.html',
  styleUrls: ['./expenses.component.scss']
})
export class ExpensesComponent implements OnInit{

  allExpensesResponse: PageResponseFraisMissionDto= {};
  selectedExpenses: FraisMissionDto={};
  page: number=0;
  size: number=4;

  constructor(
    private expensesService:FraisDeMissionService,
    private router:Router
  ){}
  ngOnInit(): void {
    this.getAllExpenses();
  }


  getAllExpenses(){
    this.expensesService.findAllFraisMissions({
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
