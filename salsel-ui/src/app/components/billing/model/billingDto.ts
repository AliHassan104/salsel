export interface IBilling {
  invoiceDate?: any;
  customerAccountNumber?: string;
  airwayBillNo?: number;
  invoiceNo?: number;
  address?: string;
  country?: string;
  city?: string;
  taxNo?: number;
  taxInvoiceTo?: string;
  invoiceType?: string;
  customerRef?: number;
  product?: string;
  serviceDetails?: string;
  charges?: number;
  status?: boolean;
  billingStatus?: string;
  vatTax?: number;
  customerName?:string;
}

export class Billing implements IBilling {
  invoiceDate?: any;
  customerAccountNumber?: string;
  airwayBillNo?: number;
  invoiceNo?: number;
  address?: string;
  country?: string;
  city?: string;
  taxNo?: number;
  taxInvoiceTo?: string;
  invoiceType?: string;
  customerRef?: number;
  product?: string;
  serviceDetails?: string;
  charges?: number;
  status?: boolean;
  billingStatus?: string;
  vatTax?: number;
  customerName?: string;
}
