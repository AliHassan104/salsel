import { Component } from "@angular/core";
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { ServiceTypeService } from "../service/service-type.service";
import { IServiceType } from "src/app/components/service-type/model/serviceType.model";
import { ProductTypeService } from "../../product-type/service/product-type.service";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";

@Component({
  selector: "app-service-type",
  templateUrl: "./service-type.component.html",
  styleUrls: ["./service-type.component.scss"],
  providers: [MessageService],
})
export class ServiceTypeComponent {
  serviceTypeForm!: FormGroup;
  serviceType?: IServiceType;
  id?: any;
  mode?: string = "Add";
  productTypes?: IServiceType[];

  constructor(
    private serviceTypeService: ServiceTypeService,
    private productTypeService: ProductTypeService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private messageService: MessageService,
    private formService: FormvalidationService
  ) {}

  ngOnInit(): void {
    this.serviceTypeForm = this.fb.group({
      name: [null, Validators.required],
      code: [null, Validators.required],
      type: [null, Validators.required],
    });

    this.route.queryParams.subscribe((params) => {
      this.id = params["id"];
      if (this.id) {
        this.mode = "Update";
        this.updateForm(this.id);
      }
    });

    this.getAllProductTypes();
  }

  onSubmit() {
    if (this.serviceTypeForm && this.serviceTypeForm.valid) {
      this.serviceType = this.createFromForm();
      this.serviceTypeService
        .addServiceType(this.serviceType)
        .subscribe((res) => {
          if (res && res.body) {
            this.router.navigate(["service-type/list"]);
          }
        });
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.serviceTypeForm);
    }
  }

  updateForm(id?: any) {
    this.getDepartmentCategoryById(id);
  }

  getDepartmentCategoryById(id?: any) {
    this.serviceTypeService.getServiceTypeById(id).subscribe((res) => {
      if (res && res.body) {
        this.serviceType = res.body;
        this.patchFormWithDto();
      }
    });
  }

  patchFormWithDto() {
    this.serviceTypeForm.setValue({
      name: this.serviceType.name,
      code: this.serviceType.code,
      type: this.serviceType.productType,
    });
  }

  createFromForm() {
    const formValue = this.serviceTypeForm.value;

    const serviceType: IServiceType = {
      id: this.id ? this.id : undefined,
      name: formValue.name,
      code: formValue.code,
      productType: {
        id: formValue.type.id,
        name: formValue.type.name,
      },
    };

    return serviceType;
  }

  getAllProductTypes() {
    this.productTypeService
      .getProductTypes({ status: true })
      .subscribe((res) => {
        if (res && res.body) {
          this.productTypes = res.body;
        }
      });
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  onCancel() {
    this.router.navigate(["service-type/list"]);
  }
}
