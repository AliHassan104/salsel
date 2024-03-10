import { ICountry } from "../../country/model/countryDto";


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
