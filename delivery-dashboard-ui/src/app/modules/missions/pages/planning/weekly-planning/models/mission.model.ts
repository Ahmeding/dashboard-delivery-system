import {MissionResponse} from "../../../../../../../ds-api/src/models/mission-response";

export interface Vehicle {
  id: number;
  plateNumber: string;
  type: string;
}

export interface Driver {
  id: number;
  name: string;
  avatar?: string;
}

export interface MissionCard {
  id: number;
  title: string;
  startTime: string;
  endTime: string;
  status: 'EN_COURS' | 'ACCOMPLI' | 'ANNULEE' | 'TERMINIEE';
  destination: string;
  destinationColor: string;
  vehicles: Vehicle[];
  drivers: Driver[];
}

export interface DayPlan {
  date: Date;
  missions: MissionResponse[]/*MissionCard[]*/;
}
