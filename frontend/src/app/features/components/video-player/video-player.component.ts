import { Component, ElementRef, Input, OnDestroy, AfterViewInit, ViewChild } from '@angular/core';
import Plyr from 'plyr';
import { CommonModule } from '@angular/common';
import { MediaContentModel } from '../../../core/models/media-content.model';

@Component({
  selector: 'app-video-player',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './video-player.component.html',
  styleUrls: ['./video-player.component.scss']
})
export class VideoPlayerComponent implements AfterViewInit, OnDestroy {
  @Input() media: MediaContentModel | null = null;
  @Input() selectedResolution?: string;

  @ViewChild('videoPlayer', { static: false }) videoPlayerRef!: ElementRef<HTMLVideoElement>;

  player?: Plyr;
  changingQuality = false;
  noVideoAvailable = false;

  ngAfterViewInit(): void {
    if (this.videoPlayerRef) {
      this.initPlayer();
    } else {
      console.warn('videoPlayerRef not initialized');
    }
  }

  ngOnDestroy(): void {
    this.player?.destroy();
  }

  private initPlayer(): void {
    if (!this.videoPlayerRef?.nativeElement || !this.media?.videos?.length) {
      this.noVideoAvailable = true;
      return;
    }

    const resolutions = this.getAvailableResolutions();
    if (!this.selectedResolution || !resolutions.includes(this.selectedResolution)) {
      this.selectedResolution = resolutions[0];
    }

    const initialVideo = this.media.videos.find(v => v.resolution === this.selectedResolution);
    if (!initialVideo) {
      this.noVideoAvailable = true;
      return;
    }

    this.noVideoAvailable = false;

    this.player = new Plyr(this.videoPlayerRef.nativeElement, {
      controls: [
        'play',
        'pause',
        'rewind',
        'fast-forward',
        'progress',
        'current-time',
        'duration',
        'mute',
        'volume',
        'captions',
        'settings',
        'pip',
        'airplay',
        'download',
        'fullscreen'
      ],
      settings: ['quality'],
      quality: {
        default: this.parseResolutionToNumber(this.selectedResolution),
        options: resolutions.map(r => this.parseResolutionToNumber(r)),
        forced: true,
        onChange: (newQuality: number) => {
          const newRes = resolutions.find(r => this.parseResolutionToNumber(r) === newQuality);
          if (newRes) this.changeResolution(newRes);
        }
      }
    });

    this.player.source = {
      type: 'video',
      title: this.media.title,
      sources: [
        {
          src: initialVideo.url,
          type: 'video/mp4',
          size: this.parseResolutionToNumber(this.selectedResolution)
        }
      ]
    };
  }

  private async changeResolution(newResolution: string): Promise<void> {
    if (!this.media || !this.player || this.changingQuality || this.selectedResolution === newResolution) return;

    const video = this.media?.videos?.find(v => v.resolution === newResolution);
    if (!video) return;

    this.changingQuality = true;
    this.selectedResolution = newResolution;

    try {
      await this.player.pause();
    } catch {}

    this.player.source = {
      type: 'video',
      title: this.media.title,
      sources: [
        {
          src: video.url,
          type: 'video/mp4',
          size: this.parseResolutionToNumber(newResolution)
        }
      ]
    };

    try {
      await this.player.play();
    } catch {}

    this.changingQuality = false;
  }

  private getAvailableResolutions(): string[] {
    if (!this.media?.videos) return [];
    return Array.from(new Set(this.media.videos.map(v => v.resolution)))
      .sort((a, b) => this.parseResolutionToNumber(b) - this.parseResolutionToNumber(a));
  }

  private parseResolutionToNumber(resolution: string | undefined): number {
    if (!resolution) return 0;
    const numericPart = resolution.replace(/\D/g, '');
    return numericPart ? parseInt(numericPart) : 0;
  }
}
