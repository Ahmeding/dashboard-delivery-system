/* tslint:disable */
/* eslint-disable */
import { AdresseDto } from '../models/adresse-dto';
import { CommandeClientDto } from '../models/commande-client-dto';
import { DevisDto } from '../models/devis-dto';
export interface ClientDto {
  adresseDto?: AdresseDto;
  adresseEmail?: string;
  commandeClientsDtos?: Array<CommandeClientDto>;
  description?: string;
  devisDtos?: Array<DevisDto>;
  id?: number;
  nom?: string;
  numTel?: string;
  prenom?: string;
  siren?: string;
  siret?: string;
}
