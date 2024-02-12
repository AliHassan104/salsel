import { IDepartmentCategory } from "../../department-category/model/department-category.model";

export interface ITicketCategory {
  id?: number;
  name?: string;
  status?: string;
  departmentCategory?: IDepartmentCategory;
}

export class TicketCategory implements ITicketCategory {
  constructor(
    public id?: number,
    public name?: string,
    public status?: string,
    public departmentCategory?: IDepartmentCategory
  ) {}
}