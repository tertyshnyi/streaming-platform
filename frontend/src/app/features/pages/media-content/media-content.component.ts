import { Component, OnInit, OnDestroy, signal, effect } from '@angular/core';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DomSanitizer } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { SparklesComponent } from '../../../shared/components/sparkles/sparkles.component';
import { VideoPlayerComponent } from '../../components/video-player/video-player.component';
import { CommentsSectionComponent } from '../../components/comments-section/comments-section.component';

import { MediaService } from '../../../core/services/media-content.service';
import { UserService } from '../../../core/services/user.service';

import { MediaContentModel } from '../../../core/models/media-content.model';
import { CommentModel } from '../../../core/models/comment.model';

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

  media: MediaContentModel | null = null;
  comments: CommentModel[] = [];    // <--- добавлено: хранит комментарии
  loading = true;

  isTrailerOpen = false;
  isDescriptionExpanded = false;
  isAuthenticated = signal(false);

  constructor(
    public userService: UserService,
    private route: ActivatedRoute,
    private mediaService: MediaService,
    private router: Router,
    private sanitizer: DomSanitizer

  ) {
    effect(() => {
      this.isAuthenticated.set(this.userService.isAuthenticatedSignal());
    });
  }

  ngOnInit() {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (slug) {
      this.mediaService.getBySlug(slug).subscribe(data => {
        if (data) {
          this.media = data;
          this.loading = false;

          this.mediaService.getCommentsByMediaId(data.id).subscribe(comments => {
            this.comments = comments;
          });

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

  ngOnDestroy(): void {
    document.body.style.overflow = 'auto';
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
