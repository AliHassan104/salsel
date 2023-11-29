import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MessageService } from "primeng/api";
import { Table } from "primeng/table";
import { Product } from "src/app/api/product";
import { User } from "src/app/api/user";
import { AuthService } from "../auth.service";

@Component({
  selector: "app-userlist",
  templateUrl: "./userlist.component.html",
  styleUrls: ["./userlist.component.scss"],
})
export class UserlistComponent {
  userData!: FormGroup;
  productDialog: boolean = false;

  deleteProductDialog: boolean = false;

  deleteProductsDialog: boolean = false;

  getTagSeverity(status: string): string {
    switch (status) {
      case "Active":
        return "success";
      case "Inactive":
        return "danger";
      case "Unassigned":
        return "warning";
      default:
        return ""; // Handle other cases as needed
    }
  }

  users: User[] = [
    {
      id: 1,
      name: "Usman Aslam",
      username: "usmanaslam138",
      email: "usman138@gmail.com",
      password: "123456",
      role: "admin",
      status: "Active",
    },
    {
      id: 2,
      name: "Hamza Khan",
      username: "hamza123",
      email: "hamza122@gmail.com",
      password: "123456",
      role: "user",
      status: "Inactive",
    },
    {
      id: 3,
      name: "Ali Raza",
      username: "ali123",
      email: "ali122@gmail.com",
      password: "123456",
      role: "user",
      status: "Unassigned",
    },
  ];

  product: Product = {};

  selectedProducts: Product[] = [];

  submitted: boolean = false;

  cols: any[] = [];

  statuses: any[] = [];

  rowsPerPageOptions = [5, 10, 20];

  permissions = ["Dashboard", "All Tickets", "Add Ticket", "UserList"];

  constructor(
    private authService: AuthService,
    private messageService: MessageService,
    private fb: FormBuilder
  ) {}

  openNew() {
    // this.product = {};
    this.submitted = false;
    this.productDialog = true;
  }

  deleteSelectedProducts() {
    this.deleteProductsDialog = true;
  }

  editProduct(product: Product) {
    // this.product = { ...product };
    this.productDialog = true;
  }

  deleteProduct(product: Product) {
    this.deleteProductDialog = true;
    // this.product = { ...product };
  }

  hideDialog() {
    this.productDialog = false;
    this.submitted = false;
  }

  createId(): string {
    let id = "";
    const chars =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (let i = 0; i < 5; i++) {
      id += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return id;
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  getData(event) {
    console.log("get data", event);
  }

  form: FormGroup;

  categories: string[] = ["Electronics", "Clothing"];
  subcategories: { [key: string]: string[] } = {
    Electronics: ["Laptop", "Smartphone"],
    Clothing: ["Shirt", "Pants"],
  };

  ngOnInit() {
    this.form = this.fb.group({
      category: ["", Validators.required],
      subcategory: ["", Validators.required],
    });
  }

  // Method to update subcategories based on the selected category
  updateSubcategories() {
    const category = this.form.get("category").value;
    const subcategoryControl = this.form.get("subcategory");

    // Clear the existing subcategory value
    subcategoryControl.setValue("");

    // Update available subcategories based on the selected category
    const availableSubcategories = this.subcategories[category] || [];

    // Update the pattern for the subcategory validator
    subcategoryControl.setValidators([
      Validators.required,
      Validators.pattern(this.getSubcategoryPattern()),
    ]);
    subcategoryControl.updateValueAndValidity();
  }

  // Method to generate the pattern for the subcategory validator
  getSubcategoryPattern(): string {
    const subcategories = [].concat(...Object.values(this.subcategories));
    return `^(${subcategories.join("|")})$`;
  }
}
