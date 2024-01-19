import { IRole } from "src/app/Model/roleDto";

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
