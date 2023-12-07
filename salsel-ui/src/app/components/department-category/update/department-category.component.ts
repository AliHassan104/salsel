import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IDepartment } from 'src/app/api/department.model';
import { DepartmentService } from '../../department/service/department.service';
import { IDepartmentCategory } from 'src/app/api/department-category.model';
import { DepartmentCategoryService } from '../service/department-category.service';


@Component({
  selector: 'app-department-category',
  templateUrl: './department-category.component.html',
  styleUrls: ['./department-category.component.scss']
})
export class DepartmentCategoryComponent {
  departmentCategoryForm!: FormGroup;
  departmentCategory?: IDepartmentCategory;
  departmentCategoryId?: any;
  mode?: string = 'Create'
  departmentCategoryName?: string;
  departments?: IDepartment[];

  constructor(
    private departmentCategoryService: DepartmentCategoryService,
    private departmentService: DepartmentService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
  ) {
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.departmentCategoryId = params['id'];
      if(this.departmentCategoryId){
        this.mode = 'Update';
        this.updateForm(this.departmentCategoryId);
      }
    });

    this.departmentCategoryForm = this.fb.group({
      name: [null, Validators.required],
      type: [null, Validators.required]
    })

    this.getAllDepartments();

  }

  onSubmit() {
    if(this.departmentCategoryForm && this.departmentCategoryForm.valid){

      this.departmentCategory = this.createFromForm();
      this.departmentCategoryService.create(this.departmentCategory).subscribe((res) =>{
        if(res && res.body){
          this.router.navigate(["department-category"]);
        }
      });
    }
  }

  updateForm(id?: any){
    this.getDepartmentCategoryById(id);
  }

  getDepartmentCategoryById(id?: any){
    this.departmentCategoryService.getDepartmentCategoryById(id).subscribe(res =>{
      if(res && res.body){
        this.departmentCategory = res.body
        this.departmentCategoryName = this.departmentCategory.department.name;
        this.patchFormWithDto();
      }
    })
  }

  patchFormWithDto() {
    this.departmentCategoryForm.patchValue({
      name: this.departmentCategory.name,
      type: this.departmentCategory.department
    });
  }

  createFromForm(){
    const formValue = this.departmentCategoryForm.value;

    const departmentCategory: IDepartmentCategory = {
      id: this.departmentCategoryId? this.departmentCategoryId : undefined,
      name: formValue.name,
      department: {
        id: formValue.type.id,
        name: formValue.type.name
      }
    };

    return departmentCategory;
  }

  getAllDepartments(){
    this.departmentService.getDepartments().subscribe((res) =>{
      if(res && res.body){
        this.departments = res.body;
      }
    })
  }

  onCancel() {
    // this.router.navigate(["tickets"]);
  }
}
