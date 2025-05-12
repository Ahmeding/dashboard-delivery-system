import {Component, OnInit} from '@angular/core';
import {PageResponseReparationDto} from "../../../../../ds-api/src/models/page-response-reparation-dto";
import {ReparationDto} from "../../../../../ds-api/src/models/reparation-dto";
import {ReparationService} from "../../../../../ds-api/src/services/reparation.service";
import {Router} from "@angular/router";
import {PageResponseContratDto} from "../../../../../ds-api/src/models/page-response-contrat-dto";
import {ContratService} from "../../../../../ds-api/src/services/contrat.service";
import {ContratDto} from "../../../../../ds-api/src/models/contrat-dto";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ViewContractModalComponent} from "./view-contract-modal/view-contract-modal.component";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {AlertService} from "../../../../services/alert/alert.service";
import {DateUtilsService} from "../../../../services/date-utils/date-utils.service";
import {StringUtilsService} from "../../../../services/string-utils/string-utils.service";

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

  constructor(
    private confirmBoxService:ConfirmBoxEvokeService,
    private alertService:AlertService,
    private dateUtilSdervice:DateUtilsService,
    private stringUtilsService:StringUtilsService,
    private modalService: NgbModal,
    private contratService:ContratService,
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

  formatDate(dateString: string | undefined):string{

   return dateString as string;
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


  deleteContract(contract_id: number) {
    if (contract_id === undefined) {
      this.alertService.alertError('Repair ID is missing. Unable to delete contract.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this contract?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.contratService.delete13({id:contract_id}).subscribe(() => {
            this.getAllContracts();
            this.alertService.alertSuccess('Deletion successfully completed');
          });
        }
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : 'Unable to delete contract');
      }
    )
  }

  goToUpdateContract(id: number) {
    this.router.navigate(['drivers','edit-contract',id])
  }

  openDriverModal(contract: ContratDto) {
    const modalRef = this.modalService.open(ViewContractModalComponent,{'size':'xl'});
    modalRef.componentInstance.selectedContract= contract;
  }

  formatDateTime(dateTimeString: string | undefined):string {
    return dateTimeString as string;
  }

  capitalizeName(name: string | undefined) {
    return this.stringUtilsService.capitalizeName(name!);
  }
}
