export interface IEmployee {
    department?: string;
    idFilePath?:string;
    jobTitle?:string;
    housing?:number;
    nationality?:string;
    otherAllowance?:number;
    passportFilePath?:string;
    salary?:number;
    status?:boolean;
    transportation?:boolean;
    userId?:number;
    mobile?:number;
    attachments?:any;
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
 }
