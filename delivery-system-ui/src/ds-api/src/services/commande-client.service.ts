/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { CommandeClientDto } from '../models/commande-client-dto';
import { delete9 } from '../fn/commande-client/delete-9';
import { Delete9$Params } from '../fn/commande-client/delete-9';
import { findAll9 } from '../fn/commande-client/find-all-9';
import { FindAll9$Params } from '../fn/commande-client/find-all-9';
import { findByCode1 } from '../fn/commande-client/find-by-code-1';
import { FindByCode1$Params } from '../fn/commande-client/find-by-code-1';
import { findById9 } from '../fn/commande-client/find-by-id-9';
import { FindById9$Params } from '../fn/commande-client/find-by-id-9';
import { save9 } from '../fn/commande-client/save-9';
import { Save9$Params } from '../fn/commande-client/save-9';

@Injectable({ providedIn: 'root' })
export class CommandeClientService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `save9()` */
  static readonly Save9Path = '/cmdclients/create';

  /**
   * Ajouter/Modifier un commande client , cette methode permet d'enregistrer ou modifier un chauffreur
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `save9()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save9$Response(params: Save9$Params, context?: HttpContext): Observable<StrictHttpResponse<CommandeClientDto>> {
    return save9(this.http, this.rootUrl, params, context);
  }

  /**
   * Ajouter/Modifier un commande client , cette methode permet d'enregistrer ou modifier un chauffreur
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `save9$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save9(params: Save9$Params, context?: HttpContext): Observable<CommandeClientDto> {
    return this.save9$Response(params, context).pipe(
      map((r: StrictHttpResponse<CommandeClientDto>): CommandeClientDto => r.body)
    );
  }

  /** Path part for operation `findById9()` */
  static readonly FindById9Path = '/cmdclients/cmdclient/{id}';

  /**
   * Rechercher un CommandeClient  par ID! Cette methode permet de chercher un commande client  par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findById9()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById9$Response(params: FindById9$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findById9(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher un CommandeClient  par ID! Cette methode permet de chercher un commande client  par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findById9$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById9(params: FindById9$Params, context?: HttpContext): Observable<any> {
    return this.findById9$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findByCode1()` */
  static readonly FindByCode1Path = '/cmdclients/cmdclient/{code}';

  /**
   * Rechercher un Commande Client  par CODE ! Cette methode permet de chercher une commande client  par son code
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByCode1()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByCode1$Response(params: FindByCode1$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findByCode1(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher un Commande Client  par CODE ! Cette methode permet de chercher une commande client  par son code
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByCode1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByCode1(params: FindByCode1$Params, context?: HttpContext): Observable<any> {
    return this.findByCode1$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAll9()` */
  static readonly FindAll9Path = '/cmdclients/all';

  /**
   * Rechercher tout les CommandeClient ! Cette methode permet de retouner toutes les commandes clients
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAll9()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll9$Response(params?: FindAll9$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAll9(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher tout les CommandeClient ! Cette methode permet de retouner toutes les commandes clients
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAll9$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll9(params?: FindAll9$Params, context?: HttpContext): Observable<any> {
    return this.findAll9$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `delete9()` */
  static readonly Delete9Path = '/cmdclients/delete/{id}';

  /**
   * Supprimer un CommandeClient  par ID! Cette methode permet de suprimmer un commande client  par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete9()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete9$Response(params: Delete9$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return delete9(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer un CommandeClient  par ID! Cette methode permet de suprimmer un commande client  par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete9$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete9(params: Delete9$Params, context?: HttpContext): Observable<any> {
    return this.delete9$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

}
