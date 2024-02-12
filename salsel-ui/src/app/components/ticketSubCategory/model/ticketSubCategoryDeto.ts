import { ITicketCategory } from "../../ticketCategory/model/ticketCategoryDto";

export interface ITicketSubCategory {
  id?: number;
  name?: string;
  status?: string;
  ticketCategory?: ITicketCategory;
}

export class TicketSubCategory implements ITicketSubCategory {
  constructor(
    public id?: number,
    public name?: string,
    public status?: string,
    public ticketCategory?: ITicketCategory
  ) {}
}
