import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SearchResultModel } from '../models/search-result.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.beUrl}/media/search`;

  search(query: string): Observable<SearchResultModel[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<SearchResultModel[]>(this.apiUrl, { params });
  }
}
