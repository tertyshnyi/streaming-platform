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
  @Input() selectedQuality?: number;

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

    const qualities = this.getAvailableQualities();
    if (!this.selectedQuality || !qualities.includes(this.selectedQuality)) {
      this.selectedQuality = qualities[0];
    }

    const initialVideo = this.media.videos.find(v => v.quality === this.selectedQuality);
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
        default: this.selectedQuality,
        options: qualities,
        forced: true,
        onChange: (newQuality: number) => this.changeQuality(newQuality)
      }
    });

    this.player.source = {
      type: 'video',
      title: this.media.title,
      sources: [
        {
          src: initialVideo.url,
          type: 'video/mp4',
          size: this.selectedQuality
        }
      ]
    };
  }

  private async changeQuality(newQuality: number): Promise<void> {
    if (!this.media || !this.player || this.changingQuality || this.selectedQuality === newQuality) return;

    const video = this.media?.videos?.find(v => v.quality === newQuality);
    if (!video) return;

    this.changingQuality = true;
    this.selectedQuality = newQuality;

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
          size: newQuality
        }
      ]
    };

    try {
      await this.player.play();
    } catch {}

    this.changingQuality = false;
  }

  private getAvailableQualities(): number[] {
    if (!this.media?.videos) return [];
    return Array.from(new Set(this.media.videos.map(v => v.quality))).sort((a, b) => b - a);
  }
}
