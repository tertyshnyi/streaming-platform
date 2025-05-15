export interface MediaContentModel {
  id: number;
  title: string;
  slug: string;
  releaseDate: string; // ISO формат даты, например: "2023-05-01"
  description: string;
  genres: string[];
  actors: string[];
  directors: string[];
  trailerUrl?: string;
  countries: string[];
  globalRating: number; // например, 7.8
  // comments: CommentModel[]; // предполагается, что у тебя есть такой интерфейс
  commentsTotal: number;
  posterImg: string;
  coverImg: string;
  duration: string;
  videos?: VideoSourceModel[];
}

export interface VideoSourceModel {
  quality: number;
  url: string; // для YouTube — это может быть либо полная ссылка, либо ID
  type: 'mp4' | 'youtube';
}
