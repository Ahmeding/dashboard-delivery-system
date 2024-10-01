/* tslint:disable */
/* eslint-disable */
import { VehiculeDto } from '../models/vehicule-dto';
export interface ReparationDto {
  commentaire?: string;
  cout?: number;
  dateDebut?: string;
  dateFin?: string;
  dateProchainRep?: string;
  id?: number;
  type?: 'MECANIQUE' | 'ELECTRIQUE' | 'CARROSSERIE' | 'VIDANGE_COMPLET';
  vehiculeDto?: VehiculeDto;
}
