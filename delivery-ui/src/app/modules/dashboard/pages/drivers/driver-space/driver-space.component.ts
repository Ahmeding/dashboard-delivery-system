import {Component, OnInit} from '@angular/core';
import {PageResponseMissionDto} from "../../../../../../ds-api/src/models/page-response-mission-dto";
import {MissionService} from "../../../../../../ds-api/src/services/mission.service";

@Component({
  selector: 'app-driver-space',
  templateUrl: './driver-space.component.html',
  styleUrls: ['./driver-space.component.scss']
})
export class DriverSpaceComponent implements OnInit{

  page: number=0;
  size: number=10
  allMyMissionsResponse: PageResponseMissionDto={};
  isLoading: boolean = false;

  constructor(
    private missionsServices:MissionService,

  ) {
  }

  ngOnInit(): void {
    this.getAllMyMissions();
  }
  private getAllMyMissions() {
      this.missionsServices.findAllMissionsByDriverId({
        page:0
        ,size:10
      }).subscribe({
        next:(myMissions)=>{
          this.allMyMissionsResponse=myMissions;
        },error:(err)=>{
          console.log(err);
        }
      });
  }
  missionDetails(param: any) {

  }

  missionDelete(param: any) {

  }

  misionEdit(id: any) {

  }


  goToFirstPage() {
    this.page=0;
    this.getAllMyMissions();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllMyMissions();
  }

  goToNextPage() {
    this.page++;
    this.getAllMyMissions();
  }

  goToLastPage() {
    this.page=this.allMyMissionsResponse.totalPages as number -1;
    this.getAllMyMissions();
  }

  goToPage(index: number) {
    this.page = index;
    this.getAllMyMissions();
  }

  get isLastPage():boolean{
    return this.page == this.allMyMissionsResponse.totalPages as number -1;
  }

}
