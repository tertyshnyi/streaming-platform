import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MovieModel } from '../models/movie.model';
import { SeriesModel } from '../models/series.model';
import { environment } from '../../environments/environment';
import { MediaHomeCardModel } from '../models/media-home-card.model';

@Injectable({
  providedIn: 'root'
})
export class MediaContentService {
  private baseUrl = environment.beUrl;

  constructor(private http: HttpClient) {}

  getLatestMedia(): Observable<MediaHomeCardModel[]> {
    return this.http.get<MediaHomeCardModel[]>(`${this.baseUrl}/media/latest`);
  }

  getById<T extends MovieModel | SeriesModel>(id: number, type: 'MOVIE' | 'SERIES'): Observable<T> {
    const endpoint = type === 'MOVIE' ? 'movies' : 'series';
    return this.http.get<T>(`${this.baseUrl}/${endpoint}/${id}`);
  }
}
