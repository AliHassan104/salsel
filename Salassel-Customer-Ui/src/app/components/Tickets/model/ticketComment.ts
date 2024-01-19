import { Ticket } from "./ticketValuesDto";

export interface TicketComment {
  id?: number;
  comment?: string;
  message?: string;
  ticket: Ticket;
}
