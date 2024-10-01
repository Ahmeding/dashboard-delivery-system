/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { delete5 } from '../fn/frais-de-mission/delete-5';
import { Delete5$Params } from '../fn/frais-de-mission/delete-5';
import { deleteLigneFraisMission } from '../fn/frais-de-mission/delete-ligne-frais-mission';
import { DeleteLigneFraisMission$Params } from '../fn/frais-de-mission/delete-ligne-frais-mission';
import { findAll5 } from '../fn/frais-de-mission/find-all-5';
import { FindAll5$Params } from '../fn/frais-de-mission/find-all-5';
import { findAllLignesFraisMissionByFraisMissionId } from '../fn/frais-de-mission/find-all-lignes-frais-mission-by-frais-mission-id';
import { FindAllLignesFraisMissionByFraisMissionId$Params } from '../fn/frais-de-mission/find-all-lignes-frais-mission-by-frais-mission-id';
import { findById5 } from '../fn/frais-de-mission/find-by-id-5';
import { FindById5$Params } from '../fn/frais-de-mission/find-by-id-5';
import { findByIdMission } from '../fn/frais-de-mission/find-by-id-mission';
import { FindByIdMission$Params } from '../fn/frais-de-mission/find-by-id-mission';
import { FraisMissionDto } from '../models/frais-mission-dto';
import { save5 } from '../fn/frais-de-mission/save-5';
import { Save5$Params } from '../fn/frais-de-mission/save-5';
import { updateFraisMission } from '../fn/frais-de-mission/update-frais-mission';
import { UpdateFraisMission$Params } from '../fn/frais-de-mission/update-frais-mission';
import { updateQuantite } from '../fn/frais-de-mission/update-quantite';
import { UpdateQuantite$Params } from '../fn/frais-de-mission/update-quantite';

@Injectable({ providedIn: 'root' })
export class FraisDeMissionService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `save5()` */
  static readonly Save5Path = '/fraismissions/create';

  /**
   * Ajouter/Modifier un fraisMission, cette methode permet d'enregistrer ou modifier un fraisMission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `save5()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save5$Response(params: Save5$Params, context?: HttpContext): Observable<StrictHttpResponse<FraisMissionDto>> {
    return save5(this.http, this.rootUrl, params, context);
  }

  /**
   * Ajouter/Modifier un fraisMission, cette methode permet d'enregistrer ou modifier un fraisMission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `save5$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save5(params: Save5$Params, context?: HttpContext): Observable<FraisMissionDto> {
    return this.save5$Response(params, context).pipe(
      map((r: StrictHttpResponse<FraisMissionDto>): FraisMissionDto => r.body)
    );
  }

  /** Path part for operation `updateFraisMission()` */
  static readonly UpdateFraisMissionPath = '/fraismissions/update';

  /**
   * Modifier un fraisMission, cette methode permet de modifier un fraisMission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateFraisMission()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateFraisMission$Response(params: UpdateFraisMission$Params, context?: HttpContext): Observable<StrictHttpResponse<FraisMissionDto>> {
    return updateFraisMission(this.http, this.rootUrl, params, context);
  }

  /**
   * Modifier un fraisMission, cette methode permet de modifier un fraisMission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateFraisMission$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateFraisMission(params: UpdateFraisMission$Params, context?: HttpContext): Observable<FraisMissionDto> {
    return this.updateFraisMission$Response(params, context).pipe(
      map((r: StrictHttpResponse<FraisMissionDto>): FraisMissionDto => r.body)
    );
  }

  /** Path part for operation `updateQuantite()` */
  static readonly UpdateQuantitePath = '/fraismissions/update/{idFM}/{idLigneFM}/{qte}';

  /**
   * Modifier la qte dans une ligne fraisMission, cette methode permet de la qte modifier dans une ligne fraisMission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateQuantite()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateQuantite$Response(params: UpdateQuantite$Params, context?: HttpContext): Observable<StrictHttpResponse<FraisMissionDto>> {
    return updateQuantite(this.http, this.rootUrl, params, context);
  }

  /**
   * Modifier la qte dans une ligne fraisMission, cette methode permet de la qte modifier dans une ligne fraisMission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateQuantite$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateQuantite(params: UpdateQuantite$Params, context?: HttpContext): Observable<FraisMissionDto> {
    return this.updateQuantite$Response(params, context).pipe(
      map((r: StrictHttpResponse<FraisMissionDto>): FraisMissionDto => r.body)
    );
  }

  /** Path part for operation `findByIdMission()` */
  static readonly FindByIdMissionPath = '/fraismissions/mission/{idMission}';

  /**
   * Rechercher un FraisMission par l'ID de mission! Cette methode permet de chercher un fraisMission par l'ID de mission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByIdMission()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByIdMission$Response(params: FindByIdMission$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findByIdMission(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher un FraisMission par l'ID de mission! Cette methode permet de chercher un fraisMission par l'ID de mission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByIdMission$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByIdMission(params: FindByIdMission$Params, context?: HttpContext): Observable<any> {
    return this.findByIdMission$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAllLignesFraisMissionByFraisMissionId()` */
  static readonly FindAllLignesFraisMissionByFraisMissionIdPath = '/fraismissions/fraismission/{idFM}';

  /**
   * Rechercher les lignes du frais de mission par l'ID de mission! Cette methode permet de chercher tous les lignes du fraisMission par l'ID de mission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllLignesFraisMissionByFraisMissionId()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllLignesFraisMissionByFraisMissionId$Response(params: FindAllLignesFraisMissionByFraisMissionId$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAllLignesFraisMissionByFraisMissionId(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher les lignes du frais de mission par l'ID de mission! Cette methode permet de chercher tous les lignes du fraisMission par l'ID de mission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllLignesFraisMissionByFraisMissionId$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllLignesFraisMissionByFraisMissionId(params: FindAllLignesFraisMissionByFraisMissionId$Params, context?: HttpContext): Observable<any> {
    return this.findAllLignesFraisMissionByFraisMissionId$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findById5()` */
  static readonly FindById5Path = '/fraismissions/fraisMission/{id}';

  /**
   * Rechercher un FraisMission par ID! Cette methode permet de chercher un fraisMission par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findById5()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById5$Response(params: FindById5$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findById5(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher un FraisMission par ID! Cette methode permet de chercher un fraisMission par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findById5$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById5(params: FindById5$Params, context?: HttpContext): Observable<any> {
    return this.findById5$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAll5()` */
  static readonly FindAll5Path = '/fraismissions/all';

  /**
   * Rechercher tout les FraisMission! Cette methode permet de retouner tous les fraisMissions
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAll5()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll5$Response(params?: FindAll5$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAll5(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher tout les FraisMission! Cette methode permet de retouner tous les fraisMissions
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAll5$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll5(params?: FindAll5$Params, context?: HttpContext): Observable<any> {
    return this.findAll5$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `delete5()` */
  static readonly Delete5Path = '/fraismissions/delete/{id}';

  /**
   * Supprimer un FraisMission par ID! Cette methode permet de suprimmer un fraisMission par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete5()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete5$Response(params: Delete5$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return delete5(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer un FraisMission par ID! Cette methode permet de suprimmer un fraisMission par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete5$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete5(params: Delete5$Params, context?: HttpContext): Observable<any> {
    return this.delete5$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `deleteLigneFraisMission()` */
  static readonly DeleteLigneFraisMissionPath = '/fraismissions/delete/{idFM}/{idLigneFM}';

  /**
   * Supprimer une ligne du fraisMission, cette methode permet de supprimer une ligne  du fraisMission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteLigneFraisMission()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteLigneFraisMission$Response(params: DeleteLigneFraisMission$Params, context?: HttpContext): Observable<StrictHttpResponse<FraisMissionDto>> {
    return deleteLigneFraisMission(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer une ligne du fraisMission, cette methode permet de supprimer une ligne  du fraisMission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteLigneFraisMission$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteLigneFraisMission(params: DeleteLigneFraisMission$Params, context?: HttpContext): Observable<FraisMissionDto> {
    return this.deleteLigneFraisMission$Response(params, context).pipe(
      map((r: StrictHttpResponse<FraisMissionDto>): FraisMissionDto => r.body)
    );
  }

}
