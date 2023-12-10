
export interface IDepartment {
    id?: number,
    name?: string,
    status?: string,
  }

  export class Department implements IDepartment {
    constructor(
        public id?: number,
        public name?: string,
        public status?: string,
    ) {}
  }