/* tslint:disable */
/* eslint-disable */
import { ReparationDto } from '../models/reparation-dto';
export interface PieceRechangeDto {
  id?: number;
  nom?: string;
  prix?: number;
  reference?: string;
  reparationDto?: ReparationDto;
}
