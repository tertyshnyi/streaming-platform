import { VideoSourceModel } from "./video-source.model";

export interface EpisodeModel {
  id: number;
  title: string;
  duration: number;
  videos?: VideoSourceModel[];
}