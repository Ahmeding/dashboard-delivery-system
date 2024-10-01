/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { ChauffeurDto } from '../models/chauffeur-dto';
import { delete1 } from '../fn/utilisateur/delete-1';
import { Delete1$Params } from '../fn/utilisateur/delete-1';
import { findAll1 } from '../fn/utilisateur/find-all-1';
import { FindAll1$Params } from '../fn/utilisateur/find-all-1';
import { findByEmail } from '../fn/utilisateur/find-by-email';
import { FindByEmail$Params } from '../fn/utilisateur/find-by-email';
import { findById1 } from '../fn/utilisateur/find-by-id-1';
import { FindById1$Params } from '../fn/utilisateur/find-by-id-1';
import { save1 } from '../fn/utilisateur/save-1';
import { Save1$Params } from '../fn/utilisateur/save-1';

@Injectable({ providedIn: 'root' })
export class UtilisateurService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `save1()` */
  static readonly Save1Path = '/utilisateurs/create';

  /**
   * Ajouter/Modifier un utilisateur, cette methode permet d'enregistrer ou modifier un utilisateur
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `save1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save1$Response(params: Save1$Params, context?: HttpContext): Observable<StrictHttpResponse<ChauffeurDto>> {
    return save1(this.http, this.rootUrl, params, context);
  }

  /**
   * Ajouter/Modifier un utilisateur, cette methode permet d'enregistrer ou modifier un utilisateur
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `save1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  save1(params: Save1$Params, context?: HttpContext): Observable<ChauffeurDto> {
    return this.save1$Response(params, context).pipe(
      map((r: StrictHttpResponse<ChauffeurDto>): ChauffeurDto => r.body)
    );
  }

  /** Path part for operation `findById1()` */
  static readonly FindById1Path = '/utilisateurs/findbyid/{id}';

  /**
   * Rechercher un Utilisateur par ID! Cette methode permet de chercher un utilisateur par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findById1()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById1$Response(params: FindById1$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findById1(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher un Utilisateur par ID! Cette methode permet de chercher un utilisateur par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findById1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findById1(params: FindById1$Params, context?: HttpContext): Observable<any> {
    return this.findById1$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findByEmail()` */
  static readonly FindByEmailPath = '/utilisateurs/findbyemail/{email}';

  /**
   * Rechercher un Utilisateur par son email ! Cette methode permet de chercher un utilisateur par son email
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByEmail()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByEmail$Response(params: FindByEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findByEmail(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher un Utilisateur par son email ! Cette methode permet de chercher un utilisateur par son email
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByEmail$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByEmail(params: FindByEmail$Params, context?: HttpContext): Observable<any> {
    return this.findByEmail$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `findAll1()` */
  static readonly FindAll1Path = '/utilisateurs/all';

  /**
   * Rechercher tout les Utilisateur! Cette methode permet de retouner tous les chauffeurs
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAll1()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll1$Response(params?: FindAll1$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return findAll1(this.http, this.rootUrl, params, context);
  }

  /**
   * Rechercher tout les Utilisateur! Cette methode permet de retouner tous les chauffeurs
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAll1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAll1(params?: FindAll1$Params, context?: HttpContext): Observable<any> {
    return this.findAll1$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

  /** Path part for operation `delete1()` */
  static readonly Delete1Path = '/utilisateurs/delete/{id}';

  /**
   * Supprimer un Utilisateur par ID! Cette methode permet de suprimmer un utilisateur par son ID
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete1()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1$Response(params: Delete1$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
    return delete1(this.http, this.rootUrl, params, context);
  }

  /**
   * Supprimer un Utilisateur par ID! Cette methode permet de suprimmer un utilisateur par son ID
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1(params: Delete1$Params, context?: HttpContext): Observable<any> {
    return this.delete1$Response(params, context).pipe(
      map((r: StrictHttpResponse<any>): any => r.body)
    );
  }

}
