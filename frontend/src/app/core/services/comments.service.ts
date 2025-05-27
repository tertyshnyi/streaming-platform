import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { CommentModel } from '../models/comment.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CommentsService {
  private apiUrl = `${environment.beUrl}/comments`;

  constructor(private http: HttpClient) {}

  addComment(comment: {
    mediaContentId: number;
    text: string;
    parentCommentId: number | null;
  }): Observable<CommentModel> {
    return this.http.post<CommentModel>(`${this.apiUrl}`, comment);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentId}`);
  }


  getCommentsByMediaId(mediaContentId: number): Observable<CommentModel[]> {
    return this.http
      .get<CommentModel[]>(`${this.apiUrl}/media/${mediaContentId}`)
      .pipe(map(comments => this.buildCommentTree(comments)));
  }


  private buildCommentTree(comments: CommentModel[]): CommentModel[] {
    const map = new Map<number, CommentModel>();
    const roots: CommentModel[] = [];

    comments.forEach((comment) => {
      comment.children = [];
      map.set(comment.id, comment);
    });

    comments.forEach((comment) => {
      if (comment.parentCommentId === null) {
        roots.push(comment);
      } else {
        const parent = map.get(comment.parentCommentId);
        if (parent) {
          parent.children = parent.children || [];
          parent.children.push(comment);
        }
      }
    });

    const fixChildren = (comments: CommentModel[]) => {
      comments.forEach((comment) => {
        if (!comment.children || comment.children.length === 0) {
          comment.children = null;
        } else {
          fixChildren(comment.children);
        }
      });
    };

    fixChildren(roots);

    return roots;
  }
}
