export interface IEmployee {
  department?: string;
  idFilePath?: string;
  jobTitle?: string;
  housing?: number;
  nationality?: string;
  otherAllowance?: number;
  passportFilePath?: string;
  salary?: number;
  status?: boolean;
  transportation?: boolean;
  userId?: number;
  mobile?: number;
  attachments?: any;
  phone?: string;
  name?: string;
  firstname?: string;
  city?: string;
  country?: string;
  email?: string;
  position?: string;
  employeeNumber?: any;
  createAsUser?: boolean;
  address?:string;
  lastname?:string;
  totalAmount?:any
}

 export class Employee implements IEmployee {
   constructor() {}
   status?: boolean;
   department?: string;
   idFilePath?: string;
   jobTitle?: string;
   housing?: number;
   nationality?: string;
   otherAllowance?: number;
   passportFilePath?: string;
   salary?: number;
   transportation?: boolean;
   userId?: number;
   mobile?: number;
   attachments?: any;
   phone?: string;
   name?: string;
   firstname?: string;
   city?: string;
   country?: string;
   email?: string;
   position?: string;
   employeeNumber?: any;
   createAsUser?: boolean;
   address?: string;
   lastname?: string;
   totalAmount?: any;
 }
