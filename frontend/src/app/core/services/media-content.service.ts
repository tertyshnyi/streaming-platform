import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MovieModel } from '../models/movie.model';
import { SeriesModel } from '../models/series.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MediaContentService {
  private baseUrl = environment.beUrl;

  constructor(private http: HttpClient) {}

  getById<T extends MovieModel | SeriesModel>(id: number, type: 'MOVIE' | 'SERIES'): Observable<T> {
    const endpoint = type === 'MOVIE' ? 'movies' : 'series';
    return this.http.get<T>(`${this.baseUrl}/${endpoint}/${id}`);
  }
}
