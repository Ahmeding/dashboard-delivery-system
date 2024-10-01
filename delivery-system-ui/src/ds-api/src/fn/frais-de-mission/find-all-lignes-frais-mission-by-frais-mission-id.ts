/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface FindAllLignesFraisMissionByFraisMissionId$Params {
  idFM: number;
}

export function findAllLignesFraisMissionByFraisMissionId(http: HttpClient, rootUrl: string, params: FindAllLignesFraisMissionByFraisMissionId$Params, context?: HttpContext): Observable<StrictHttpResponse<any>> {
  const rb = new RequestBuilder(rootUrl, findAllLignesFraisMissionByFraisMissionId.PATH, 'get');
  if (params) {
    rb.path('idFM', params.idFM, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<any>;
    })
  );
}

findAllLignesFraisMissionByFraisMissionId.PATH = '/fraismissions/fraismission/{idFM}';
