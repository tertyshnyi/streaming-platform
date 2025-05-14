export interface MediaCardModel {
  id: number;
  title: string;
  slug: string;
  poster: string;
  genres: string[];
  rating: number;
  countries?: string[];
  year?: string;
}
