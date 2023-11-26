export interface Ticket {
  assignedTo?: string;
  category?: string;
  createdAt?: Date;
  createdBy?: {
    id: any;
    name?: string;
    password?: string;
    roles: [
      {
        id?: any;
        name?: string;
        permissions?: [
          {
            id?: any;
            name?: string;
            value?: boolean;
          }
        ];
      }
    ];
  };
  deliveryAddress?: string;
  department?: string;
  departmentCategory?: string;
  destinationCity?: string;
  destinationCountry?: string;
  id?: 0;
  originCity?: string;
  originCountry?: string;
  pickupAddress?: string;
  pickupDate?: Date;
  pickupTime?: {
    hour?: string;
    minute?: string;
    nano?: 0;
    second?: string;
  };
  recipientsContactNumber?: string;
  recipientsName?: string;
  shipperContactNumber?: string;
  shipperName?: string;
  shipperRefNumber?: string;
  status?: string;
  ticketFlag?: boolean;
}
