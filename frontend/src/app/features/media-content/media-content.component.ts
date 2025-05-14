import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DomSanitizer } from '@angular/platform-browser';

import { SparklesComponent } from '../../shared/components/sparkles/sparkles.component';

import { MediaService } from '../../core/services/media-content.service';

import { MediaContentModel } from '../../core/models/media-content.model';

@Component({
  selector: 'app-media-content',
  imports: [
    CommonModule,
    SparklesComponent
  ],
  standalone: true,
  templateUrl: './media-content.component.html',
  styleUrls: ['./media-content.component.scss']
})
export class MediaContentComponent implements OnInit {
  media: MediaContentModel | null = null;
  loading = true;

  isTrailerOpen = false;

  constructor(
    private route: ActivatedRoute,
    private mediaService: MediaService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (slug) {
      this.mediaService.getBySlug(slug).subscribe((data) => {
        if (data) {
          this.media = data;
        } else {
          // Перенаправление с заменой URL в истории
          this.router.navigateByUrl('/404', { replaceUrl: true });
        }
        this.loading = false;
      });
    } else {
      this.router.navigateByUrl('/404', { replaceUrl: true });
      this.loading = false;
    }
  }

  openTrailer() {
    this.isTrailerOpen = true;
    document.body.style.overflow = 'hidden';
  }

  closeTrailer() {
    this.isTrailerOpen = false;
    document.body.style.overflow = 'auto';
  }

  getYouTubeEmbedUrl(url: string | undefined): any {
    if (!url) {
      return '';
    }
    const videoId = url.split('v=')[1];
    const embedUrl = `https://www.youtube.com/embed/${videoId}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  }

  ngOnDestroy(): void {
    document.body.style.overflow = 'auto';
  }
}
