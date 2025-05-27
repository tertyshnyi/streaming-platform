import { MediaContentModel } from "../models/media-content.model";
import { MovieModel } from "../models/movie.model";
import { SeriesModel } from "../models/series.model";

export function isMovie(media: MediaContentModel): media is MovieModel {
  return media.type === 'MOVIE';
}

export function isSeries(media: MediaContentModel): media is SeriesModel {
  return media.type === 'SERIES';
}
