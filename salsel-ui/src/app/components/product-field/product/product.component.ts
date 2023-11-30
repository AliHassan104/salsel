import { Component, OnInit, OnDestroy } from "@angular/core";
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, NavigationEnd, Router } from "@angular/router";
import { HttpClient } from "@angular/common/http";
import { Ticket } from "src/app/api/ticket";
import { MessageService } from "primeng/api";
import { environment } from "src/environments/environment";
import { IProductFieldValuesDto } from "src/app/api/productFieldValuesDto";
import { IProductFieldDto } from "src/app/api/productFieldDto";
import { ProductFieldService } from "../service/product-service.service";
import { an } from "@fullcalendar/core/internal-common";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
  providers: [MessageService]
})
export class ProductComponent {

  productFieldForm: FormGroup;
  types = ["DROPDOWN", "MULTIDROPDOWN"];
  status = ["Active", "Inactive"];
  productFieldDto?: IProductFieldDto;
  submitForm?: boolean = false;
  productFieldId?: any;
  mode?: string = 'Create'

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private fb: FormBuilder,
    private productService?: ProductFieldService
  ) {
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.productFieldId = params['id'];
      console.log(this.productFieldId);
      if(this.productFieldId){
        this.mode = 'Update';
        this.updateForm(this.productFieldId);
      }
    });

    this.productFieldForm = this.fb.group({
      name: [null, Validators.required],
      sequence: [null, Validators.required],
      type: [null, Validators.required],
      productFieldStatus: ['Active', Validators.required],
      productFieldValues: this.fb.array([this.createProductField()]),
    });
  };

  createProductField(): FormGroup {
    return this.fb.group({
      id: [null],
      name: [null, Validators.required],
      status: ['Active', Validators.required],
    });
  }

  addProductField() {
    this.productFieldValues.push(this.createProductField());
  }

  removeProductField(index: number, pfvid?: any) {
    debugger;
    this.productFieldValues.removeAt(index);

    if(this.productFieldId){
      this.productService.removeProductFieldValue(this.productFieldId, pfvid).subscribe(res =>{
        if(res.status == 200){
          console.log("removed");
        }
      })
    }
  }

  get productFieldValues() {
    return (this.productFieldForm.get('productFieldValues') as FormArray);
  }

  isTypeSelected(): boolean {
    const typeControl = this.productFieldForm.get('type');
    return typeControl && typeControl.value !== null && typeControl.value !== '';
  }

  getProductFieldById(id?: any){
    this.productService.getProductFieldById(id).subscribe(res =>{
      if(res && res.body){
        this.productFieldDto = res.body
        this.patchFormWithDto();
      }
    })
  }

  patchFormWithDto() {
    const productFieldValuesArray = this.productFieldForm.get('productFieldValues') as FormArray;
    productFieldValuesArray.clear();
    this.productFieldForm.patchValue({
      name: this.productFieldDto.name,
      sequence: this.productFieldDto.sequence,
      type: this.productFieldDto.type,
      productFieldStatus: this.productFieldDto.status,
    });

    this.productFieldDto.productFieldValuesList.forEach((value) => {
      productFieldValuesArray.push(this.fb.group({
        id: value.id,
        name: value.name,
        status: value.status
      }));
    });
  }

  onSubmit() {
    if(this.submitForm && this.productFieldForm.valid){
      this.productFieldDto = this.createFromForm();
      this.productService.addProductFields(this.productFieldDto).subscribe(res =>{
        if(res && res.body){
          this.router.navigate(["product/product-list"])
        }
      })
    }
    this.submitForm = false;
  }

  createFromForm(){
    const formValue = this.productFieldForm.value;

    const productFieldValuesList: IProductFieldValuesDto[] = formValue.productFieldValues.map((values: any) => {
      return {
        id: values.id? values.id : undefined,
        name: values.name,
        status: values.status,
      };
    });

    const productFieldDto: IProductFieldDto = {
      id: this.productFieldId? this.productFieldId : undefined,
      name: formValue.name,
      sequence: formValue.sequence,
      type: formValue.type,
      status: formValue.productFieldStatus,
      productFieldValuesList: productFieldValuesList,
    };

    return productFieldDto;
  }

  updateForm(id?: any){
    this.getProductFieldById(id);
  }

  onCancel() {
    this.router.navigate(["tickets"]);
  }

  //   Pop up message
  success() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket added succesfully",
    });
  }

  update() {
    this.messageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket Updated succesfully",
    });
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }
}
