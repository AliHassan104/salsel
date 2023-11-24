export interface ShipperDetails {
  shipperName?: string;
  shipperContact?: number;
  pickUpAddress?: string;
  shipperRef?: number;
  originCountry?: string;
  originCity?: string;
}

export interface RecipientDetails {
  recipientName?: string;
  recipientContact?: number;
  destinationCountry?: string;
  destinationCity?: string;
  deliveryAddress?: string;
}

export interface AmountCurrency {
  currency?: string;
  amount?: number;
}

export interface ShipmentDetails {
  productType?: string;
  serviceType?: string;
  content?: string;
  weight?: string;
  amountCurrency?: AmountCurrency;
  dutyTaxBillingTo?: string;
  pieces?: number;
}

export interface TicketData {
  _id?: string;
  pickupDateTime?: Date;
  shipperDetails?: ShipperDetails;
  recipientDetails?: RecipientDetails;
  shipmentDetails?: ShipmentDetails;
}
