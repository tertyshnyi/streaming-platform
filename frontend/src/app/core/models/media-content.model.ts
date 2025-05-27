export interface MediaContentModel {
  id: number;
  title: string;
  slug: string;
  releaseDate: string;
  releaseYear: number;
  description: string;
  genres: string[];
  actors: string[];
  directors: string[];
  trailerUrl?: string;
  countries: string[];
  globalRating: number | null;
  posterImg: string;
  coverImg: string;
  type: 'MOVIE' | 'SERIES';
  commentsTotal: number;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string | null;
}
