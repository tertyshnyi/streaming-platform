import { Component, ElementRef, Input, OnDestroy, AfterViewInit, ViewChild, OnChanges, SimpleChanges } from '@angular/core';
import Plyr from 'plyr';
import { CommonModule } from '@angular/common';
import { MediaContentModel } from '../../../core/models/media-content.model';
import { isMovie, isSeries } from '../../../core/guards/media-type.guard';
import { VideoSourceModel } from '../../../core/models/video-source.model';

@Component({
  selector: 'app-video-player',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './video-player.component.html',
  styleUrls: ['./video-player.component.scss']
})
export class VideoPlayerComponent implements AfterViewInit, OnDestroy, OnChanges {
  @Input() media: MediaContentModel | null = null;
  @Input() selectedResolution?: string;
  @Input() selectedEpisodeIndex?: number;

  @ViewChild('videoPlayer', { static: false }) videoPlayerRef!: ElementRef<HTMLVideoElement>;

  player?: Plyr;
  changingQuality = false;
  noVideoAvailable = false;

  ngAfterViewInit(): void {
    console.log('[VideoPlayer] ngAfterViewInit - initializing player');
    this.initPlayer();
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('[VideoPlayer] ngOnChanges - changes:', changes);

    if (!this.player) return;

    if (changes['media'] || changes['selectedEpisodeIndex']) {
      console.log('[VideoPlayer] media or selectedEpisodeIndex changed - resetting selectedResolution');
      this.selectedResolution = undefined;
    }

    if (
      changes['media'] ||
      changes['selectedResolution'] ||
      changes['selectedEpisodeIndex']
    ) {
      console.log('[VideoPlayer] media, selectedResolution or selectedEpisodeIndex changed - updating player source');
      this.updatePlayerSource();
    }
  }

  ngOnDestroy(): void {
    console.log('[VideoPlayer] ngOnDestroy - destroying player');
    this.player?.destroy();
  }

  private initPlayer(): void {
    if (!this.videoPlayerRef?.nativeElement) {
      console.warn('[VideoPlayer] videoPlayerRef not initialized');
      return;
    }

    const videos = this.getCurrentVideos();
    console.log('[VideoPlayer] initPlayer - current videos:', videos);

    if (!videos?.length) {
      this.noVideoAvailable = true;
      console.warn('[VideoPlayer] No videos available');
      return;
    }

    const resolutions = this.getAvailableResolutions(videos);
    console.log('[VideoPlayer] initPlayer - available resolutions:', resolutions);

    if (!this.selectedResolution || !resolutions.includes(this.selectedResolution)) {
      this.selectedResolution = resolutions[0];
      console.log('[VideoPlayer] initPlayer - setting default selectedResolution:', this.selectedResolution);
    }

    this.noVideoAvailable = false;

    this.player = new Plyr(this.videoPlayerRef.nativeElement, {
      controls: [
        'play', 'pause', 'rewind', 'fast-forward', 'progress',
        'current-time', 'duration', 'mute', 'volume', 'captions',
        'settings', 'pip', 'airplay', 'download', 'fullscreen'
      ],
      settings: ['quality'],
      quality: {
        default: this.parseResolutionToNumber(this.selectedResolution),
        options: resolutions.map(r => this.parseResolutionToNumber(r)),
        forced: true,
        onChange: (newQuality: number) => {
          const newRes = resolutions.find(r => this.parseResolutionToNumber(r) === newQuality);
          if (newRes) {
            console.log('[VideoPlayer] Quality changed to:', newRes);
            this.changeResolution(newRes);
          }
        }
      }
    });

    this.updatePlayerSource();
  }

  private async changeResolution(newResolution: string): Promise<void> {
    if (this.changingQuality) {
      console.log('[VideoPlayer] changeResolution called but already changing quality');
      return;
    }
    if (this.selectedResolution === newResolution) {
      console.log('[VideoPlayer] changeResolution called but resolution is the same:', newResolution);
      return;
    }

    console.log('[VideoPlayer] Changing resolution from', this.selectedResolution, 'to', newResolution);
    this.selectedResolution = newResolution;

    await this.updatePlayerSource();
  }

  private async updatePlayerSource(): Promise<void> {
    if (!this.player) {
      console.warn('[VideoPlayer] updatePlayerSource called but player not initialized');
      return;
    }

    const videos = this.getCurrentVideos();
    console.log('[VideoPlayer] updatePlayerSource - current videos:', videos);

    if (!videos?.length) {
      this.noVideoAvailable = true;
      console.warn('[VideoPlayer] updatePlayerSource - no videos available');
      return;
    }

    const resolutions = this.getAvailableResolutions(videos);
    console.log('[VideoPlayer] updatePlayerSource - available resolutions:', resolutions);

    if (!this.selectedResolution || !resolutions.includes(this.selectedResolution)) {
      this.selectedResolution = resolutions[0];
      console.log('[VideoPlayer] updatePlayerSource - setting selectedResolution to default:', this.selectedResolution);
    }

    const video = videos.find(v => v.resolution === this.selectedResolution);
    if (!video) {
      this.noVideoAvailable = true;
      console.warn('[VideoPlayer] updatePlayerSource - video not found for resolution:', this.selectedResolution);
      return;
    }

    this.noVideoAvailable = false;
    this.changingQuality = true;

    try {
      await this.player.pause();
    } catch (e) {
      console.warn('[VideoPlayer] Failed to pause player:', e);
    }

    console.log('[VideoPlayer] updatePlayerSource - setting player source to:', video.url);

    this.player.source = {
      type: 'video',
      title: this.media?.title ?? '',
      sources: [
        {
          src: video.url,
          type: 'video/mp4',
          size: this.parseResolutionToNumber(this.selectedResolution)
        }
      ]
    };

    try {
      await this.player.play();
      console.log('[VideoPlayer] updatePlayerSource - player started playing');
    } catch (e) {
      console.warn('[VideoPlayer] Failed to play video:', e);
    }

    this.changingQuality = false;
  }

  private getCurrentVideos(): VideoSourceModel[] | null {
    if (!this.media) {
      console.log('[VideoPlayer] getCurrentVideos - no media');
      return null;
    }

    if (isMovie(this.media)) {
      console.log('[VideoPlayer] getCurrentVideos - media is movie');
      return this.media.videos ?? [];
    }

    if (isSeries(this.media)) {
      const episodeIndex = this.selectedEpisodeIndex ?? 0;
      console.log(`[VideoPlayer] getCurrentVideos - media is series, selectedEpisodeIndex: ${episodeIndex}`);
      const episode = this.media.episodes?.[episodeIndex];
      console.log('[VideoPlayer] getCurrentVideos - current episode:', episode);
      return episode?.videos ?? [];
    }

    console.log('[VideoPlayer] getCurrentVideos - media type unknown');
    return null;
  }

  private getAvailableResolutions(videos: VideoSourceModel[]): string[] {
    const res = Array.from(new Set(videos.map(v => v.resolution)))
      .sort((a, b) => this.parseResolutionToNumber(b) - this.parseResolutionToNumber(a));
    console.log('[VideoPlayer] getAvailableResolutions:', res);
    return res;
  }

  private parseResolutionToNumber(resolution: string | undefined): number {
    if (!resolution) return 0;
    const numericPart = resolution.replace(/\D/g, '');
    return numericPart ? parseInt(numericPart, 10) : 0;
  }
}
