import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, tap } from 'rxjs';
import { MediaCardModel } from '../models/media-card.model';
import { MovieModel } from '../models/movie.model';
import { SeriesModel } from '../models/series.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  private baseUrl = environment.beUrl;
  private filterUrl = `${this.baseUrl}/media/filter`;
  private movieUrl = `${this.baseUrl}/movies`;
  private seriesUrl = `${this.baseUrl}/series`;

  private mediaList: MediaCardModel[] = [];

  constructor(private http: HttpClient) {}

  getAllMedia(): Observable<MediaCardModel[]> {
    return this.http.get<MediaCardModel[]>(this.filterUrl).pipe(
      tap(media => this.mediaList = media)
    );
  }

  getAllMovies(): Observable<MovieModel[]> {
    return this.http.get<MovieModel[]>(this.movieUrl);
  }

  getAllSeries(): Observable<SeriesModel[]> {
    return this.http.get<SeriesModel[]>(this.seriesUrl);
  }

  searchMedia(query: string): Observable<MediaCardModel[]> {
    if (query.length <= 3) return of([]);
    const url = `${this.baseUrl}/media/search?query=${encodeURIComponent(query)}`;
    return this.http.get<MediaCardModel[]>(url).pipe(
      tap(media => this.mediaList = media)
    );
  }

  filteredMediaList(query: string): MediaCardModel[] {
    if (!query.trim()) return this.mediaList;
    const lower = query.toLowerCase();
    return this.mediaList.filter(
      item =>
        item.title.toLowerCase().includes(lower) ||
        item.type.toLowerCase().includes(lower)
    );
  }

  getMovieById(id: number): Observable<MovieModel> {
    return this.http.get<MovieModel>(`${this.movieUrl}/${id}`);
  }

  getSeriesById(id: number): Observable<SeriesModel> {
    return this.http.get<SeriesModel>(`${this.seriesUrl}/${id}`);
  }

  createMovie(payload: MovieModel): Observable<MovieModel> {
    return this.http.post<MovieModel>(this.movieUrl, payload);
  }

  createSeries(payload: SeriesModel): Observable<SeriesModel> {
    return this.http.post<SeriesModel>(this.seriesUrl, payload);
  }

  updateMovie(id: number, payload: MovieModel): Observable<MovieModel> {
    return this.http.put<MovieModel>(`${this.movieUrl}/${id}`, payload);
  }

  updateSeries(id: number, payload: SeriesModel): Observable<SeriesModel> {
    return this.http.put<SeriesModel>(`${this.seriesUrl}/${id}`, payload);
  }

  deleteMedia(id: number, type: 'MOVIE' | 'SERIES'): Observable<void> {
    const url = type === 'MOVIE' ? `${this.movieUrl}/${id}` : `${this.seriesUrl}/${id}`;
    return this.http.delete<void>(url).pipe(
      tap(() => {
        this.mediaList = this.mediaList.filter(item => item.id !== id);
      })
    );
  }
}
