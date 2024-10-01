import {Component, Input, OnInit} from '@angular/core';
import {MissionService} from "../../../../../../../ds-api/src/services/mission.service";
import {MissionDto} from "../../../../../../../ds-api/src/models/mission-dto";
import {Router} from "@angular/router";
import {FormControl, FormGroup} from "@angular/forms";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";
import {AlertService} from "../../../../../../services/alert/alert.service";
import {ToastrService} from "ngx-toastr";



@Component({
  selector: 'app-missions',
  templateUrl: './missions.component.html',
  styleUrls: ['./missions.component.scss']
})
export class MissionsComponent implements OnInit{


  private _mission:MissionDto={}

  missions:Array<MissionDto>=[]
  constructor(
    private router:Router,
    private alertService:AlertService,
    private missionService:MissionService,
    private confirmBoxService: ConfirmBoxEvokeService,
    private toastService:ToastrService) {
  }
  ngOnInit(): void {
    this.innitForm();
    this.getAllMission()
  }



  getAllMission(){
    this.missionService.findAll4().subscribe({
      next:(res)=>{
        console.log(res.length);
        this.missions=res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }


  misionEdit(id: number | undefined) {

  }

  missionDelete(mission_id: number) {
    if (mission_id === undefined) {
      console.log("mission_id : "+ mission_id);
      this.alertService.alertError('Mission ID is missing. Unable to delete mission.');
      return;
    }
    this.confirmBoxService.customFour('Are you sure you want to delete this theme?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
      resp => {
        if (resp.success) {
          this.missionService.delete4({id:mission_id}).subscribe(() => {
            this.getAllMission();
            this.alertService.alertSuccess('Deleted successfully');
            this.alertService.alertSuccess('Deletion successfully completed');
          });
        }
      },
      (err) => {
        this.toastService.error(err.error !== null ? err.error.message : 'Unable to delete mission');
      }
    )
  }

  missionDetails(id: number | undefined) {
      this.router.navigate([`details/${id}`]);
  }
  //for pagination
  page: number = 1;
  position: number = 1;
  handlePageChange(event: number) {
    this.page  = event
  }
  selectPage(page: string) {
    this.page = parseInt(page, 10) || 1;

  }

  //for filter
  filterForm!: FormGroup;



  private innitForm() {
    this.filterForm = new FormGroup({
      filter: new FormControl(5)
    });
  }
}
