/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { delete6 } from '../fn/entreprise-controller/delete-6';
import { Delete6$Params } from '../fn/entreprise-controller/delete-6';
import { EntrepriseDto } from '../models/entreprise-dto';
import { findAll6 } from '../fn/entreprise-controller/find-all-6';
import { FindAll6$Params } from '../fn/entreprise-controller/find-all-6';
import { findById6 } from '../fn/entreprise-controller/find-by-id-6';
import { FindById6$Params } from '../fn/entreprise-controller/find-by-id-6';
import { findByRaisonSocial } from '../fn/entreprise-controller/find-by-raison-social';
import { FindByRaisonSocial$Params } from '../fn/entreprise-controller/find-by-raison-social';
import { save6 } from '../fn/entreprise-controller/save-6';
import { Save6$Params } from '../fn/entreprise-controller/save-6';

@Injectable({ providedIn: 'root' })
export class EntrepriseControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `save6()` */
  static readonly Save6Path = '/entreprises/create';

  /**
   * Ajouter/Modifier une entreprise, cette methode permet d'enregistrer ou modifier une entreprise
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `save6()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save6$Response(params: Save6$Params, context?: HttpContext): Observable<StrictHttpResponse<EntrepriseDto>> {
    return save6(this.http, this.rootUrl, params, context);
  }

  /**
   * Ajouter/Modifier une entreprise, cette methode permet d'enregistrer ou modifier une entreprise
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `save6$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save6(params: Save6$Params, context?: HttpContext): Observable<EntrepriseDto> {
    return this.save6$Response(params, context).pipe(
      map((r: StrictHttpResponse<EntrepriseDto>): EntrepriseDto => r.body)
    );
  }

  /** Path part for operation `findByRaisonSocial()` */
  static readonly FindByRaisonSocialPath = '/entreprises/entreprise/{rsocial}';

  /**
   * Rechercher une Entreprise par nom! Cette methode permet de chercher une entreprise par son nom
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByRaisonSocial()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByRaisonSocial$Response(params: FindByRaisonSocial$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findByRaisonSocial(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher une Entreprise par nom! Cette methode permet de chercher une entreprise par son nom
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByRaisonSocial$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByRaisonSocial(params: FindByRaisonSocial$Params, context?: HttpContext): Observable<any> {
    return this.findByRaisonSocial$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findById6()` */
  static readonly FindById6Path = '/entreprises/entreprise/{id}';

  /**
   * Rechercher une Entreprise par ID! Cette methode permet de chercher une entreprise par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findById6()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById6$Response(params: FindById6$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findById6(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher une Entreprise par ID! Cette methode permet de chercher une entreprise par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findById6$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById6(params: FindById6$Params, context?: HttpContext): Observable<any> {
    return this.findById6$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAll6()` */
  static readonly FindAll6Path = '/entreprises/all';

  /**
   * Rechercher tout les Entreprise! Cette methode permet de retouner tous les entreprises
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAll6()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll6$Response(params?: FindAll6$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAll6(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher tout les Entreprise! Cette methode permet de retouner tous les entreprises
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAll6$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll6(params?: FindAll6$Params, context?: HttpContext): Observable<any> {
    return this.findAll6$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `delete6()` */
  static readonly Delete6Path = '/entreprises/delete/{id}';

  /**
   * Supprimer une Entreprise par ID! Cette methode permet de suprimmer une entreprise par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete6()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete6$Response(params: Delete6$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return delete6(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer une Entreprise par ID! Cette methode permet de suprimmer une entreprise par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete6$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete6(params: Delete6$Params, context?: HttpContext): Observable<any> {
    return this.delete6$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

}
