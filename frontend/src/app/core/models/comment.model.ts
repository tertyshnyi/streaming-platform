import { UserModel } from "./user.model";

export interface CommentModel {
  id: number;
  parentId: number | null;
  mediaId: number;
  user: UserModel;
  createdAt: string;
  content: string;
  childrenComments: CommentModel[];
}
