import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MissionDto} from "../../../../../ds-api/src/models/mission-dto";
import {ChauffeurDto} from "../../../../../ds-api/src/models/chauffeur-dto";
import {PageResponseMissionDto} from "../../../../../ds-api/src/models/page-response-mission-dto";
import {Router} from "@angular/router";
import {AlertService} from "../../../../services/alert/alert.service";
import {MissionService} from "../../../../../ds-api/src/services/mission.service";
import {ToastrService} from "ngx-toastr";
import {ConfirmBoxEvokeService} from "@costlydeveloper/ngx-awesome-popup";

@Component({
  selector: 'app-all-missions',
  templateUrl: './all-missions.component.html',
  styleUrls: ['./all-missions.component.scss']
})
export class AllMissionsComponent  implements OnInit{

  //for filter
  filterForm!: FormGroup;
  isLoading!: boolean;
  missions:Array<MissionDto>=[]
  drivers:Array<ChauffeurDto>=[]
  allMissionsResponse: PageResponseMissionDto={};
  page: number = 0;
  size: number = 5;

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
    this.isLoading=true;
    this.missionService.findAllMissions({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(ms)=>{
        console.log(ms);
        this.allMissionsResponse=ms;
        this.isLoading=false;
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
    this.confirmBoxService.customFour('Are you sure you want to delete this mission?', 'This action is irreversible!', 'Confirme', 'Cancel').subscribe(
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
  position: number = 1;
  handlePageChange(event: number) {
    this.page  = event
  }
  selectPage(page: string) {
    this.page = parseInt(page, 10) || 1;

  }
  private innitForm() {
    this.filterForm = new FormGroup({
      filter: new FormControl(5)
    });
  }



  goToFirstPage() {
    this.page=0;
    this.getAllMission();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllMission();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllMission();
  }

  goToNextPage() {
    this.page++;
    this.getAllMission();
  }


  get  isLastPage():boolean{
    return this.page == this.allMissionsResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allMissionsResponse.totalPages as number -1;
  }

  goToNewMission() {
    this.router.navigate(['missons','new-mission"'])
  }
}
