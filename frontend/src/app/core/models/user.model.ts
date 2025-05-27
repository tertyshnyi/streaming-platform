export interface UserModel {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
  profileImg?: string;
  createdAt: string;
  authorities: string[];
}
