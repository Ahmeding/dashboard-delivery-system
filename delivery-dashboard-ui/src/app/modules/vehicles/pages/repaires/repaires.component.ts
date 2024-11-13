import {Component, OnInit} from '@angular/core';
import {PageResponseReparationDto} from "../../../../../ds-api/src/models/page-response-reparation-dto";
import {ReparationService} from "../../../../../ds-api/src/services/reparation.service";
import {ReparationDto} from "../../../../../ds-api/src/models/reparation-dto";
import {Router} from "@angular/router";
import {VehiculeService} from "../../../../../ds-api/src/services/vehicule.service";
import {VehiculeDto} from "../../../../../ds-api/src/models/vehicule-dto";
import {FormControl, FormGroup} from "@angular/forms";
import {DateUtilsService} from "../../../../services/date-utils/date-utils.service";
import {ViewVehiculeModalComponent} from "../vehicles/view-vehicule-modal/view-vehicule-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ViewRepairModalComponent} from "./view-repair-modal/view-repair-modal.component";
import {AlertService} from "../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";

@Component({
  selector: 'app-repaires',
  templateUrl: './repaires.component.html',
  styleUrls: ['./repaires.component.scss']
})
export class RepairesComponent implements OnInit{


  allRepairsResponse: PageResponseReparationDto={};
  page: number=0;
  size: number=4;
  selectedRepair: ReparationDto={};

constructor(
  private confirmBoxService:ConfirmBoxEvokeService,
  private alertService:AlertService,
  private modalService:NgbModal,
    private dateUtilSdervice:DateUtilsService,
  private repairService:ReparationService,
  private vehiculeService:VehiculeService,
            private router:Router) {
}


  ngOnInit(): void {
    this.getAllRepairs();
  }

  formatDate(dateString: string | undefined):string{
  return this.dateUtilSdervice.formatToDate(dateString) as string;
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
  deleteRepair(repair_id: number) {
    if (repair_id === undefined) {
      this.alertService.alertError('Repair ID is missing. Unable to delete repair.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this repair?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.repairService.delete2({id:repair_id}).subscribe(() => {
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

  openRepairModal(repair: ReparationDto) {
    const modalRef = this.modalService.open(ViewRepairModalComponent, { size: 'xl' });
    modalRef.componentInstance.selectedRepair = repair;  // Pass data to modal
  }



}
