/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { isAdmin } from '../fn/admin-controller/is-admin';
import { IsAdmin$Params } from '../fn/admin-controller/is-admin';

@Injectable({ providedIn: 'root' })
export class AdminControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `isAdmin()` */
  static readonly IsAdminPath = '/api/v1/admin/isadmin';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `isAdmin()` instead.
   *
   * This method doesn't expect any request body.
   */
  isAdmin$Response(params?: IsAdmin$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return isAdmin(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `isAdmin$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  isAdmin(params?: IsAdmin$Params, context?: HttpContext): Observable<string> {
    return this.isAdmin$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
