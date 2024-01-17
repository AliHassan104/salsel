import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { CountryService } from "../service/country.service";
import { ActivatedRoute, Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";

@Component({
  selector: "app-country-form",
  templateUrl: "./country-form.component.html",
  styleUrls: ["./country-form.component.scss"],
  providers: [MessageService],
})
export class CountryFormComponent implements OnInit {
  countryForm!: FormGroup;

  editMode;
  singleCountry;
  editId: number;

  constructor(
    private countryService: CountryService,
    private router: Router,
    private formService: FormvalidationService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.formSetup();

    //Query Params
    this.queryParamSetup();
    this.editForm();
  }

  formSetup() {
    this.countryForm = new FormGroup({
      name: new FormControl(null, Validators.required),
    });
  }

  queryParamSetup() {
    this.route.queryParams.subscribe((params) => {
      if (params["id"] != null) {
        this.editMode = params["updateMode"] === "true"; // Convert to boolean
        this.editId = +params["id"]; // Convert to number
      } else {
        this.editMode = false;
      }
    });
  }

  editForm() {
    if (this.editMode) {
      this.countryService
        .getSingleCountry(this.editId)
        .subscribe((res: any) => {
          this.singleCountry = res;

          this.countryForm.patchValue({
            name: this.singleCountry?.name,
          });
        });
    }
  }

  onSubmit(data) {
    if (this.countryForm.valid) {
      if (this.editMode) {
        this.countryService
          .editCountry(this.editId, data)
          .subscribe((res: any) => {
            this.countryForm.reset();
            this.router.navigate(["country/list"]);
          });
      } else {
        this.countryService.addCountry(data).subscribe((res: any) => {
          this.countryForm.reset();
          this.router.navigate(["country/list"]);
        });
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.countryForm);
    }
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  //   On cancel edit request
  onCancel() {
    this.router.navigate(["country/list"]);
  }
}
