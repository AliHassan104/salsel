import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { IDepartment } from "src/app/components/department/model/department.model";
import { DepartmentService } from "../../department/service/department.service";
import { IProductType } from "src/app/components/product-type/model/productType.model";
import { ProductTypeService } from "../service/product-type.service";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";

@Component({
  selector: "app-product-type",
  templateUrl: "./product-type.component.html",
  styleUrls: ["./product-type.component.scss"],
  providers: [MessageService],
})
export class ProductTypeComponent {
  productTypeForm!: FormGroup;
  productType?: IProductType;
  id?: any;
  mode?: string = "Add";

  constructor(
    private productTypeService: ProductTypeService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private messageService: MessageService,
    private formService: FormvalidationService
  ) {}

  ngOnInit(): void {
    this.productTypeForm = this.fb.group({
      name: [null, Validators.required],
      code: [null, Validators.required],
    });

    this.route.queryParams.subscribe((params) => {
      this.id = params["id"];
      if (this.id) {
        this.mode = "Update";
        this.updateForm(this.id);
      }
    });
  }

  onSubmit() {
    if (this.productTypeForm && this.productTypeForm.valid) {
      this.productType = this.createFromForm();
      this.productTypeService
        .addProductType(this.productType)
        .subscribe((res) => {
          if (res && res.body) {
            this.router.navigate(["product-type/list"]);
          }
        });
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.productTypeForm);
    }
  }

  updateForm(id?: any) {
    this.getDepartmentById(id);
  }

  getDepartmentById(id?: any) {
    this.productTypeService.getProductTypeById(id).subscribe((res) => {
      if (res && res.body) {
        this.productType = res.body;
        this.patchFormWithDto();
      }
    });
  }

  patchFormWithDto() {
    this.productTypeForm.patchValue({
      name: this.productType.name,
      code: this.productType.code,
    });
  }

  createFromForm() {
    const formValue = this.productTypeForm.value;

    const productType: IProductType = {
      id: this.id ? this.id : undefined,
      name: formValue.name,
      code: formValue.code,
    };

    return productType;
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  onCancel() {
    this.router.navigate(["product-type/list"]);
  }
}
