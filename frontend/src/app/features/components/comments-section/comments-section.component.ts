import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {
  trigger,
  state,
  style,
  transition,
  animate,
} from '@angular/animations';

import { CommentsService } from '../../../core/services/comments.service';
import { CommentModel } from '../../../core/models/comment.model';
import { UserModel } from '../../../core/models/user.model';
import { RelativeTimePipe } from '../../../core/pipes/relative-time.pipe';

@Component({
  selector: 'app-comments-section',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    RelativeTimePipe
  ],
  templateUrl: './comments-section.component.html',
  styleUrls: ['./comments-section.component.scss'],
  animations: [
    trigger('expandCollapse', [
      state(
        'expanded',
        style({
          height: '*',
          opacity: 1,
          overflow: 'visible',
        })
      ),
      state(
        'collapsed',
        style({
          height: '0px',
          opacity: 0,
          overflow: 'hidden',
          padding: '0px',
          margin: '0px',
        })
      ),
      transition('expanded <=> collapsed', animate('300ms ease')),
    ]),
  ],
})
export class CommentsSectionComponent implements OnInit, OnChanges {
  @Input() isAuthenticated = false;
  @Input() currentUser: UserModel | null = null;
  @Input() mediaId?: number;
  @Input() comments: CommentModel[] = [];

  newCommentInput = '';
  isNewCommentActive = false;

  replyInputs: { [key: number]: string } = {};
  replyRecipients: { [key: number]: string } = {};
  replyVisibility: { [key: number]: boolean } = {};
  repliesVisibility: { [key: number]: boolean } = {};

  constructor(private commentsService: CommentsService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mediaId']?.currentValue != null) {
      this.loadComments(changes['mediaId'].currentValue);
    }
  }

  ngOnInit(): void {
    if (this.mediaId != null) {
      this.loadComments(this.mediaId);
    }
  }

  loadComments(mediaId: number): void {
    this.commentsService.getCommentsByMediaId(mediaId).subscribe({
      next: (comments) => (this.comments = comments),
      error: (err) => console.error('Error loading comments:', err),
    });
  }

  sendNewComment(): void {
    const content = this.newCommentInput.trim();

    if (
      !this.isAuthenticated ||
      !this.currentUser ||
      content.length < 2 ||
      content.length > 300 ||
      this.mediaId === undefined
    ) return;

    const newCommentPayload = {
      mediaContentId: this.mediaId,
      text: content,
      parentCommentId: null,
    };

    this.commentsService.addComment(newCommentPayload).subscribe({
      next: (savedComment) => {
        this.comments = [savedComment, ...this.comments];
        this.newCommentInput = '';
        this.isNewCommentActive = false;
      },
      error: (err) => {
        console.error('ОError to add comment:', err);
      }
    });
  }

  sendReply(parentComment: CommentModel): void {
    const replyTextRaw = this.replyInputs[parentComment.id]?.trim();

    if (
      !this.isAuthenticated ||
      !this.currentUser ||
      !replyTextRaw ||
      replyTextRaw.length < 2 ||
      replyTextRaw.length > 300 ||
      this.mediaId === undefined
    ) return;

    const newReplyPayload = {
      mediaContentId: this.mediaId,
      text: replyTextRaw,
      parentCommentId: parentComment.id,
    };

    this.commentsService.addComment(newReplyPayload).subscribe({
      next: (savedReply) => {
        if (!parentComment.children) {
          parentComment.children = [];
        }
        parentComment.children.push(savedReply);
        parentComment.childrenCount = (parentComment.childrenCount || 0) + 1;
        this.replyInputs[parentComment.id] = '';
        this.cancelReply(parentComment.id);
      },
      error: (err) => {
        console.error('Error to add answer:', err);
      }
    });
  }

  private removeCommentById(commentId: number): void {
    const recursiveRemove = (comments: CommentModel[]): CommentModel[] => {
      return comments
        .filter(comment => comment.id !== commentId)
        .map(comment => ({
          ...comment,
          children: comment.children ? recursiveRemove(comment.children) : null,
        }));
    };

    this.comments = recursiveRemove(this.comments);
  }

  deleteComment(commentId: number): void {
    this.commentsService.deleteComment(commentId).subscribe({
      next: () => {
        this.removeCommentById(commentId);
      },
      error: (err) => {
        console.error('Error to remove comment:', err);
      }
    });
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

  onAnswerClick(commentId: number, recipientName: string): void {
    if (!this.isAuthenticated) {
      alert('You must be logged in to reply.');
      return;
    }
    this.toggleReplyField(commentId, recipientName);
  }

  toggleReplies(commentId: number): void {
    this.repliesVisibility[commentId] = !this.repliesVisibility[commentId];
  }

  isRepliesVisible(commentId: number): boolean {
    return !!this.repliesVisibility[commentId];
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

  cancelReply(commentId: number): void {
    this.replyInputs[commentId] = '';
    delete this.replyVisibility[commentId];
    delete this.replyRecipients[commentId];
  }

  pluralize(count: number, word: string): string {
    return count === 1 ? word : word + 's';
  }

  getAllDescendants(comment: CommentModel): CommentModel[] {
    const descendants: CommentModel[] = [];
    const collectChildren = (comments: CommentModel[] | null) => {
      if (!comments) return;
      comments.forEach(c => {
        descendants.push(c);
        collectChildren(c.children);
      });
    };
    collectChildren(comment.children);
    return descendants;
  }

  findParentName(parentId: number | null): string {
    if (parentId === null) return '';
    const findInComments = (comments: CommentModel[]): CommentModel | null => {
      for (const c of comments) {
        if (c.id === parentId) return c;
        if (c.children) {
          const found = findInComments(c.children);
          if (found) return found;
        }
      }
      return null;
    };
    const parentComment = findInComments(this.comments);
    return parentComment ? parentComment.author : 'Unknown';
  }

  getUserAvatar(comment: { profileImg?: string } | UserModel | null): string {
    return comment?.profileImg || '/images/404-vdova.png';
  }
}
