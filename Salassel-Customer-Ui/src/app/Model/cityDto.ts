import { ICountry } from "./countryDto";
import { IDepartment } from "./department.model";

export interface ICityDto {
  id?: number;
  name?: string;
  country?: ICountry;
}

export class City implements ICityDto {
  constructor(
     id?: number,
  name?: string,
  country?: ICountry,
  ) {}
}
