export interface IAddressBook {
  address?: string;
  city?: string;
  contactNumber?: string;
  country?: string;
  createdAt?: Date;
  district?: string;
  id?: number;
  name?: string;
  refNumber?: string;
  status?: boolean;
  streetName?: string;
  uniqueId?: string;
  userType?: string;
  createdBy?:string
}

export class AddressBook implements IAddressBook {
  constructor(
    public address?: string,
    public city?: string,
    public contactNumber?: string,
    public country?: string,
    public createdAt?: Date,
    public district?: string,
    public id?: number,
    public name?: string,
    public refNumber?: string,
    public status?: boolean,
    public streetName?: string,
    public uniqueId?: string,
    public userType?: string,
    public createdBy?:string
  ) {}
}
