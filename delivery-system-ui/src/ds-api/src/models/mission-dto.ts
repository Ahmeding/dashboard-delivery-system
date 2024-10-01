/* tslint:disable */
/* eslint-disable */
import { EtapeDto } from '../models/etape-dto';
import { MissionChauffeurDto } from '../models/mission-chauffeur-dto';
import { MissionVehicleDto } from '../models/mission-vehicle-dto';
export interface MissionDto {
  commentaire?: string;
  dateDebut?: string;
  dateFin?: string;
  etapeDtos?: Array<EtapeDto>;
  etatMission?: 'EN_COURS' | 'ACCOMPLI' | 'ANNULEE' | 'TERMINIEE';
  fraisChauffeur?: number;
  id?: number;
  kmDebut?: number;
  kmFin?: number;
  missionAccomplished?: boolean;
  missionChauffeurDtos?: Array<MissionChauffeurDto>;
  missionVehiclesDtos?: Array<MissionVehicleDto>;
  prixHt?: number;
  prixTTC?: number;
  titre?: string;
  tva?: number;
  volume?: number;
}
