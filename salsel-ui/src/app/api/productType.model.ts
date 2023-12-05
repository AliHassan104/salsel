
export interface IProductType {
    id?: number,
    name?: string,
    status?: string,
    code?: string
  }

  export class ProductType implements IProductType {
    constructor(
        public id?: number,
        public name?: string,
        public status?: string,
        public code?: string
    ) {}
  }