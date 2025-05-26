export interface SearchResultModel {
  id: number;
  title: string;
  description: string;
  posterImg: string;
  globalRating: number | null;
  genres: string[];
  type: string;
  slug: string;
  releaseDate: string;
  countries: string[];
}
