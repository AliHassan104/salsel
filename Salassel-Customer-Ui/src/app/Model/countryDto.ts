export interface ICountry {
  id?: number;
  name?: string;
}

export class Country implements ICountry {
  constructor(public id?: number, public name?: string) {}
}
