import { Injectable } from '@angular/core';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  private mediaList = [
    {
      id: 1,
      title: 'Movie 1',
      type: 'Movie',
      releaseDate: '2022-01-01',
      genres: ['ACTION', 'DRAMA'],
      description: 'An action-packed drama.',
      trailerUrl: 'https://example.com/trailer1',
      posterImg: 'https://i.pinimg.com/736x/67/74/3a/67743a994e1549a411a1e1098d60c3a4.jpg',
      coverImg: 'https://example.com/cover1.jpg',
      actors: ['John Doe', 'Jane Smith'],
      directors: ['Michael Bay'],
      countries: ['USA'],
      videos: [
        { resolution: '1080p', url: 'https://video.example.com/1080p.mp4' },
        { resolution: '720p', url: 'https://video.example.com/720p.mp4' }
      ]
    },
    {
      id: 2,
      title: 'Series 1',
      type: 'Series',
      releaseDate: '2022-01-01',
      genres: ['ACTION', 'DRAMA'],
      description: 'An action-packed drama.',
      trailerUrl: 'https://example.com/trailer1',
      posterImg: 'https://i.pinimg.com/736x/67/74/3a/67743a994e1549a411a1e1098d60c3a4.jpg',
      coverImg: 'https://example.com/cover1.jpg',
      actors: ['John Doe', 'Jane Smith'],
      directors: ['Michael Bay'],
      countries: ['USA'],
      episodes: [
        {
          title: 'Pilot',
          duration: 42,
          videos: [
            {
              resolution: '1080p',
              url: 'https://cdn.example.com/series/s01e01-1080p.mp4'
            },
            {
              resolution: '720p',
              url: 'https://cdn.example.com/series/s01e01-720p.mp4'
            }
          ]
        },
        {
          title: 'Second Episode',
          duration: 45,
          videos: [
            {
              resolution: '1080p',
              url: 'https://cdn.example.com/series/s01e02-1080p.mp4'
            }
          ]
        }
      ]
    }
  ];

  constructor(
    // private http: HttpClient
  ) {}

  getAllMedia(): Observable<any[]> {
    // return this.http.get('/api/media');
    return of(this.mediaList);
  }

  getMediaById(id: number): Observable<any> {
    const media = this.mediaList.find(item => item.id === id);
    return of(media);
  }

  updateMedia(id: number, updatedData: any): Observable<any> {
    const index = this.mediaList.findIndex(item => item.id === id);
    if (index !== -1) {
      this.mediaList[index] = { ...this.mediaList[index], ...updatedData };
    }
    return of(this.mediaList[index]);
  }

  deleteMedia(id: number): Observable<void> {
    this.mediaList = this.mediaList.filter(item => item.id !== id);
    return of(void 0);
  }

  createMedia(media: any): Observable<any> {
    const newId = this.mediaList.length ? Math.max(...this.mediaList.map(m => m.id)) + 1 : 1;
    const newMedia = { id: newId, ...media };
    this.mediaList.push(newMedia);
    return of(newMedia);
  }

  filteredMediaList(query: string): any[] {
    if (!query.trim()) return this.mediaList;

    const lower = query.toLowerCase();
    return this.mediaList.filter(
      item =>
        item.title.toLowerCase().includes(lower) ||
        item.type?.toLowerCase().includes(lower)
    );
  }
}
