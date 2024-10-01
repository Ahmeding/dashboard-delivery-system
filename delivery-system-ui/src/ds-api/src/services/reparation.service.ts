/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { delete2 } from '../fn/reparation/delete-2';
import { Delete2$Params } from '../fn/reparation/delete-2';
import { findAll2 } from '../fn/reparation/find-all-2';
import { FindAll2$Params } from '../fn/reparation/find-all-2';
import { findByDate } from '../fn/reparation/find-by-date';
import { FindByDate$Params } from '../fn/reparation/find-by-date';
import { findById2 } from '../fn/reparation/find-by-id-2';
import { FindById2$Params } from '../fn/reparation/find-by-id-2';
import { ReparationDto } from '../models/reparation-dto';
import { save2 } from '../fn/reparation/save-2';
import { Save2$Params } from '../fn/reparation/save-2';

@Injectable({ providedIn: 'root' })
export class ReparationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `save2()` */
  static readonly Save2Path = '/reparations/create';

  /**
   * Ajouter/Modifier une reparation, cette methode permet d'enregistrer ou modifier une reparattion
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `save2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save2$Response(params: Save2$Params, context?: HttpContext): Observable<StrictHttpResponse<ReparationDto>> {
    return save2(this.http, this.rootUrl, params, context);
  }

  /**
   * Ajouter/Modifier une reparation, cette methode permet d'enregistrer ou modifier une reparattion
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `save2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save2(params: Save2$Params, context?: HttpContext): Observable<ReparationDto> {
    return this.save2$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReparationDto>): ReparationDto => r.body)
    );
  }

  /** Path part for operation `findById2()` */
  static readonly FindById2Path = '/reparations/reparation/{id}';

  /**
   * Rechercher une Reparation par ID! Cette methode permet de chercher une reparation par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findById2()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById2$Response(params: FindById2$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findById2(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher une Reparation par ID! Cette methode permet de chercher une reparation par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findById2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById2(params: FindById2$Params, context?: HttpContext): Observable<any> {
    return this.findById2$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findByDate()` */
  static readonly FindByDatePath = '/reparations/date/{dateRep}';

  /**
   * Rechercher une Reparation par DATE ! Cette methode permet de chercher une reparation par date
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByDate()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByDate$Response(params: FindByDate$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findByDate(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher une Reparation par DATE ! Cette methode permet de chercher une reparation par date
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByDate$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByDate(params: FindByDate$Params, context?: HttpContext): Observable<any> {
    return this.findByDate$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAll2()` */
  static readonly FindAll2Path = '/reparations/all';

  /**
   * Rechercher tout les Reparation! Cette methode permet de retouner toutes les reparations
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAll2()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll2$Response(params?: FindAll2$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAll2(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher tout les Reparation! Cette methode permet de retouner toutes les reparations
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAll2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll2(params?: FindAll2$Params, context?: HttpContext): Observable<any> {
    return this.findAll2$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `delete2()` */
  static readonly Delete2Path = '/reparations/delete/{id}';

  /**
   * Supprimer une Reparation par ID! Cette methode permet de suprimmer une reparation par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete2()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2$Response(params: Delete2$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return delete2(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer une Reparation par ID! Cette methode permet de suprimmer une reparation par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2(params: Delete2$Params, context?: HttpContext): Observable<any> {
    return this.delete2$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

}
