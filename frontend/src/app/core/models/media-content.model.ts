export interface MediaContentModel {
  id: number;
  title: string;
  slug: string;
  releaseDate: string;
  description: string;
  genres: string[];
  actors: string[];
  directors: string[];
  trailerUrl?: string;
  countries: string[];
  globalRating: number;
  commentsTotal: number;
  posterImg: string;
  coverImg: string;
  duration: string;
  videos?: VideoSourceModel[];
}

export interface VideoSourceModel {
  quality: number;
  url: string;
  type: 'mp4' | 'youtube';
}
