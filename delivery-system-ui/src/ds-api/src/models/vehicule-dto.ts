/* tslint:disable */
/* eslint-disable */
export interface VehiculeDto {
  dateExpCtrlTec?: string;
  datePMC?: string;
  id?: number;
  immatriculation?: string;
  kilometrage?: number;
  marque?: string;
  modele?: string;
  numVin?: string;
  typecarburant?: 'GASOIL' | 'ESSENCE' | 'ELECTRIC' | 'HYBRIDE';
  volume?: number;
}
