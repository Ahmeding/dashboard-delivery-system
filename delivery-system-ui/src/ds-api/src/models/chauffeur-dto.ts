/* tslint:disable */
/* eslint-disable */
import { AdresseDto } from '../models/adresse-dto';
export interface ChauffeurDto {
  adresseDto?: AdresseDto;
  adresseEmail?: string;
  dateNaissance?: string;
  id?: number;
  motDePasse?: string;
  nom?: string;
  numSecu?: string;
  numTel?: string;
  prenom?: string;
  rib?: string;
  typePermi?: string;
}
