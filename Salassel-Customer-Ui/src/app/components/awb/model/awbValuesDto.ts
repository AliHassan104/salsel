import { S } from "@fullcalendar/core/internal-common";

export interface IAwbDto {
  amount?: number;
  content?: string;
  currency?: string;
  deliveryAddress?: string;
  destinationCity?: string;
  destinationCountry?: string;
  dutyAndTaxesBillTo?: string;
  createdAt?: string;
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
  requestType?: string;
  serviceTypeCode?: string;
  accountNumber?: string;
  deliveryStreetName?: string;
  deliveryDistrict?: string;
  pickupStreetName?: string;
  pickupDistrict?: string;
  createdBy?: string;
  uniqueNumber?: string;
}
