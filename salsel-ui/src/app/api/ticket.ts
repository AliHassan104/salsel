export interface Ticket {
  assignedTo?: string;
  category?: string;
  createdAt?: string;
  deliveryAddress?: string;
  department?: string;
  departmentCategory?: string;
  destinationCity?: string;
  destinationCountry?: string;
  id?: 0;
  originCity?: string;
  originCountry?: string;
  pickupAddress?: string;
  pickupDate?: string;
  pickupTime?: string;
  recipientsContactNumber?: string;
  recipientsName?: string;
  shipperContactNumber?: string;
  shipperName?: string;
  shipperRefNumber?: string;
  status?: string;
  ticketFlag?: boolean;
}
