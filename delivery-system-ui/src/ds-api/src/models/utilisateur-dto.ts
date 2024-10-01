/* tslint:disable */
/* eslint-disable */
import { AdresseDto } from '../models/adresse-dto';
export interface UtilisateurDto {
  adresseDto?: AdresseDto;
  email?: string;
  id?: number;
  motDePasse?: string;
  nom?: string;
  numTel?: string;
  prenom?: string;
  role?: 'ADMIN' | 'USER';
}
