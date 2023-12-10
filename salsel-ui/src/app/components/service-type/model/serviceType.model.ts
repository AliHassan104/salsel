import { IProductType } from "../../product-type/model/productType.model";

export interface IServiceType {
  id?: number;
  name?: string;
  status?: string;
  code?: string;
  productType?: IProductType;
}

export class IServiceType implements IServiceType {
  constructor(
    public id?: number,
    public name?: string,
    public status?: string,
    public code?: string,
    public productType?: IProductType
  ) {}
}
