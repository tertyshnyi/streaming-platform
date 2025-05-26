import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MediaCardModel } from '../models/media-card.model';
import { environment } from '../../environments/environment.development';


@Injectable({ providedIn: 'root' })
export class AdvancedFilterService {
  private apiUrl = `${environment.beUrl}/media`;

  constructor(private http: HttpClient) {}

  filterMedia(
    type?: string,
    genres?: string[],
    countries?: string[],
    releaseYears?: number[],
    ratingFrom?: number,
    ratingTo?: number
  ): Observable<MediaCardModel[]> {
    let params = new HttpParams();

    if (type) params = params.set('type', type);
    if (genres && genres.length) genres.forEach(g => params = params.append('genres', g));
    if (countries && countries.length) countries.forEach(c => params = params.append('countries', c));
    if (releaseYears && releaseYears.length) releaseYears.forEach(y => params = params.append('releaseYears', y.toString()));

    if (ratingFrom !== undefined && ratingFrom !== 0) {
      params = params.set('ratingFrom', ratingFrom.toString());
    }
    
    if (ratingTo !== undefined && ratingTo !== 10) {
      params = params.set('ratingTo', ratingTo.toString());
    }

    return this.http.get<MediaCardModel[]>(`${this.apiUrl}/filter`, { params });
  }

  getGenres(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/genres`);
  }

  getCountries(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/countries`);
  }

  getReleaseYears(): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/releaseYears`);
  }
}
