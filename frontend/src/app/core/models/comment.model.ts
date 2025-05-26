export interface CommentModel {
  id: number;
  mediaContentId: number;
  parentCommentId: number | null;
  text: string;
  authorId: number;
  author: string;
  profileImg: string;
  createdAt: string;
  children: CommentModel[] | null;
  childrenCount: number | null;
}
