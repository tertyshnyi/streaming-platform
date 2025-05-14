export interface MediaContentModel {
  id: number;
  title: string;
  slug: string;
  description: string;
  poster: string;
  genres: string[];
  dubbing?: string;
  sound?: string;
  translation?: string;
  episodes: string;
  season: string;
  status: string;
  countries: string[];
  trailerUrl?: string;
}
