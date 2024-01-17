import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { CountryService } from "../../country/service/country.service";
import { CityService } from "../service/city.service";
import { ActivatedRoute, Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { Country } from "../../../Api/customer";

@Component({
  selector: "app-city-form",
  templateUrl: "./city-form.component.html",
  styleUrls: ["./city-form.component.scss"],
  providers: [MessageService],
})
export class CityFormComponent implements OnInit {
  cityForm!: FormGroup;

  editedCountry;
  countries?;
  singleCity;

  editMode;
  editId;
  params = { status: true };

  constructor(
    private countryService: CountryService,
    private cityService: CityService,
    private router: Router,
    private messageService: MessageService,
    private formService: FormvalidationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // REACTIVE FORM SETUP
    this.formSetup();

    //Query Params
    this.queryParamSetup();
    this.editForm();

    this.getAllCountries();
  }

  formSetup() {
    this.cityForm = new FormGroup({
      name: new FormControl(null, Validators.required),
      country: new FormControl(null, Validators.required),
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
      this.cityService.getSingleCity(this.editId).subscribe((res: any) => {
        this.singleCity = res;

        //For Country
        this.countryService
          .getAllCountries(this.params)
          .subscribe((res: any) => {
            this.editedCountry = res;
            this.editedCountry = this.editedCountry.filter(
              (country) => country?.name == this.singleCity?.country?.name
            );
            this.cityForm.patchValue({
              country: this.editedCountry[0],
            });
          });

        this.cityForm.patchValue({
          name: this.singleCity?.name,
        });
      });
    }
  }

  //   GET ALL COUNTRIES
  getAllCountries() {
    this.countryService.getAllCountries(this.params).subscribe((res: any) => {
      this.countries = res;
    });
  }

  onSubmit(data) {
    if (this.cityForm.valid) {
      const city = {
        country: {
          id: data?.country?.id,
          name: data?.country?.name,
        },
        name: data?.name,
      };

      if (this.editMode) {
        this.cityService.editCity(this.editId, data).subscribe((res) => {
          this.cityForm.reset();
          this.router.navigate(["city/list"]);
        });
      } else {
        this.cityService.addCity(data).subscribe((res) => {
          this.cityForm.reset();
          this.router.navigate(["city/list"]);
        });
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.cityForm);
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
    this.router.navigate(["city/list"]);
  }
}
