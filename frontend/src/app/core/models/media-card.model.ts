export interface MediaCardModel {
  id: number;
  title: string;
  description: string;
  type: string;
  slug: string;
  globalRating: number | null;
  releaseDate: string;
  countries: string[];
  posterImg: string;
  genres: string[];
}
