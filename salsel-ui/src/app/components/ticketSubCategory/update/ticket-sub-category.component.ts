import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { IDepartmentCategory } from "../../department-category/model/department-category.model";
import { DepartmentCategoryService } from "../../department-category/service/department-category.service";
import { IDepartment } from "../../department/model/department.model";
import { DepartmentService } from "../../department/service/department.service";
import { ITicketCategory } from "../../ticketCategory/model/ticketCategoryDto";
import { TicketCategoryService } from "../../ticketCategory/service/ticket-category.service";
import {
  ITicketSubCategory,
  TicketSubCategory,
} from "../model/ticketSubCategoryDeto";
import { TicketSubCategoryService } from "../service/ticket-sub-category.service";

@Component({
  selector: "app-ticket-sub-category",
  templateUrl: "./ticket-sub-category.component.html",
  styleUrls: ["./ticket-sub-category.component.scss"],
  providers: [MessageService],
})
export class TicketSubCategoryComponent {
  ticketSubCategoryForm!: FormGroup;
  ticketSubCategory?: ITicketSubCategory;
  ticketSubCategoryId?: any;
  mode?: string = "Add";
  ticketSubCategoryName?: string;
  departmentCategories?: IDepartmentCategory[];
  ticketCategories?: ITicketCategory[];
  department?: IDepartment[];

  constructor(
    private departmentCategoryService: DepartmentCategoryService,
    private ticketCategoryService: TicketCategoryService,
    private ticketSubCategoryService: TicketSubCategoryService,
    private departmentService: DepartmentService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private formService: FormvalidationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.ticketSubCategoryForm = this.fb.group({
      name: [null, Validators.required],
      type: [null, Validators.required],
      department: [null, Validators.required],
      ticketCategory: [null, Validators.required],
    });

    this.route.queryParams.subscribe((params) => {
      this.ticketSubCategoryId = params["id"];
      if (this.ticketSubCategoryId) {
        this.mode = "Update";
        this.updateForm(this.ticketSubCategoryId);
      }
    });

    this.getAllDepartments();
  }

  onSubmit() {
    if (this.ticketSubCategoryForm && this.ticketSubCategoryForm.valid) {
      this.ticketSubCategory = this.createFromForm();
      this.ticketSubCategoryService
        .create(this.ticketSubCategory)
        .subscribe((res) => {
          if (res && res.body) {
            this.router.navigate(["ticket-sub-category/list"]);
          }
        });
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.ticketSubCategoryForm);
    }
  }

  updateForm(id?: any) {
    this.getTicketCategoryById(id);
  }

  getTicketCategoryById(id?: any) {
    this.ticketSubCategoryService
      .getTicketSubCategoryById(id)
      .subscribe((res) => {
        if (res && res.body) {
          this.ticketSubCategory = res.body;
          this.ticketSubCategoryName =
            this.ticketSubCategory?.ticketCategory?.departmentCategory?.name;
          this.patchDepartmentCategoryAndTicketCategory(
            this.ticketSubCategory?.ticketCategory?.departmentCategory
              ?.department?.id,
            this.ticketSubCategory?.ticketCategory?.departmentCategory?.id
          );

          this.patchFormWithDto();
        }
      });
  }

  patchFormWithDto() {
    this.ticketSubCategoryForm.patchValue({
      name: this.ticketSubCategory?.name,
      type: this.ticketSubCategory?.ticketCategory?.departmentCategory,
      department:
        this.ticketSubCategory?.ticketCategory?.departmentCategory?.department,
      ticketCategory: this.ticketSubCategory?.ticketCategory,
    });
  }

  createFromForm() {
    const formValue = this.ticketSubCategoryForm.value;

    const TicketSubCategory: ITicketSubCategory = {
      id: this.ticketSubCategoryId ? this.ticketSubCategoryId : undefined,
      name: formValue.name,
      ticketCategory: {
        id: formValue.ticketCategory.id,
        name: formValue.ticketCategory.name,
      },
    };

    return TicketSubCategory;
  }

  getAllDepartments() {
    const params = { status: true };
    this.departmentService.getDepartments(params).subscribe((res) => {
      if (res && res.body) {
        this.department = res.body;
      }
    });
  }

  onSelectDepartment(data: any) {
    const department: IDepartment = data.value;
    const departmentId = department.id;

    this.departmentCategoryService
      .getDepartmentCategoryByDepartment(departmentId)
      .subscribe((res) => {
        if (res && res.body) {
          this.departmentCategories = res.body;
        }
      });
  }

  onSelectDepartmentCategory(data: any) {
    const departmentCategory: IDepartmentCategory = data.value;
    const departmentCategoryId = departmentCategory.id;

    this.ticketCategoryService
      .getTicketCategoryByDepartmentCaegory(departmentCategoryId)
      .subscribe((res) => {
        if (res && res.body) {
          this.ticketCategories = res.body;
        }
      });
  }

  patchDepartmentCategoryAndTicketCategory(
    departmentId?: any,
    departmentCategoryId?: any
  ) {
    this.departmentCategoryService
      .getDepartmentCategoryByDepartment(departmentId)
      .subscribe((res) => {
        if (res && res.body) {
          this.departmentCategories = res.body;
        }
      });

    this.ticketCategoryService
      .getTicketCategoryByDepartmentCaegory(departmentCategoryId)
      .subscribe((res) => {
        if (res && res.body) {
          this.ticketCategories = res.body;
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
    this.router.navigate(["ticket-sub-category/list"]);
  }
}
