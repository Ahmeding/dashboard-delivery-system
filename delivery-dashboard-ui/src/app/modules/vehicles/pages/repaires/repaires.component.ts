import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {DateUtilsService} from "../../../../services/date-utils/date-utils.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ViewRepairModalComponent} from "./view-repair-modal/view-repair-modal.component";
import {AlertService} from "../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {PageResponseRepairResponse} from "../../../../../ds-api/src/models/page-response-repair-response";
import {RepairResponse} from "../../../../../ds-api/src/models/repair-response";
import {RepairService} from "../../../../../ds-api/src/services/repair.service";
import {VehicleService} from "../../../../../ds-api/src/services/vehicle.service";

@Component({
  selector: 'app-repaires',
  templateUrl: './repaires.component.html',
  styleUrls: ['./repaires.component.scss']
})
export class RepairesComponent implements OnInit{


  allRepairsResponse: PageResponseRepairResponse={};
  page: number=0;
  size: number=4;
  selectedRepair: RepairResponse={};

constructor(
  private confirmBoxService:ConfirmBoxEvokeService,
  private alertService:AlertService,
  private modalService:NgbModal,
    private dateUtilSdervice:DateUtilsService,
  private repairService:RepairService,
  private vehiculeService:VehicleService,
            private router:Router) {
}


  ngOnInit(): void {
    this.getAllRepairs();
  }


getAllRepairs(){
  this.repairService.findAllRepair({
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
  deleteRepair(repair_id: number) {
    if (repair_id === undefined) {
      this.alertService.alertError('Repair ID is missing. Unable to delete repair.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this repair?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.repairService.deleteRepair({id:repair_id}).subscribe(() => {
            this.getAllRepairs();
            this.alertService.alertSuccess('Deletion successfully completed');
          });
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : 'Unable to delete repair');
      }
    )
  }
*
  goToUpdateRepair(id: number) {
    this.router.navigate(['vehicles','edit-repair',id])
  }

  openRepairModal(repair: RepairResponse) {
    const modalRef = this.modalService.open(ViewRepairModalComponent, { size: 'xl' });
    modalRef.componentInstance.selectedRepair = repair;  // Pass data to modal
  }



}
