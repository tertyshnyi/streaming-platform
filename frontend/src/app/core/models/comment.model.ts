export interface CommentModel {
  id: number;
  parentId: number | null;
  profileImg: string;
  name: string;
  createdAt: string;
  content: string;
  childrenComments: CommentModel[];
}
