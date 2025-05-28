export interface MediaCardModel {
  id: number;
  title: string;
  description: string;
  type: 'MOVIE' | 'SERIES';
  slug: string;
  globalRating: number | null;
  releaseDate: string;
  countries: string[];
  posterImg: string;
  genres: string[];
}
