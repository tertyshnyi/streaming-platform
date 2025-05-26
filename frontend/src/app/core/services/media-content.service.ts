import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MediaContentModel } from '../models/media-content.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MediaContentService {
  private baseUrl = environment.beUrl;

  constructor(private http: HttpClient) {}

  getById(id: number, type: 'MOVIE' | 'SERIES'): Observable<MediaContentModel> {
    const endpoint = type === 'MOVIE' ? 'movies' : 'series';
    return this.http.get<MediaContentModel>(`${this.baseUrl}/${endpoint}/${id}`);
  }
}
