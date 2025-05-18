import { Component, ElementRef, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DomSanitizer } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import Plyr from 'plyr';

import { SparklesComponent } from '../../shared/components/sparkles/sparkles.component';
import { MediaService } from '../../core/services/media-content.service';
import { MediaContentModel } from '../../core/models/media-content.model';
import { CommentModel } from '../../core/models/comment.model';

@Component({
  selector: 'app-media-content',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SparklesComponent
  ],
  standalone: true,
  templateUrl: './media-content.component.html',
  styleUrls: ['./media-content.component.scss']
})
export class MediaContentComponent implements OnInit, OnDestroy {

  comments: CommentModel[] = [
    {
      id: 1,
      parentId: null,
      profileImg: '/images/404-captain.png',
      name: 'DoctorNIFI',
      createdAt: '49 хвилин тому',
      content: 'хуйня ебана',
      childrenComments: [
        {
          id: 2,
          parentId: 1,
          profileImg: '/images/404-captain.png',
          name: 'Moderator',
          createdAt: '40 хвилин тому',
          content: 'Будь ласка, дотримуйтесь правил спільноти.',
          childrenComments: []
        }
      ]
    },
    {
      id: 3,
      parentId: null,
      profileImg: '/images/404-captain.png',
      name: 'Darka_Tsvihunova',
      createdAt: '1 місяць тому',
      content: 'Це просто неймовірно. Прекрасне естетичне та атмосферне аніме.',
      childrenComments: []
    }
  ];

  newCommentInput: string = '';
  isNewCommentActive: boolean = false;

  sendNewComment(): void {
    const content = this.newCommentInput.trim();
    if (!content) return;

    const newComment: CommentModel = {
      id: Date.now(),
      parentId: null,
      profileImg: '/images/404-vdova.png',
      name: 'You',
      createdAt: 'щойно',
      content: content,
      childrenComments: []
    };

    this.comments.push(newComment);
    this.newCommentInput = '';
    this.isNewCommentActive = false;
  }

  cancelNewComment(): void {
    this.newCommentInput = '';
    this.isNewCommentActive = false;
  }

  onNewCommentInputBlur(): void {
    setTimeout(() => {
      if (!this.newCommentInput) {
        this.isNewCommentActive = false;
      }
    }, 150);
  }

  repliesVisibility: { [key: number]: boolean } = {};
  replyVisibility: { [key: number]: boolean } = {};
  replyInputs: { [key: number]: string } = {};
  replyRecipients: { [key: number]: string } = {};

  toggleReplies(commentId: number): void {
    this.repliesVisibility[commentId] = !this.repliesVisibility[commentId];
  }

  isRepliesVisible(commentId: number): boolean {
    return !!this.repliesVisibility[commentId];
  }

  hasReplies(comment: CommentModel): boolean {
    return comment.childrenComments?.length > 0;
  }

  toggleReplyField(commentId: number, recipientName: string = ''): void {
    if (this.replyVisibility[commentId]) {
      delete this.replyVisibility[commentId];
      delete this.replyRecipients[commentId];
    } else {
      this.replyVisibility[commentId] = true;
      this.replyRecipients[commentId] = recipientName;
    }
  }

  isReplyFieldVisible(commentId: number): boolean {
    return !!this.replyVisibility[commentId];
  }

  sendReply(parentComment: CommentModel): void {
    const replyTextRaw = this.replyInputs[parentComment.id]?.trim();
    if (!replyTextRaw) return;

    const newReply: CommentModel = {
      id: Date.now(),
      parentId: parentComment.id,
      profileImg: '/images/404-vdova.png',
      name: 'You',
      createdAt: 'щойно',
      content: replyTextRaw,
      childrenComments: []
    };

    if (!parentComment.childrenComments) {
      parentComment.childrenComments = [];
    }
    parentComment.childrenComments.push(newReply);

    this.replyInputs[parentComment.id] = '';
    this.cancelReply(parentComment.id);
  }

  cancelReply(commentId: number): void {
    this.replyInputs[commentId] = '';
    delete this.replyVisibility[commentId];
    delete this.replyRecipients[commentId];
  }

  getAllDescendants(comment: CommentModel): CommentModel[] {
    let descendants: CommentModel[] = [];
    if (comment.childrenComments && comment.childrenComments.length > 0) {
      comment.childrenComments.forEach(child => {
        descendants.push(child);
        descendants.push(...this.getAllDescendants(child));
      });
    }
    return descendants;
  }

  findParentName(parentId: number | null): string {
    if (!parentId) return '';
    let found = this.findCommentById(parentId, this.comments);
    return found ? found.name : '';
  }

  findCommentById(id: number, commentsList: CommentModel[]): CommentModel | null {
    for (let comment of commentsList) {
      if (comment.id === id) return comment;
      let foundInChildren = this.findCommentById(id, comment.childrenComments || []);
      if (foundInChildren) return foundInChildren;
    }
    return null;
  }

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
