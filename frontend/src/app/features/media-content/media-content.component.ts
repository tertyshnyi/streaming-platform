import { Component, ElementRef, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DomSanitizer } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import Plyr from 'plyr';

import { SparklesComponent } from '../../shared/components/sparkles/sparkles.component';
import { MediaService } from '../../core/services/media-content.service';
import { MediaContentModel } from '../../core/models/media-content.model';

@Component({
  selector: 'app-media-content',
  imports: [
    CommonModule,
    FormsModule,
    SparklesComponent
  ],
  standalone: true,
  templateUrl: './media-content.component.html',
  styleUrls: ['./media-content.component.scss']
})
export class MediaContentComponent implements OnInit, OnDestroy {
  media: MediaContentModel | null = null;
  loading = true;

  isTrailerOpen = false;
  isDescriptionExpanded = false;

  selectedQuality?: number;
  private player?: Plyr;

  @ViewChild('videoPlayer', { static: false }) videoPlayerRef!: ElementRef<HTMLVideoElement>;

  constructor(
    private route: ActivatedRoute,
    private mediaService: MediaService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit() {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (slug) {
      this.mediaService.getBySlug(slug).subscribe(data => {
        if (data) {
          this.media = data;

          const qualities = this.getAvailableQualities(data);
          this.selectedQuality = qualities.length ? qualities[0] : undefined;

          this.loading = false;

          setTimeout(() => {
            this.initPlayer();
          }, 0);
        } else {
          this.router.navigateByUrl('/404', { replaceUrl: true });
          this.loading = false;
        }
      });
    } else {
      this.router.navigateByUrl('/404', { replaceUrl: true });
      this.loading = false;
    }
  }

  private initPlayer(): void {
    if (!this.videoPlayerRef || !this.media?.videos?.length) return;

    const qualities = this.getAvailableQualities(this.media);
    if (!this.selectedQuality || !qualities.includes(this.selectedQuality)) {
      this.selectedQuality = qualities[0];
    }

    const initialVideo = this.media.videos.find(v => v.quality === this.selectedQuality);
    if (!initialVideo) return;

    this.player = new Plyr(this.videoPlayerRef.nativeElement, {
      controls: ['play', 'progress', 'current-time', 'mute', 'volume', 'fullscreen', 'settings'],
      settings: ['quality'],
      quality: {
        default: this.selectedQuality,
        options: qualities,
        forced: true,
        onChange: (newQuality: number) => {
          this.changeQuality(newQuality);
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
          size: this.selectedQuality,
        }
      ]
    };
  }

  private changingQuality = false;

  private async changeQuality(newQuality: number): Promise<void> {
    if (!this.media || !this.player || this.changingQuality) return;

    if (this.selectedQuality === newQuality) return;

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
          size: newQuality,
        }
      ]
    };

    try {
      await this.player.play();
    } catch {}

    this.changingQuality = false;
  }

  getAvailableQualities(media?: MediaContentModel | null): number[] {
    if (!media?.videos) return [];
    const qualities = media.videos.map(v => Number(v.quality));
    return Array.from(new Set(qualities)).sort((a, b) => b - a);
  }

  ngOnDestroy(): void {
    document.body.style.overflow = 'auto';
    this.player?.destroy();
  }

  openTrailer() {
    this.isTrailerOpen = true;
    document.body.style.overflow = 'hidden';
  }

  closeTrailer() {
    this.isTrailerOpen = false;
    document.body.style.overflow = 'auto';
  }

  toggleDescription() {
    this.isDescriptionExpanded = !this.isDescriptionExpanded;
  }

  getYouTubeEmbedUrl(url: string | undefined): any {
    if (!url) return '';
    const videoId = url.split('v=')[1]?.split('&')[0];
    if (!videoId) return '';

    const embedUrl = `https://www.youtube.com/embed/${videoId}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  }
}
