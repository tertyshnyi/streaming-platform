import { MediaContentModel } from "./media-content.model";
import { VideoSourceModel } from "./video-source.model";

export interface MovieModel extends MediaContentModel {
  type: 'MOVIE';
  duration: number;
  videos?: VideoSourceModel[];
}
