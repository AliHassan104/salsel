import { IRole } from "src/app/components/permissions/model/roleDto";

export interface IUser {
  id?: number;
  name?: string;
  password?: string;
  roles?: [IRole];
}
