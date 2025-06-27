import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MissionCard } from '../models/mission.model';
import { getDestinationColor } from '../utils/planning.utils';
import {MissionResponse} from "../../../../../../../ds-api/src/models/mission-response";

@Injectable({
  providedIn: 'root'
})
export class PlanningService {
  private apiUrl = 'your-api-endpoint';

  constructor(private http: HttpClient) {}

  getMissionsByWeek(weekStart: Date): Observable<MissionResponse[]> {
    // Replace this with actual API call
    return of(this.getMockMissions()).pipe(
      map(missions => missions.map(mission => ({
        ...mission,
        destinationColor: getDestinationColor(mission.deliveryDestination?.deliveryService?.destinationCity!)
      })))
    );
  }

  private getMockMissions(): MissionResponse[] {
    return [
    /*  {
        id: 1,
        title: 'Delivery to Client A',
        startDate:  "[2024,12,09,10,30]",
        endDate: '2024-12-09T10:30',
        missionStatus: 'PLANNED',
        deliveryDestination: {deliveryService:{destinationCity:"Paris"}},
        missionVehicles: [
          { id: 1,driverVehicles:[{missionVehicleId:1,driverId:1}] ,
            vehicleId: {immatriculation:'ABC123', vehicleType: 'VAN',} },
        ],
      },*/

      // Add more mock missions...
    ];
  }
}
