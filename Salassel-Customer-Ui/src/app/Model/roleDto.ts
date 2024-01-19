import { IPermissions } from "./permissionDto";

export interface IRole {
  id?: number;
  name?: string;
  permissions?: [IPermissions];
}
