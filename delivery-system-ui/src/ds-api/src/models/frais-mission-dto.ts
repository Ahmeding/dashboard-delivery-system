/* tslint:disable */
/* eslint-disable */
import { LigneFraisMissionDto } from '../models/ligne-frais-mission-dto';
import { MissionDto } from '../models/mission-dto';
export interface FraisMissionDto {
  commentaire?: string;
  id?: number;
  lignesFraisMissionDto?: Array<LigneFraisMissionDto>;
  missionDto?: MissionDto;
  nom?: string;
}
