/* tslint:disable */
/* eslint-disable */
import { ChauffeurDto } from '../models/chauffeur-dto';
export interface ContratDto {
  chauffeurDto?: ChauffeurDto;
  dateDebut?: string;
  dateFin?: string;
  id?: number;
  typeContrat?: 'CDD' | 'CDI';
}
