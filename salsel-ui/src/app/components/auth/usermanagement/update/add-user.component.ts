import { HttpClient } from "@angular/common/http";
import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "src/app/components/Tickets/service/formvalidation.service";
import { RolesService } from "../../../permissions/service/roles.service";
import { UserService } from "../service/user.service";
import { IUser } from "../model/userDto";
import { LoginService } from "../../service/login.service";
import { Password } from "primeng/password";
import { Dropdown } from "primeng/dropdown";
import { CityService } from "src/app/components/City/service/city.service";
import { CountryService } from "src/app/components/country/service/country.service";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { HrModuleService } from "src/app/components/hr-module/service/hr-module.service";
import { Table } from "primeng/table";

@Component({
  selector: "app-add-user",
  templateUrl: "./add-user.component.html",
  styleUrls: ["./add-user.component.scss"],
  providers: [MessageService],
})
export class AddUserComponent implements OnInit {
  visible: boolean = false;
  employees;
  countries?;
  cities?;
  editMode;
  editId;
  singleUser?: IUser;
  roles?;
  params: any = { status: true };

  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  @ViewChild("dropdown") dropdown?: Dropdown;
  @ViewChild("dropdown1") dropdown1?: Dropdown;
  constructor(
    private router: Router,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private formService: FormvalidationService,
    private rolesService: RolesService,
    private userService: UserService,
    private loginService: LoginService,
    private cityService: CityService,
    private countryService: CountryService,
    private dropdownService: DropdownService,
    private employeeService: HrModuleService
  ) {}

  userForm!: FormGroup;

  ngOnInit(): void {
    this.formSetup();
    this.getRoles();
    this.queryParamSetup();
    this.onGettingEmployee();
  }

  ngAfterViewInit(): void {
    if (this.dropdown) {
      (this.dropdown.filterBy as any) = {
        split: (_: any) => [(item: any) => item],
      };
    }
    if (this.dropdown1) {
      (this.dropdown1.filterBy as any) = {
        split: (_: any) => [(item: any) => item],
      };
    }
  }

  //   Get All Cities
  getAllCities(countryName) {
    this.cityService.getAllCities(this.params).subscribe((res: any) => {
      this.cities = res;
      let filterCities = this.cities.filter(
        (city) => city?.country?.name == countryName
      );
      this.cities = this.dropdownService.extractNames(filterCities);
    });
  }

  getCountry(country: any) {
    this.cityService.getAllCities(this.params).subscribe((res: any) => {
      this.cities = res;
      let filterCities = this.cities.filter(
        (city: any) => city?.country?.name == country?.value
      );
      this.cities = this.dropdownService.extractNames(filterCities);
    });
  }

  formSetup() {
    this.userForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
      firstname: new FormControl(null),
      lastname: new FormControl(null),
      phone: new FormControl(null),
      roles: new FormControl(null, Validators.required),
      country: new FormControl(null, Validators.required),
      city: new FormControl(null, Validators.required),
    });
  }

  isLowerCaseMissing(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/(?=.*[a-z])/.test(this.userForm.value.password)
    );
  }

  isUpperCaseMissing(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/(?=.*[A-Z])/.test(this.userForm.value.password)
    );
  }

  isDigitMissing(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/(?=.*\d)/.test(this.userForm.value.password)
    );
  }

  isShortPassword(): boolean {
    return (
      this.userForm.get("password")?.hasError("pattern") &&
      !/.{8,}/.test(this.userForm.value.password)
    );
  }

  queryParamSetup() {
    this.route.queryParams.subscribe((params) => {
      // Retrieve editMode and id from the query parameters
      if (params["id"] != null) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      } else {
        this.editMode = false;
      }
    });

    this.editForm();
  }

  editForm() {
    if (this.editMode) {
      this.userService.GetUserById(this.editId).subscribe((res: any) => {
        this.singleUser = res;

        this.rolesService.getRoles().subscribe((res: any) => {
          const role = res.filter(
            (value) => value?.name == this.singleUser?.roles[0]?.name
          );

          this.getAllCities(this.singleUser?.country);

          this.userForm.patchValue({
            email: this.singleUser?.email,
            firstname: this.singleUser?.firstname,
            lastname: this.singleUser?.lastname,
            phone: this.singleUser?.phone,
            roles: role != null ? role[0] : "",
            country: this.singleUser?.country,
            city: this.singleUser?.city,
          });
        });
      });
    }
  }

  getRoles() {
    this.rolesService.getRoles().subscribe((res: any) => {
      this.roles = res;
    });

    this.countryService.getAllCountries(this.params).subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });
  }

  onSubmit() {
    if (this.userForm.valid) {
      let formValue = this.userForm.value;
      let fullname = formValue.firstname + " " + formValue.lastname;
      const data = {
        firstname: formValue.firstname,
        lastname: formValue.lastname,
        name: fullname,
        phone: formValue.phone,
        email: formValue.email,
        country: formValue.country,
        city: formValue.city,
        roles: [
          {
            id: formValue.roles.id,
          },
        ],
        status: true,
      };
      if (this.editMode) {
        this.userService.updateUser(this.editId, data).subscribe(
          (res: any) => {
            this.userForm.reset();
            this.router.navigate(["user/list"]);
          },
          (error) => {
            this.messageService.add({
              severity: "error",
              summary: "Error",
              detail: error?.error?.error,
            });
          }
        );
      } else {
        this.loginService.signUp(data).subscribe(
          (res: any) => {
            this.userForm.reset();
            this.router.navigate(["user/list"]);
          },
          (error) => {
            this.messageService.add({
              severity: "error",
              summary: "Error",
              detail: error?.error?.error,
            });
          }
        );
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.userForm);
    }
  }

  onCancel() {
    this.router.navigate(["user/list"]);
  }

  autoFillData() {
    this.visible = true;
  }

  onSelectEmployee(id:any){
    this.employeeService.getEmployeeById(id).subscribe((response:any)=>{
        const employee = response.body;


        this.getAllCities(employee?.country);

        this.userForm.patchValue({
          firstname: employee?.firstname,
          lastname: employee?.lastname,
          phone: employee?.mobile,
          country: employee?.country,
          city: employee?.city,
        });

        this.visible = false;
        this.messageService.add({
          severity: "success",
          summary: "Success",
          detail: "Data has been filled in successfully.",
        });
    })

  }

  onGettingEmployee() {
    this.employeeService.getAllEmployees(this.params).subscribe((res: any) => {
      // const filteredData = res?.body?.filter((item) => !item.email);
      // console.log(filteredData);
      // this.employees = filteredData;
      this.employees = res.body;
    });
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }
}
