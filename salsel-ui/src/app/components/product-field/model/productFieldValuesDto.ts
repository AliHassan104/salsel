export interface IProductFieldValuesDto {
    id?: number,
    name?: string,
    status?: string,
  }

  export class ProductFieldValuesDto implements IProductFieldValuesDto {
    constructor(
        public id?: number,
        public name?: string,
        public status?: string,
    ) {}
  }