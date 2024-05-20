import { Component, Input } from "@angular/core";

@Component({
  selector: "app-invoice-excel-data",
  templateUrl: "./invoice-excel-data.component.html",
  styleUrls: ["./invoice-excel-data.component.scss"],
})
export class InvoiceExcelDataComponent {
  @Input() excelData: any[][]; // Input property to receive the Excel data from the parent component
  @Input() tableHeaders: string[]; // Input property to receive the table headers from the parent component
}
