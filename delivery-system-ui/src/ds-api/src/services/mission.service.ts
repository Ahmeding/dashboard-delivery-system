/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { delete4 } from '../fn/mission/delete-4';
import { Delete4$Params } from '../fn/mission/delete-4';
import { findAll4 } from '../fn/mission/find-all-4';
import { FindAll4$Params } from '../fn/mission/find-all-4';
import { findAllByDate } from '../fn/mission/find-all-by-date';
import { FindAllByDate$Params } from '../fn/mission/find-all-by-date';
import { findById4 } from '../fn/mission/find-by-id-4';
import { FindById4$Params } from '../fn/mission/find-by-id-4';
import { MissionDto } from '../models/mission-dto';
import { save4 } from '../fn/mission/save-4';
import { Save4$Params } from '../fn/mission/save-4';
import { updateEtatMission } from '../fn/mission/update-etat-mission';
import { UpdateEtatMission$Params } from '../fn/mission/update-etat-mission';
import { updateKilometrage } from '../fn/mission/update-kilometrage';
import { UpdateKilometrage$Params } from '../fn/mission/update-kilometrage';

@Injectable({ providedIn: 'root' })
export class MissionService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `save4()` */
  static readonly Save4Path = '/missions/create';

  /**
   * Ajouter/Modifier une mission, cette methode permet d'enregistrer ou modifier une mission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `save4()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save4$Response(params: Save4$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return save4(this.http, this.rootUrl, params, context);
  }

  /**
   * Ajouter/Modifier une mission, cette methode permet d'enregistrer ou modifier une mission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `save4$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save4(params: Save4$Params, context?: HttpContext): Observable<any> {
    return this.save4$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `updateEtatMission()` */
  static readonly UpdateEtatMissionPath = '/missions/update/{id}/{etatMission}';

  /**
   * Modifier l'etat d'une mission, cette methode permet modifier l'etat d' une mission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateEtatMission()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateEtatMission$Response(params: UpdateEtatMission$Params, context?: HttpContext): Observable<StrictHttpResponse<MissionDto>> {
    return updateEtatMission(this.http, this.rootUrl, params, context);
  }

  /**
   * Modifier l'etat d'une mission, cette methode permet modifier l'etat d' une mission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateEtatMission$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateEtatMission(params: UpdateEtatMission$Params, context?: HttpContext): Observable<MissionDto> {
    return this.updateEtatMission$Response(params, context).pipe(
      map((r: StrictHttpResponse<MissionDto>): MissionDto => r.body)
    );
  }

  /** Path part for operation `updateKilometrage()` */
  static readonly UpdateKilometragePath = '/missions/update/{flag}/{id}/{kilometrage}';

  /**
   * Mettre à jour le kilomtratge debut/fin d'une mission, cette methode permet Mettre à jour le kilomtratge debut/fin d' une mission
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateKilometrage()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateKilometrage$Response(params: UpdateKilometrage$Params, context?: HttpContext): Observable<StrictHttpResponse<MissionDto>> {
    return updateKilometrage(this.http, this.rootUrl, params, context);
  }

  /**
   * Mettre à jour le kilomtratge debut/fin d'une mission, cette methode permet Mettre à jour le kilomtratge debut/fin d' une mission
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateKilometrage$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  updateKilometrage(params: UpdateKilometrage$Params, context?: HttpContext): Observable<MissionDto> {
    return this.updateKilometrage$Response(params, context).pipe(
      map((r: StrictHttpResponse<MissionDto>): MissionDto => r.body)
    );
  }

  /** Path part for operation `findById4()` */
  static readonly FindById4Path = '/missions/mission/{id}';

  /**
   * Supprimer une Mission par ID! Cette methode permet de suprimmer une mission par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findById4()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById4$Response(params: FindById4$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findById4(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer une Mission par ID! Cette methode permet de suprimmer une mission par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findById4$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById4(params: FindById4$Params, context?: HttpContext): Observable<any> {
    return this.findById4$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAllByDate()` */
  static readonly FindAllByDatePath = '/missions/date/{dateMission}';

  /**
   * Rechercher une Mission par date! Cette methode permet de chercher une mission par une date
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllByDate()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllByDate$Response(params: FindAllByDate$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAllByDate(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher une Mission par date! Cette methode permet de chercher une mission par une date
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllByDate$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllByDate(params: FindAllByDate$Params, context?: HttpContext): Observable<any> {
    return this.findAllByDate$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAll4()` */
  static readonly FindAll4Path = '/missions/all';

  /**
   * Rechercher tout les Missions! Cette methode permet de retouner toutes les missions
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAll4()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll4$Response(params?: FindAll4$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAll4(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher tout les Missions! Cette methode permet de retouner toutes les missions
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAll4$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll4(params?: FindAll4$Params, context?: HttpContext): Observable<any> {
    return this.findAll4$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `delete4()` */
  static readonly Delete4Path = '/missions/delete/{id}';

  /**
   * Supprimer une Mission par ID! Cette methode permet de suprimmer une mission par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete4()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete4$Response(params: Delete4$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return delete4(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer une Mission par ID! Cette methode permet de suprimmer une mission par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete4$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete4(params: Delete4$Params, context?: HttpContext): Observable<any> {
    return this.delete4$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

}
