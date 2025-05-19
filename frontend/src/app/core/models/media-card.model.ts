export interface MediaCardModel {
  id: number;
  title: string;
  slug: string;
  posterImg: string;
  genres: string[];
  globalRating: number;
  countries?: string[];
  year?: string;
}
