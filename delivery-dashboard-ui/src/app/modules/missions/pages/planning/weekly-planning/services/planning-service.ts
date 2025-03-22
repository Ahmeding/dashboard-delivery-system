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
        destinationColor: getDestinationColor(mission.deliveryDestinationResponse?.serviceDelivery?.destinationCity!)
      })))
    );
  }

  private getMockMissions(): MissionResponse[] {
    return [
      {
        id: 1,
        titre: 'Delivery to Client A',
        dateDebut:  "[2024,12,09,10,30]",
        dateFin: '2024-12-09T10:30',
        etatMission: 'PLANNED',
        deliveryDestinationResponse: {serviceDelivery:{destinationCity:"Paris"}},
        missionVehiclesDtos: [
          { id: 1,chauffeursVehicleDto:[{missionVehicleId:1,chauffeurDto:{
            firstname:"john"
              }}] ,vehicleDto: {immatriculation:'ABC123', vehicleType: 'VAN',} },
        ],
      },

      // Add more mock missions...
    ];
  }
}
