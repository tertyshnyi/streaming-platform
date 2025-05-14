import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { MediaCardModel } from '../models/media-card.model';
import { MediaContentModel } from '../models/media-content.model';

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  private mediaData: MediaContentModel = {
    id: 1,
    title: 'Amazing Show',
    slug: 'amazing-show',
    description: 'An amazing show that captivates audiences worldwide.',
    poster: '/images/settings-background.jpg',
    genres: ['Action', 'Drama', 'Adventure'],
    dubbing: 'English',
    sound: 'Stereo',
    translation: 'Spanish',
    episodes: '12',
    season: '1',
    status: 'Ongoing',
    countries: ['USA', 'Canada'],
    trailerUrl: 'https://www.youtube.com/watch?v=ee9i6oMqShk'
  };

  private mediaCardData: MediaCardModel[] = [
    {
      id: 1,
      title: 'Amazing Show',
      slug: 'amazing-show',
      poster: '/images/settings-background.jpg',
      genres: ['Action', 'Drama'],
      rating: 8.5,
      countries: ['USA'],
      year: '2023',
    },
    {
      id: 2,
      title: 'Another Show',
      slug: 'another-show',
      poster: '/images/settings-background.jpg',
      genres: ['Comedy', 'Drama'],
      rating: 7.4,
      countries: ['USA'],
      year: '2022',
    },
  ];

  getBySlug(slug: string): Observable<MediaContentModel | null> {
    // Если slug совпадает с slug в mediaData, возвращаем объект
    return of(this.mediaData.slug === slug ? this.mediaData : null);
  }

  getMedia(): Observable<MediaCardModel[]> {
    return of(this.mediaCardData);
  }
}
