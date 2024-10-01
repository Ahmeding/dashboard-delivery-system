/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { FraisMissionDto } from '../../models/frais-mission-dto';

export interface Save5$Params {
      body: FraisMissionDto
}

export function save5(http: HttpClient, rootUrl: string, params: Save5$Params, context?: HttpContext): Observable<StrictHttpResponse<FraisMissionDto>> {
  const rb = new RequestBuilder(rootUrl, save5.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<FraisMissionDto>;
    })
  );
}

save5.PATH = '/fraismissions/create';
