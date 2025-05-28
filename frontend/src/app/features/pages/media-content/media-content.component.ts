import { Component, OnInit, OnDestroy, signal, Input } from '@angular/core';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { SparklesComponent } from '../../../shared/components/sparkles/sparkles.component';
import { VideoPlayerComponent } from '../../components/video-player/video-player.component';
import { CommentsSectionComponent } from '../../components/comments-section/comments-section.component';

import { MediaContentService } from '../../../core/services/media-content.service';
import { UserService } from '../../../core/services/user.service';
import { CommentsService } from '../../../core/services/comments.service';

import { MediaContentModel } from '../../../core/models/media-content.model';
import { CommentModel } from '../../../core/models/comment.model';

import { switchMap, filter, tap } from 'rxjs/operators';
import { MovieModel } from '../../../core/models/movie.model';
import { SeriesModel } from '../../../core/models/series.model';
import { isMovie, isSeries } from '../../../core/guards/media-type.guard';

@Component({
  selector: 'app-media-content',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SparklesComponent,
    VideoPlayerComponent,
    CommentsSectionComponent
  ],
  templateUrl: './media-content.component.html',
  styleUrls: ['./media-content.component.scss']
})
export class MediaContentComponent implements OnInit, OnDestroy {

  media = signal<MediaContentModel | null>(null);

  comments = signal<CommentModel[]>([]);
  loading = signal(true);

  isTrailerOpen = signal(false);
  isDescriptionExpanded = signal(false);

  private _selectedEpisodeIndex = signal(0);
  selectedEpisodeIndex = this._selectedEpisodeIndex.asReadonly();

  selectEpisode(index: number) {
    this._selectedEpisodeIndex.set(index);
  }

  isAuthenticated!: () => boolean;

  constructor(
    public userService: UserService,
    private route: ActivatedRoute,
    private mediaService: MediaContentService,
    private router: Router,
    private sanitizer: DomSanitizer,
    private commentsService: CommentsService,
  ) {}

  isMovie(media: MediaContentModel | null): media is MovieModel {
    return !!media && isMovie(media);
  }

  isSeries(media: MediaContentModel | null): media is SeriesModel {
    return !!media && isSeries(media);
  }

  ngOnInit(): void {
    this.isAuthenticated = this.userService.isAuthenticatedSignal;

    const type = this.route.snapshot.paramMap.get('type');
    const idStr = this.route.snapshot.paramMap.get('id');
    const id = idStr ? +idStr : null;

    if (!id || !type || (type !== 'movie' && type !== 'series')) {
      this.router.navigateByUrl('/404', { replaceUrl: true });
      this.loading.set(false);
      return;
    }

    this.mediaService.getById(id, type.toUpperCase() as 'MOVIE' | 'SERIES').pipe(
      tap(mediaData => {
        if (!mediaData) {
          this.router.navigateByUrl('/404', { replaceUrl: true });
          this.loading.set(false);
        }
      }),
      filter(mediaData => !!mediaData),
      switchMap(mediaData => {
        this.media.set(mediaData!);
        this.loading.set(false);

        this._selectedEpisodeIndex.set(0);

        return this.commentsService.getCommentsByMediaId(mediaData!.id);
      })
    ).subscribe({
      next: (comments) => {
        this.comments.set(comments);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  ngOnDestroy(): void {
    document.body.style.overflow = 'auto';
  }

  openTrailer(): void {
    this.isTrailerOpen.set(true);
    document.body.style.overflow = 'hidden';
  }

  closeTrailer(): void {
    this.isTrailerOpen.set(false);
    document.body.style.overflow = 'auto';

    const iframe = document.querySelector('iframe');
    if (iframe) {
      iframe.remove();
    }
  }

  toggleDescription(): void {
    this.isDescriptionExpanded.update(v => !v);
  }

  getYouTubeEmbedUrl(url?: string): SafeResourceUrl | '' {
    if (!url) return '';
    const videoId = url.split('v=')[1]?.split('&')[0];
    if (!videoId) return '';

    const embedUrl = `https://www.youtube.com/embed/${videoId}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  }

  getMovieDuration(m: any): string {
    if (!m.duration) return '';

    const hours = Math.floor(m.duration / 60);
    const minutes = m.duration % 60;

    let durationText = '';
    if (hours > 0) {
      durationText += `${hours} hours`;
      if (minutes > 0) {
        durationText += ` ${minutes} min`;
      }
    } else {
      durationText = `${minutes} min`;
    }

    return durationText;
  }

  getEpisodesInfo(m: any): string {
    if (!m.episodesCount) return '';

    if (!m.avgDuration) {
      return `${m.episodesCount}`;
    }

    const hours = Math.floor(m.avgDuration / 60);
    const minutes = m.avgDuration % 60;

    let durationText = '';
    if (hours > 0) {
      durationText += `${hours} ч`;
      if (minutes > 0) {
        durationText += ` ${minutes} мин`;
      }
    } else {
      durationText = `${minutes} мин`;
    }

    return `${m.episodesCount} (${durationText})`;
  }

  get series(): SeriesModel | null {
    const m = this.media();
    return this.isSeries(m) ? m : null;
  }

  get showEpisodesSection(): boolean {
    return this.isSeries(this.media()) && !!this.series?.episodes?.length;
  }

  get showNoEpisodesMessage(): boolean {
    return this.isSeries(this.media()) && (!this.series?.episodes || this.series.episodes.length === 0);
  }
}
