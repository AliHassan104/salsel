import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IDepartment } from 'src/app/api/department.model';
import { DepartmentService } from '../service/department.service';


@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.scss']
})
export class DepartmentComponent {
  private _department: Object;
  departmentForm!: FormGroup;
  selectedCurrency: string;
  department?: IDepartment;
  departmentId?: any;
  mode?: string = 'Create'

  constructor(
    private departmentService: DepartmentService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
  ) {
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.departmentId = params['id'];
      if(this.departmentId){
        this.mode = 'Update';
        this.updateForm(this.departmentId);
      }
    });

    this.departmentForm = this.fb.group({
      name: [null, Validators.required]
    })
  }

  onSubmit() {
    if(this.departmentForm && this.departmentForm.valid){

      this.department = this.createFromForm();
      this.departmentService.create(this.department).subscribe((res) =>{
        if(res && res.body){
          this.router.navigate(["department"]);
        }
      });
    }
  }

  updateForm(id?: any){
    this.getDepartmentById(id);
  }

  getDepartmentById(id?: any){
    this.departmentService.getDepartmentById(id).subscribe(res =>{
      if(res && res.body){
        this.department = res.body
        this.patchFormWithDto();
      }
    })
  }

  patchFormWithDto() {
    this.departmentForm.patchValue({
      name: this.department.name
    });
  }

  createFromForm(){
    const formValue = this.departmentForm.value;

    const department: IDepartment = {
      id: this.departmentId? this.departmentId : undefined,
      name: formValue.name,
      status: formValue.productFieldStatus,
    };

    return department;
  }

  onCancel() {
    this.router.navigate(["tickets"]);
  }
}
