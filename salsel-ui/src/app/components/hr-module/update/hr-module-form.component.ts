import { HttpClient } from "@angular/common/http";
import { Component, ElementRef, ViewChild } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { Dropdown } from "primeng/dropdown";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { AccountService } from "../../accounts/service/account.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { CityService } from "../../City/service/city.service";
import { CountryService } from "../../country/service/country.service";
import { DepartmentService } from "../../department/service/department.service";
import { RolesService } from "../../permissions/service/roles.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { TicktingService } from "../../Tickets/service/tickting.service";
import { HrModuleService } from "../service/hr-module.service";
import { IEmployee } from "../model/employeeDto";

@Component({
  selector: "app-hr-module-form",
  templateUrl: "./hr-module-form.component.html",
  styleUrls: ["./hr-module-form.component.scss"],
  providers: [MessageService],
})
export class HrModuleFormComponent {
  params = { status: true };

  //   DIALOG BOX
  addCommentDialog: boolean = false;
  visible: boolean = false;
  // ALL PRODUCT FIELDS
  productFields;

  // DROPDOWN VALUES FROM PRODUCT FIELD
  status?;
  ticketFlag?;
  categories?;
  assignedTo?;
  countries?;
  originCities?;
  department?;

  // FOR EDIT PURPOSE
  editMode: boolean = false;
  editId;
  singleEmployee: IEmployee;
  editFileName: string;
  pickDate;
  pickTime;
  userRole;

  fileName: string = "";
  ticketEditParams?;
  otherDocs?: File[] = [];
  passportAttachment: any;
  idAttachment: any;

  //   FORM GROUP TICKET FORM
  employeeForm!: FormGroup;
  postCommentForm!: FormGroup;
  ticketType?: string[];
  fileList: File[] = [];
  subCategories;

  @ViewChild("fileInput") fileInput: ElementRef;
  @ViewChild("passportFile") passportFile: ElementRef;
  @ViewChild("idFile") idFile: ElementRef;
  @ViewChild("dropdown") dropdown?: Dropdown;
  @ViewChild("dropdown1") dropdown1?: Dropdown;
  @ViewChild("dropdown2") dropdown2?: Dropdown;
  @ViewChild("dropdown3") dropdown3?: Dropdown;
  @ViewChild("dropdown4") dropdown4?: Dropdown;
  @ViewChild("dropdown5") dropdown5?: Dropdown;
  @ViewChild("dropdown6") dropdown6?: Dropdown;
  @ViewChild("dropdown7") dropdown7?: Dropdown;

  //   CONSTRUCTOR
  constructor(
    private employeeService: HrModuleService,
    private _ticketService: TicktingService,
    private router: Router,
    private http: HttpClient,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private dropdownService: DropdownService,
    private formService: FormvalidationService,
    private countryService: CountryService,
    private cityService: CityService,
    private departmentService: DepartmentService,
    private roleService: RolesService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.employeeFormSetup();

    this.getAllProductFields();

    this.queryParamSetup();

    this.editForm();

    this.userRole = this.sessionStorageService.getRoleName();
  }

  //   ngAfterViewChecked(): void {
  //     if (this.employeeForm.get("ticketType").value == "Pickup Request") {
  //       const dropdowns = [
  //         this.dropdown,
  //         this.dropdown1,
  //         this.dropdown2,
  //         this.dropdown3,
  //       ];

  //       dropdowns.forEach((dropdown, index) => {
  //         if (dropdown) {
  //           (dropdown.filterBy as any) = {
  //             split: (_: any) => [(item: any) => item],
  //           };
  //         }
  //       });
  //     } else if (this.employeeForm.get("ticketType")?.value == "Rate Inquiry") {
  //       const dropdowns = [
  //         this.dropdown4,
  //         this.dropdown5,
  //         this.dropdown6,
  //         this.dropdown7,
  //       ];

  //       dropdowns.forEach((dropdown, index) => {
  //         if (dropdown) {
  //           (dropdown.filterBy as any) = {
  //             split: (_: any) => [(item: any) => item],
  //           };
  //         }
  //       });
  //     }
  //   }

  employeeFormSetup() {
    this.employeeForm = new FormGroup({
      mobile: new FormControl(null),
      nationality: new FormControl(null),
      jobTitle: new FormControl(null),
      department: new FormControl(null),
      salary: new FormControl(null),
      transportation: new FormControl(null),
      otherAllowance: new FormControl(null),
      housing: new FormControl(null),
    });
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
  }

  editForm() {
    if (this.editId != null) {
      this.employeeService
        .getEmployeeById(this.editId)
        .subscribe((res: any) => {
          this.singleEmployee = res.body;
          console.log(res.body);
          const passwordPath = this.singleEmployee?.passportFilePath;
          const idPath = this.singleEmployee?.idFilePath;

          const filePaths = this.singleEmployee?.attachments.map(
            (attachment) => attachment.filePath
          );

          const FileNames = filePaths.join(",");
          if(FileNames){
              this.editFileName = `${FileNames},${passwordPath},${idPath}`;
          }else{
              this.editFileName = `${passwordPath},${idPath}`;

          }

          this.attachCopy(passwordPath, "passport");
          this.attachCopy(idPath, "id");

          for (let i = 0; i < this.singleEmployee?.attachments?.length; i++) {
            this.attachAgreement(
              this.singleEmployee?.attachments[i]?.filePath,
              this.singleEmployee?.attachments[i]?.filePath.split("/").pop()
            );
          }

          this.employeeForm.patchValue({
            mobile: this.singleEmployee?.mobile,
            nationality: this.singleEmployee?.nationality,
            jobTitle: this.singleEmployee?.jobTitle,
            department: this.singleEmployee?.department,
            salary: this.singleEmployee?.salary,
            transportation: this.singleEmployee?.transportation,
            otherAllowance: this.singleEmployee?.otherAllowance,
            housing: this.singleEmployee?.housing,
          });
        });
    }
  }

  //   GET ALL PRODUCT FIELD

  getAllProductFields() {
    // Get All Countries

    this.countryService.getAllCountries(this.params).subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });

    // Get All Departments
    this.departmentService.getDepartments(this.params).subscribe((res) => {
      this.department = res.body;
      this.department = this.dropdownService.extractNames(this.department);
    });

    // Get All Roles
    this.roleService.getRoles().subscribe((res: any) => {
      this.assignedTo = res;
      this.assignedTo = this.dropdownService.extractNames(this.assignedTo);
    });
  }

  //   GET ALL CITIES,DEPARTMENT CATEGORIES
  getAllCitiesAndDepartmentCategories(
    destinationCountry: any,
    originCountry: any,
    department: any
  ) {
    this.cityService.getAllCities(this.params).subscribe((res) => {
      this.originCities = res;
      //   Origin Cities
      let filterOriginCities = this.originCities.filter(
        (value) => value?.country?.name == originCountry
      );
      this.originCities = this.dropdownService.extractNames(filterOriginCities);
    });
  }

  //   GET COUNTRYFROM DROPDOWN
  getCountry(country) {
    this.cityService.getAllCities(this.params).subscribe((res) => {
      this.originCities = res;
      let filterCities = this.originCities.filter(
        (city) => city?.country?.name == country.value
      );
      this.originCities = this.dropdownService.extractNames(filterCities);
    });
  }

  //  ON TICKET FORM SUBMIT

  onSubmit() {
    if (this.employeeForm.valid) {
      const formValue = this.employeeForm.value;
      const employeeData: IEmployee = {
        department: formValue?.department,
        jobTitle: formValue?.jobTitle,
        housing: formValue?.housing,
        nationality: formValue?.nationality,
        otherAllowance: formValue?.otherAllowance,
        salary: formValue?.salary,
        transportation: formValue?.transportation,
        mobile: formValue?.transportation,
        userId: 20,
      };

      if (this.editMode) {
        this.employeeService.update(this.editId,employeeData,{fileNames:this.editFileName},this.passportAttachment,this.idAttachment,this.otherDocs).subscribe((res:any)=>{
            this.employeeForm.reset();
            this.router.navigate(["employee/list"]);
        })
      } else {
        this.employeeService
          .create(
            employeeData,
            this.passportAttachment,
            this.idAttachment,
            this.otherDocs
          )
          .subscribe((res: any) => {
            this.employeeForm.reset();
            this.router.navigate(["employee/list"]);
          });
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.employeeForm);
    }
  }

  // Employee Form

  onUploadPassport(event: any): void {
    const fileInput = event.target;
    if (fileInput.files && fileInput.files.length > 0) {
      const selectedFile = fileInput.files[0];
      console.log(selectedFile);

      // Check if the selected file has a PDF extension or it's an image file
      if (
        selectedFile.name.toLowerCase().endsWith(".pdf") ||
        (selectedFile.type.startsWith("image/") &&
          !selectedFile.type.endsWith("svg+xml"))
      ) {
        this.fileName = selectedFile.name;
        this.passportAttachment = selectedFile;
      } else {
        fileInput.value = null;
        this.messageService.add({
          severity: "error",
          summary: "Invalid File",
          detail: "Only PDF and image files are allowed.",
        });
      }
    }
  }

  onUploadId(event: any): void {
    const fileInput = event.target;
    if (fileInput.files && fileInput.files.length > 0) {
      const selectedFile = fileInput.files[0];
      console.log(selectedFile);

      // Check if the selected file has a PDF extension or it's an image file
      if (
        selectedFile.name.toLowerCase().endsWith(".pdf") ||
        (selectedFile.type.startsWith("image/") &&
          !selectedFile.type.endsWith("svg+xml"))
      ) {
        this.fileName = selectedFile.name;
        this.idAttachment = selectedFile;
      } else {
        fileInput.value = null;
        this.messageService.add({
          severity: "error",
          summary: "Invalid File",
          detail: "Only PDF and image files are allowed.",
        });
      }
    }
  }

  attachCopy(agreementUrl: string, attachmentType: string) {
    this.accountService.downloadAgreement(agreementUrl).subscribe(
      (blob: Blob) => {
        const fileName = agreementUrl.split("/").pop();
        const file = new File([blob], fileName, {
          type: "application/pdf",
        });
        // Create a DataTransfer object
        const dataTransfer = new DataTransfer();

        // Add the file to the DataTransfer object
        dataTransfer.items.add(file);

        // Set the files property of the input element
        switch (attachmentType) {
          case "passport":
            this.passportFile.nativeElement.files = dataTransfer.files;
            this.passportAttachment = file;
            break;
          case "id":
            this.idFile.nativeElement.files = dataTransfer.files;
            this.idAttachment = file;
            break;
        }
      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: "Error Attaching File",
        });
      }
    );
  }

  //   Other Docs

  onFileChange(event: any): void {
    const fileInput = event.target;

    if (fileInput.files && fileInput.files.length > 0) {
      const selectedFiles = fileInput.files;

      // Check file extensions
      const allowedExtensions = /\.(pdf|jpg|jpeg|png|gif)$/i;
      let isValid = true;

      for (let i = 0; i < selectedFiles.length; i++) {
        const file = selectedFiles[i];
        if (!allowedExtensions.test(file.name)) {
          isValid = false;
          break;
        }
      }

      if (isValid) {
        this.fileName = selectedFiles[0].name;
        for (let i = 0; i < selectedFiles.length; i++) {
          this.otherDocs.push(selectedFiles[i]);
        }
        this.updateFileInput();
      } else {
        this.updateFileInput();
        // this.clearFileInput();
        // this.otherDocs = [];
        this.messageService.add({
          severity: "error",
          summary: "Invalid File",
          detail: "Only PDF, JPG, JPEG, PNG, GIF files are allowed.",
        });
      }
    } else {
      //   this.otherDocs = [];
      //   this.clearFileInput();
      this.updateFileInput();
      this.messageService.add({
        severity: "warn",
        summary: "No File Selected",
      });
    }
  }

  removeFile(index: number): void {
    this.otherDocs.splice(index, 1);
    this.updateFileInput();
  }

  attachAgreement(attachmentUrl: string, fileName: string) {
    this.accountService.downloadAgreement(attachmentUrl).subscribe(
      (blob: Blob) => {
        const file = new File([blob], `${fileName}`, {
          type: "application/pdf",
        });
        this.attachFiles(file);
      },
      (error) => {
        this.messageService.add({
          severity: "error",
          summary: "Error",
          detail: "Attachment Not Found",
        });
      }
    );
  }

  attachFiles(file: File) {
    // Add the file to the global array
    this.fileList.push(file);

    // Set the files property of the input element
    const dataTransfer = new DataTransfer();
    this.fileList.forEach((f, index) => {
      dataTransfer.items.add(f);
    });
    this.fileInput.nativeElement.files = dataTransfer.files;
    this.otherDocs = this.fileList;
  }

  updateFileInput(): void {
    const fileInput = document.getElementById("attachment") as HTMLInputElement;
    if (fileInput) {
      const newFileList = new DataTransfer();
      for (const file of this.otherDocs) {
        newFileList.items.add(file);
      }
      fileInput.files = newFileList.files;
    }
  }

  clearFileInput(): void {
    // Reset the file input field
    const fileInput = document.getElementById("attachment") as HTMLInputElement;
    if (fileInput) {
      fileInput.value = null;
    }
  }

  // PART OF POPUP

  onCancel() {
    this.router.navigate(["ticket/list"]);
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
