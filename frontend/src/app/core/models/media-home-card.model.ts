export interface MediaHomeCardModel {
  id: number;
  slug: string;
  type: 'MOVIE' | 'SERIES';
  title: string;
  description: string;
  coverImg: string;
}
