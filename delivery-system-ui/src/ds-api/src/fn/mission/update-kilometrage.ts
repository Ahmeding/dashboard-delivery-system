/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { MissionDto } from '../../models/mission-dto';

export interface UpdateKilometrage$Params {
  flag: string;
  id: number;
  kilometrage: number;
}

export function updateKilometrage(http: HttpClient, rootUrl: string, params: UpdateKilometrage$Params, context?: HttpContext): Observable<StrictHttpResponse<MissionDto>> {
  const rb = new RequestBuilder(rootUrl, updateKilometrage.PATH, 'patch');
  if (params) {
    rb.path('flag', params.flag, {});
    rb.path('id', params.id, {});
    rb.path('kilometrage', params.kilometrage, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<MissionDto>;
    })
  );
}

updateKilometrage.PATH = '/missions/update/{flag}/{id}/{kilometrage}';
