import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { IDepartmentCategory } from "../../department-category/model/department-category.model";
import { DepartmentCategoryService } from "../../department-category/service/department-category.service";
import { DepartmentService } from "../../department/service/department.service";
import { ITicketCategory } from "../model/ticketCategoryDto";
import { TicketCategoryService } from "../service/ticket-category.service";
import { IDepartment } from "../../department/model/department.model";

@Component({
  selector: "app-ticket-category-form",
  templateUrl: "./ticket-category-form.component.html",
  styleUrls: ["./ticket-category-form.component.scss"],
  providers: [MessageService],
})
export class TicketCategoryFormComponent {
  ticketCategoryForm!: FormGroup;
  ticketCategory?: ITicketCategory;
  ticketCategoryId?: any;
  mode?: string = "Add";
  ticketCategoryName?: string;
  departmentCategories?: IDepartmentCategory[];
  department?: IDepartment[];

  constructor(
    private departmentCategoryService: DepartmentCategoryService,
    private ticketCategoryService: TicketCategoryService,
    private departmentService: DepartmentService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private formService: FormvalidationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.ticketCategoryForm = this.fb.group({
      name: [null, Validators.required],
      type: [null, Validators.required],
      department: [null, Validators.required],
    });

    this.route.queryParams.subscribe((params) => {
      this.ticketCategoryId = params["id"];
      if (this.ticketCategoryId) {
        this.mode = "Update";
        this.updateForm(this.ticketCategoryId);
      }
    });

    this.getAllDepartments();
  }

  onSubmit() {
    if (this.ticketCategoryForm && this.ticketCategoryForm.valid) {
      this.ticketCategory = this.createFromForm();
      this.ticketCategoryService
        .create(this.ticketCategory)
        .subscribe((res) => {
          if (res && res.body) {
            this.router.navigate(["ticket-category/list"]);
          }
        });
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.ticketCategoryForm);
    }
  }

  updateForm(id?: any) {
    this.getTicketCategoryById(id);
  }

  getTicketCategoryById(id?: any) {
    this.ticketCategoryService.getTicketCategoryById(id).subscribe((res) => {
      if (res && res.body) {
        this.ticketCategory = res.body;
        this.ticketCategoryName = this.ticketCategory.departmentCategory.name;
        this.patchDepartmentCategory(this.ticketCategory.departmentCategory.department.id)

        this.patchFormWithDto();
      }
    });
  }

  patchFormWithDto() {
    this.ticketCategoryForm.patchValue({
      name: this.ticketCategory.name,
      type: this.ticketCategory.departmentCategory,
      department: this.ticketCategory.departmentCategory.department,
    });
  }

  createFromForm() {
    const formValue = this.ticketCategoryForm.value;

    const ticketCategory: ITicketCategory = {
      id: this.ticketCategoryId ? this.ticketCategoryId : undefined,
      name: formValue.name,
      departmentCategory: {
        id: formValue.type.id,
        name: formValue.type.name,
      },
    };

    return ticketCategory;
  }

  getAllDepartments() {
    const params = { status: true };
    this.departmentService.getDepartments(params).subscribe((res) => {
      if (res && res.body) {
        this.department = res.body;
      }
    });
  }

  onSelectDepartment(data:any) {
    const department:IDepartment = data.value
    const departmentId = department.id
    
    this.departmentCategoryService
      .getDepartmentCategoryByDepartment(departmentId)
      .subscribe((res) => {
        if (res && res.body) {
          this.departmentCategories = res.body;
        }
      });
  }

  patchDepartmentCategory(id?:any){
    this.departmentCategoryService
      .getDepartmentCategoryByDepartment(id)
      .subscribe((res) => {
        if (res && res.body) {
          this.departmentCategories = res.body;
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
    this.router.navigate(["ticket-category/list"]);
  }
}
