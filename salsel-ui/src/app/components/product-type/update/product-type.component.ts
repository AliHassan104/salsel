
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { IDepartment } from 'src/app/api/department.model';
import { DepartmentService } from '../../department/service/department.service';
import { IProductType } from 'src/app/api/productType.model';
import { ProductTypeService } from '../service/product-type.service';

@Component({
  selector: 'app-product-type',
  templateUrl: './product-type.component.html',
  styleUrls: ['./product-type.component.scss']
})
export class ProductTypeComponent {
  productTypeForm!: FormGroup;
  productType?: IProductType;
  id?: any;
  mode?: string = 'Create'

  constructor(
    private productTypeService: ProductTypeService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
  ) {
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.id = params['id'];
      if(this.id){
        this.mode = 'Update';
        this.updateForm(this.id);
      }
    });

    this.productTypeForm = this.fb.group({
      name: [null, Validators.required],
      code: [null, Validators.required]
    })
  }

  onSubmit() {
    if(this.productTypeForm && this.productTypeForm.valid){

      this.productType = this.createFromForm();
      this.productTypeService.addProductType(this.productType).subscribe((res) =>{
        if(res && res.body){
          this.router.navigate(["product-type/list"]);
        }
      });
    }
  }

  updateForm(id?: any){
    this.getDepartmentById(id);
  }

  getDepartmentById(id?: any){
    this.productTypeService.getProductTypeById(id).subscribe(res =>{
      if(res && res.body){
        this.productType = res.body
        this.patchFormWithDto();
      }
    })
  }

  patchFormWithDto() {
    this.productTypeForm.patchValue({
      name: this.productType.name,
      code: this.productType.code
    });
  }

  createFromForm(){
    const formValue = this.productTypeForm.value;

    const productType: IProductType = {
      id: this.id? this.id : undefined,
      name: formValue.name,
      code: formValue.code,
    };

    return productType;
  }

  onCancel() {
    this.router.navigate(["product-type/list"]);
  }
}
