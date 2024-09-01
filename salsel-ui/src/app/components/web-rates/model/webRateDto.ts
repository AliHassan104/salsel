export interface IAddressBook {
  fromCountry?: string;
  toCountry?: string;
  product?: string;
  weightRangeFrom?: number;
  weightRangeTo?: number;
  charges?: number;
  id?: number;
  additionalCharges?: number;
  status?: boolean;
}

export class AddressBook implements IAddressBook {
  constructor(
  public fromCountry?: string,
  public toCountry?: string,
  public product?: string,
  public weightRangeFrom?: number,
  public weightRangeTo?: number,
  public charges?: number,
  public id?: number,
  public additionalCharges?: number,
  public status?: boolean,
  ) {}
}
