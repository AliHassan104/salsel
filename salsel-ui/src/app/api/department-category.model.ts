import { IDepartment } from "./department.model";

export interface IDepartmentCategory {
    id?: number,
    name?: string,
    status?: string,
    department?: IDepartment
  }

  export class DepartmentCategory implements IDepartmentCategory {
    constructor(
        public id?: number,
        public name?: string,
        public status?: string,
        public department?: IDepartment
    ) {}
  }