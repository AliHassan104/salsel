import { IRole } from "src/app/components/permissions/model/roleDto";

export interface IUser {
  id?: number;
  email?: string;
  name?: string;
  firstname?: string;
  lastname?: string;
  phone?: string;
  employeeId: string;
  password?: string;
  roles?: [IRole];
  status?: true;
}
