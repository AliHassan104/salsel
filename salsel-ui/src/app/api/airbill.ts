export interface Airbill {
  amount?: number;
  content?: string;
  currency?: string;
  deliveryAddress?: string;
  destinationCity?: string;
  destinationCountry?: string;
  dutyAndTaxesBillTo?: string;
  id?: number;
  originCity?: string;
  originCountry?: string;
  pickupAddress?: string;
  pickupDate?: string;
  pickupTime?: string;
  pieces?: number;
  productType?: string;
  recipientsContactNumber?: string;
  recipientsName?: string;
  serviceType?: string;
  shipperContactNumber?: string;
  shipperName?: string;
  shipperRefNumber?: string;
  status?: boolean;
  weight?: number;
}
