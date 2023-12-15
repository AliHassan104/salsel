export interface Ticket {
  assignedTo?: string;
  category?: string;
  createdAt?: string;
  deliveryAddress?: string;
  department?: string;
  departmentCategory?: string;
  destinationCity?: string;
  destinationCountry?: string;
  id?: number;
  originCity?: string;
  originCountry?: string;
  pickupAddress?: string;
  pickupDate?: string;
  pickupTime?: string;
  recipientContactNumber?: string;
  recipientName?: string;
  shipperContactNumber?: string;
  shipperName?: string;
  shipperRefNumber?: string;
  ticketStatus?: string;
  ticketFlag?: string;
  status?: true;
  createdBy?: string;
}