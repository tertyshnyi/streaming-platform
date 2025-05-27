import { EpisodeModel } from "./episode.model";
import { MediaContentModel } from "./media-content.model";

export interface SeriesModel extends MediaContentModel {
  type: 'SERIES';
  avgDuration: number;
  episodesCount: number;
  episodes: EpisodeModel[];
}
