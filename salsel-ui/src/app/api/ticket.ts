export interface Ticket {
  assignedTo?: string;
  category?: string;
  createdAt?: Date;
  createdBy?: {
    id: 0;
    name?: "usman";
    password?: "asdasd";
    roles: [
      {
        id?: 0;
        name?: "sadasda";
        permissions?: [
          {
            id?: 0;
            name?: "eqweqw";
            value?: false;
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
  pickupDate?: string;
  pickupTime?: {
    hour?: "12";
    minute?: "12";
    nano?: 0;
    second?: "12";
  };
  recipientsContactNumber?: string;
  recipientsName?: string;
  shipperContactNumber?: string;
  shipperName?: string;
  shipperRefNumber?: string;
  status?: string;
  ticketFlag?: boolean;
}
