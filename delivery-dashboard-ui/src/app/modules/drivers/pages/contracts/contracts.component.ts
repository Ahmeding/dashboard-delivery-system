import {Component, OnInit} from '@angular/core';
import {PageResponseReparationDto} from "../../../../../ds-api/src/models/page-response-reparation-dto";
import {ReparationDto} from "../../../../../ds-api/src/models/reparation-dto";
import {ReparationService} from "../../../../../ds-api/src/services/reparation.service";
import {Router} from "@angular/router";
import {PageResponseContratDto} from "../../../../../ds-api/src/models/page-response-contrat-dto";
import {ContratService} from "../../../../../ds-api/src/services/contrat.service";
import {ContratDto} from "../../../../../ds-api/src/models/contrat-dto";

@Component({
  selector: 'app-contracts',
  templateUrl: './contracts.component.html',
  styleUrls: ['./contracts.component.scss']
})
export class ContractsComponent implements OnInit{
  allContractsResponse: PageResponseContratDto={};
  page: number=0;
  size: number=4;
  selectedContract: ContratDto={};

  constructor(private contratService:ContratService,
              private router:Router) {
  }

  getAllContracts(){
    this.contratService.findAllContrats({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(contratRep)=>{
        this.allContractsResponse=contratRep;
      },error:(err)=>{
        console.log(err);
      }
    })
  }
  goToPreviousPage() {
    this.page--;
    this.getAllContracts();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllContracts();
  }

  goToNextPage() {
    this.page++;
    this.getAllContracts();
  }


  get  isLastPage():boolean{
    return this.page == this.allContractsResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allContractsResponse.totalPages as number -1;
  }

  goToFirstPage() {
    this.page=0;
    this.getAllContracts();
  }

  navigateToNewContract() {
    this.router.navigate(['drivers','new-contract'])
  }

  ngOnInit(): void {
    this.getAllContracts();
  }

  editContract(number: number) {

  }

  deleteContract(number: number) {

  }
}
