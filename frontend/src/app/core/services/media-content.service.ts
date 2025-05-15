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
    releaseDate: '2023-05-01',
    description: 'An amazing show that captivates audiences worldwide. An amazing show that captivates audiences worldwide.' +
      'An amazing show that captivates audiences worldwide. An amazing show that captivates audiences worldwide.' +
      ' An amazing show that captivates audiences worldwide. An amazing show that captivates audiences worldwide.' +
      'An amazing show that captivates audiences worldwide. An amazing show that captivates audiences worldwide.' +
      'An amazing show that captivates audiences worldwide. An amazing show that captivates audiences worldwide.' +
      'An amazing show that captivates audiences worldwide. An amazing show that captivates audiences worldwide.',
    genres: ['Action', 'Drama', 'Adventure'],
    actors: ['John Doe', 'Jane Smith'],
    directors: ['Alice Johnson'],
    trailerUrl: 'https://www.youtube.com/watch?v=ee9i6oMqShk',
    countries: ['USA', 'Canada'],
    globalRating: 8.4,
    commentsTotal: 0,
    posterImg: '/images/posterImg.webp',
    coverImg: '/images/posterImg.webp',
    duration: '25m',
    videos: [
      {
        quality: 1080,
        url: '/videos/videoplayback.mp4',
        type: 'mp4',
      },
      {
        quality: 720,
        url: '/videos/test.mp4',
        type: 'mp4',
      }
    ]
  };

  private mediaCardData: MediaCardModel[] = [
    {
      id: 1,
      title: 'Amazing Show',
      slug: 'amazing-show',
      posterImg: '/images/posterImg.webp',
      genres: ['Action', 'Drama'],
      globalRating: 8.5,
      countries: ['USA'],
      year: '2023',
    },
    {
      id: 2,
      title: 'Another Show',
      slug: 'another-show',
      posterImg: '/images/posterImg.webp',
      genres: ['Comedy', 'Drama'],
      globalRating: 7.4,
      countries: ['USA'],
      year: '2022',
    },
  ];

  getBySlug(slug: string): Observable<MediaContentModel | null> {
    return of(this.mediaData.slug === slug ? this.mediaData : null);
  }

  getMedia(): Observable<MediaCardModel[]> {
    return of(this.mediaCardData);
  }
}
