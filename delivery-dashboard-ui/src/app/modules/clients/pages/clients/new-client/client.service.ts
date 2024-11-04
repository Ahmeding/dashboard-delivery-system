import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ClientDto} from "../../../../../../ds-api/src/models/client-dto";

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private apiUrl = 'api/clients'; // Replace with your actual API URL

  constructor(private http: HttpClient) {}

  getClients(page: number, size: number): Observable<any> {
    return this.http.get(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  getClient(id: string): Observable<ClientDto> {
    return this.http.get<ClientDto>(`${this.apiUrl}/${id}`);
  }

  createClient(client: ClientDto): Observable<ClientDto> {
    return this.http.post<ClientDto>(this.apiUrl, client);
  }

  updateClient(id: string, client: ClientDto): Observable<ClientDto> {
    return this.http.put<ClientDto>(`${this.apiUrl}/${id}`, client);
  }

  deleteClient(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleClientStatus(id: string, enabled: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/status`, { enabled });
  }
}
