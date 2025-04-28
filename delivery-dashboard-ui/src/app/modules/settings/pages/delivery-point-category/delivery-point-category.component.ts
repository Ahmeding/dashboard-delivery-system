import {Component, OnInit} from '@angular/core';
import {
  PageResponseDeliveryPointCategoryDto
} from "../../../../../ds-api/src/models/page-response-delivery-point-category-dto";
import {ServiceDeliveryDto} from "../../../../../ds-api/src/models/service-delivery-dto";
import {DeliveryPointCategoryDto} from "../../../../../ds-api/src/models/delivery-point-category-dto";
import {DeliveryPointCategoriesService} from "../../../../../ds-api/src/services/delivery-point-categories.service";
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewDpCategoryModalComponent} from "./new-dp-category-modal/new-dp-category-modal.component";
import {AlertService} from "../../../../services/alert/alert.service";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";

@Component({
  selector: 'app-delivery-point-category',
  templateUrl: './delivery-point-category.component.html',
  styleUrls: ['./delivery-point-category.component.scss']
})
export class DeliveryPointCategoryComponent implements OnInit{
  searchTerm: any;
  allDeliveryPointCategoryResponse: PageResponseDeliveryPointCategoryDto={};
  page: number=0;
  size:number=5;
  selectedDelivPointCategory: DeliveryPointCategoryDto={};

constructor(private alertService:AlertService,
            private confirmBoxService:ConfirmBoxEvokeService,
            private modalService:NgbModal,
            private deliveryPointCategoryService:DeliveryPointCategoriesService) {
}

  ngOnInit(): void {
    this.getAllDeliveryPointCategories();
  }

  private getAllDeliveryPointCategories() {
    this.deliveryPointCategoryService.findAllDeliveryPointCategories({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(dpCategoryResponse)=>{
        this.allDeliveryPointCategoryResponse=dpCategoryResponse;
      },error:(err)=>{
        console.log(err);
      }
    })
  }
  filterServices() {

  }

  openNewCategoryModal() {
    const modalRef =this.modalService.open(NewDpCategoryModalComponent,{
      size:'lg',
      centered:true,
      backdrop:'static'
    });

    modalRef.result.then((result)=>{
      if(result){
        console.log("new delivery point category");
        this.getAllDeliveryPointCategories();
      }
    }).catch((error)=>{
      console.log("Modale dismissed");
    });
  }

  deleteDelivPointCategory(categoryId: number) {
    if (categoryId === undefined) {
      this.alertService.alertError('Delivery Point Category ID is missing. Unable to delete category.');
      return;
    }
    this.confirmBoxService.customFour(`Are you sure you want to delete this category ?`, 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        this.confirmDeletingCategory(categoryId);
      },
      (err) => {
        this.alertService.alertError(err.error !== null ? err.error.message : `Unable to delete driver`);
      }
    )
  }

  editDelivPointCategory(dpCategory: DeliveryPointCategoryDto) {
    const modalRef =this.modalService.open(NewDpCategoryModalComponent,{
      size:'lg',
      centered:true,
      backdrop:'static'
    });

    modalRef.componentInstance.selectedDpCategory= dpCategory || {};
    modalRef.result.then((result)=>{
      if(result){
        console.log("new delivery point category");
        this.getAllDeliveryPointCategories();
      }
    }).catch((error)=>{
      console.log("Modale dismissed");
    });
  }



  goToPreviousPage() {
    this.page--;
    this.getAllDeliveryPointCategories();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllDeliveryPointCategories();
  }

  goToNextPage() {
    this.page++;
    this.getAllDeliveryPointCategories();
  }


  get  isLastPage():boolean{
    return this.page == this.allDeliveryPointCategoryResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allDeliveryPointCategoryResponse.totalPages as number -1;
  }

  goToFirstPage() {
    this.page=0;
    this.getAllDeliveryPointCategories();
  }

  onPageSizeChange() {

  }

  private confirmDeletingCategory(categoryId: number) {
    this.deliveryPointCategoryService.delete9({id:categoryId}).subscribe({
      next:()=>{this.alertService.alertInfo("Category deleted successfully.");
        this.getAllDeliveryPointCategories()},
      error:(err)=>{this.alertService.alertWaring("An error occurred while deleting the category. Please try again.");
        console.log(err);
      }
    })
  }
}
