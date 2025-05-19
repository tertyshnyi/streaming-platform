import {
  Component,
  Input
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {
  trigger,
  state,
  style,
  transition,
  animate
} from '@angular/animations';

import { CommentModel } from '../../../core/models/comment.model';
import { UserModel } from '../../../core/models/user.model';

@Component({
  selector: 'app-comments-section',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './comments-section.component.html',
  styleUrls: ['./comments-section.component.scss'],
  animations: [
    trigger('expandCollapse', [
      state('expanded', style({
        height: '*',
        opacity: 1,
        overflow: 'visible',
      })),
      state('collapsed', style({
        height: '0px',
        opacity: 0,
        overflow: 'hidden',
        padding: '0px',
        margin: '0px',
      })),
      transition('expanded <=> collapsed', animate('300ms ease')),
    ])
  ]
})
export class CommentsSectionComponent {
  @Input() isAuthenticated: boolean = false;
  @Input() currentUser: UserModel | null = null;
  @Input() comments: CommentModel[] = [];

  newCommentInput = '';
  isNewCommentActive = false;

  replyInputs: { [key: number]: string } = {};
  replyRecipients: { [key: number]: string } = {};
  replyVisibility: { [key: number]: boolean } = {};
  repliesVisibility: { [key: number]: boolean } = {};

  sendNewComment(): void {
    const content = this.newCommentInput.trim();
    if (!this.isAuthenticated || !this.currentUser || content.length < 2 || content.length > 300) return;

    const newComment: CommentModel = {
      id: Date.now(),
      parentId: null,
      mediaId: 0,
      user: this.currentUser,
      createdAt: new Date().toISOString(),
      content,
      childrenComments: []
    };

    this.comments.unshift(newComment);
    this.newCommentInput = '';
    this.isNewCommentActive = false;
  }

  sendReply(parentComment: CommentModel): void {
    const replyTextRaw = this.replyInputs[parentComment.id]?.trim();
    if (!this.isAuthenticated || !this.currentUser || !replyTextRaw || replyTextRaw.length < 2 || replyTextRaw.length > 300) return;

    const newReply: CommentModel = {
      id: Date.now(),
      parentId: parentComment.id,
      mediaId: parentComment.mediaId,
      user: this.currentUser,
      createdAt: new Date().toISOString(),
      content: replyTextRaw,
      childrenComments: []
    };

    parentComment.childrenComments = parentComment.childrenComments || [];
    parentComment.childrenComments.push(newReply);

    this.replyInputs[parentComment.id] = '';
    this.cancelReply(parentComment.id);
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
    return found ? found.user.name : '';
  }

  findCommentById(id: number, commentsList: CommentModel[]): CommentModel | null {
    for (let comment of commentsList) {
      if (comment.id === id) return comment;
      let foundInChildren = this.findCommentById(id, comment.childrenComments || []);
      if (foundInChildren) return foundInChildren;
    }
    return null;
  }

  getRelativeTime(dateStr: string): string {
    const now = new Date();
    const date = new Date(dateStr);
    const diff = (now.getTime() - date.getTime()) / 1000;

    if (diff < 60) return 'just now';

    const minutes = Math.floor(diff / 60);
    if (minutes < 60) return `${minutes} ${this.pluralize(minutes, 'minute')} ago`;

    const hours = Math.floor(diff / 3600);
    if (hours < 24) return `${hours} ${this.pluralize(hours, 'hour')} ago`;

    const days = Math.floor(diff / 86400);
    if (days < 7) return `${days} ${this.pluralize(days, 'day')} ago`;

    const weeks = Math.floor(days / 7);
    return `${weeks} ${this.pluralize(weeks, 'week')} ago`;
  }

  pluralize(count: number, word: string): string {
    return count === 1 ? word : word + 's';
  }
}
