import { IProductFieldValuesDto } from "./productFieldValuesDto";

export interface IProductFieldDto {
    id?: number,
    created_at?: Date,
    name?: string,
    sequence?: number,
    status?: string,
    type?: string,
    productFieldValuesList?: IProductFieldValuesDto[]
  }

  export class ProductFieldDto implements IProductFieldDto {
    constructor(
        public id?: number,
        public created_at?: Date,
        public name?: string,
        public sequence?: number,
        public status?: string,
        public type?: string,
        public productFieldValuesList?: IProductFieldValuesDto[]
    ) {}
  }