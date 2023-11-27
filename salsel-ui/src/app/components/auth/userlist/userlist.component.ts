import { Component } from "@angular/core";
import { FormGroup } from "@angular/forms";
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
    private messageService: MessageService
  ) {}

  ngOnInit() {}

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

  //   confirmDeleteSelected() {
  //     this.deleteProductsDialog = false;
  //     this.users = this.users.filter(
  //       (val) => !this.selectedProducts.includes(val)
  //     );
  //     this.messageService.add({
  //       severity: "success",
  //       summary: "Successful",
  //       detail: "Products Deleted",
  //       life: 3000,
  //     });
  //     this.selectedProducts = [];
  //   }

  //   confirmDelete() {
  //     this.deleteProductDialog = false;
  //     this.products = this.products.filter((val) => val.id !== this.product.id);
  //     this.messageService.add({
  //       severity: "success",
  //       summary: "Successful",
  //       detail: "Product Deleted",
  //       life: 3000,
  //     });
  //     this.product = {};
  //   }

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
}
