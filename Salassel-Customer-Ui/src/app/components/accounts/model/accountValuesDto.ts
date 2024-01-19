export interface IAccountData {
  accountNumber?: string;
  accountType?: string;
  customerName?: string;
  contactNumber?: string;
  address?: string;
  businessActivity?: string;
  projectName?: string;
  tradeLicenseNo?: string;
  taxDocumentNo?: string;
  county?: string;
  city?: string;
  custName?: string;
  billingPocName?: string;
  salesRegion?: string;
  salesAgentName?: string;
  accountUrl?: string;
  email?: string;
}

export class accounts implements IAccountData {
  constructor(
    public accountNumber?: string,
    public accountType?: string,
    public customerName?: string,
    public contactNumber?: string,
    public address?: string,
    public businessActivity?: string,
    public projectName?: string,
    public tradeLicenseNo?: string,
    public taxDocumentNo?: string,
    public county?: string,
    public city?: string,
    public custName?: string,
    public billingPocName?: string,
    public salesRegion?: string,
    public salesAgentName?: string
  ) {}
}
