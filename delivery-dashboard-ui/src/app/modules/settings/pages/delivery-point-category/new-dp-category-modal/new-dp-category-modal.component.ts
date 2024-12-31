import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {DeliveryPointCategoriesService} from "../../../../../../ds-api/src/services/delivery-point-categories.service";
import {DeliveryPointCategoryDto} from "../../../../../../ds-api/src/models/delivery-point-category-dto";


@Component({
  selector: 'app-new-dp-category-modal',
  templateUrl: './new-dp-category-modal.component.html',
  styleUrls: ['./new-dp-category-modal.component.scss']
})
export class NewDpCategoryModalComponent implements OnInit{

 @Input()selectedDpCategory : DeliveryPointCategoryDto={};
  dpCategoryValue:DeliveryPointCategoryDto={}
  dpCategoryForm!: FormGroup;

  constructor(
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    private dpCategoryService:DeliveryPointCategoriesService)  {
  }


  saveDpCategoty(){
  this.dpCategoryValue=this.dpCategoryForm.value;
    console.log(this.dpCategoryValue);

    this.dpCategoryService.save9({body:this.dpCategoryValue}).subscribe({
      next:(rep)=>{
        console.log(rep);
        this.activeModal.close(this.dpCategoryForm.value)
      },error(err){
        console.log(err);
      }
    })
  }

  private initCategoryForm() {

    if(!this.selectedDpCategory){
      this.selectedDpCategory={};
    }
    this.dpCategoryForm = this.fb.group({
      id:[this.selectedDpCategory.id || ''] ,
      reference:[this.selectedDpCategory.reference || ''] ,
      name:[this.selectedDpCategory.name ||'',Validators.required] ,
      description:[this.selectedDpCategory.description ||''],
      additionalFees:[this.selectedDpCategory.additionalFees ||'',[Validators.required,Validators.min(0),Validators.max(100),Validators.maxLength(3)]]
    });
  }

  ngOnInit(): void {
    this.initCategoryForm();
  }
}
