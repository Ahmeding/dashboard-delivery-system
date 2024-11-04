import {Component, OnInit} from '@angular/core';
import {PageResponseReparationDto} from "../../../../../ds-api/src/models/page-response-reparation-dto";
import {ReparationService} from "../../../../../ds-api/src/services/reparation.service";
import {ReparationDto} from "../../../../../ds-api/src/models/reparation-dto";
import {Router} from "@angular/router";

@Component({
  selector: 'app-repaires',
  templateUrl: './repaires.component.html',
  styleUrls: ['./repaires.component.scss']
})
export class RepairesComponent implements OnInit{
  allRepairsResponse: PageResponseReparationDto={};
  page: number=0;
  size: number=4;
  selectedRepaire: ReparationDto={};
  selectedRepair: ReparationDto={};

constructor(private repairService:ReparationService,
            private router:Router) {
}

getAllRepairs(){
  this.repairService.findAllReparations({
    page:this.page,
    size:this.size
  }).subscribe({
    next:(repairRep)=>{
      this.allRepairsResponse=repairRep;
    },error:(err)=>{
      console.log(err);
    }
  })
}
  goToPreviousPage() {
    this.page--;
    this.getAllRepairs();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllRepairs();
  }

  goToNextPage() {
    this.page++;
    this.getAllRepairs();
  }


  get  isLastPage():boolean{
    return this.page == this.allRepairsResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allRepairsResponse.totalPages as number -1;
  }

  goToFirstPage() {
    this.page=0;
    this.getAllRepairs();
  }

  navigateToNewRepair() {
    this.router.navigate(['vehicles','new-repair'])
  }

  ngOnInit(): void {
  this.getAllRepairs();
  }

  editRepaire(number: number) {

  }

  deleteRepair(number: number) {

  }

  editRepair(number: number) {

  }
}
