import { IRole } from "src/app/components/permissions/model/roleDto";

export interface IUser {
  id?: number;
  email?: string;
  name?: string;
  password?: string;
  roles?: [IRole];
  status?: true;
}
