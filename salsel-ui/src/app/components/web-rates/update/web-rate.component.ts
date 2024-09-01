import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Dropdown } from 'primeng/dropdown';
import { map } from 'rxjs';
import { DropdownService } from 'src/app/layout/service/dropdown.service';
import { AccountService } from '../../accounts/service/account.service';
import { AddressBookService } from '../../addressBook/service/address-book.service';
import { CityService } from '../../City/service/city.service';
import { CountryService } from '../../country/service/country.service';
import { FormvalidationService } from '../../Tickets/service/formvalidation.service';
import { IAddressBook } from '../../addressBook/model/addressBookDto';
import { IWebRate } from '../model/webRateDto';
import { WebRatesService } from '../web-rates.service';

@Component({
  selector: "app-web-rate",
  templateUrl: "./web-rate.component.html",
  styleUrls: ["./web-rate.component.scss"],
  providers: [MessageService],
})
export class WebRateComponent {
  @ViewChild("dropdown") dropdown?: Dropdown;
  @ViewChild("dropdown1") dropdown1?: Dropdown;
  @ViewChild("dropdown2") dropdown2?: Dropdown;
  @ViewChild("dropdown3") dropdown3?: Dropdown;

  addressBookForm!: FormGroup;
  addressBook?: IWebRate;
  addressBookId?: any;
  mode?: string = "Add";
  userTypes?;
  accountNumbers;
  preprocessedAccountNumbers;
  products;

  productFields?;
  countries;
  cities;

  constructor(
    private addressBookService: WebRatesService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private formService: FormvalidationService,
    private messageService: MessageService,
    private dropDownService: DropdownService,
    private countryService: CountryService,
    private cityService: CityService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.addressBookForm = this.fb.group({
      fromCountry: [null, Validators.required],
      toCountry: [null, Validators.required],
      product: [null, Validators.required],
      weightRangeFrom: [null, Validators.required],
      weightRangeTo: [null, Validators.required],
      charges: [null, Validators.required],
      additionalCharges: [null],
    });

    this.getProductFieldValues();

    this.route.queryParams.subscribe((params) => {
      this.addressBookId = params["id"];
      if (this.addressBookId) {
        this.mode = "Update";
        this.updateForm(this.addressBookId);
      }
    });
  }

  ngAfterViewInit(): void {
    const dropdowns = [this.dropdown, this.dropdown1, this.dropdown2,this.dropdown3];

    dropdowns.forEach((dropdown, index) => {
      if (dropdown) {
        (dropdown.filterBy as any) = {
          split: (_: any) => [(item: any) => item],
        };
      }
    });
  }

  getProductFieldValues() {
    this.dropDownService.getAllProductFields().subscribe((res: any) => {
      this.productFields = res;

      this.products = this.dropDownService.extractNames(
        this.productFields.filter((data: any) => data?.name == "products")[0]
          ?.productFieldValuesList
      );
    });

    this.countryService
      .getAllCountries({ status: true })
      .subscribe((res: any) => {
        this.countries = res;
        this.countries = this.dropDownService.extractNames(this.countries);
      });

    // GET ALL AccountNumbers
    this.getAllAccountNumbers();
  }

  getAllAccountNumbers() {
    this.accountService
      .getAllAccounts({ status: true })
      .pipe(
        map((res: any) =>
          res?.body?.map((item: any) => String(item.accountNumber))
        )
      )
      .subscribe((res: string[]) => {
        this.accountNumbers = res;
      });
  }

  onSubmit() {
    if (this.addressBookForm && this.addressBookForm.valid) {
      this.addressBook = this.createFromForm();
      if (this.mode == "Update") {
        this.addressBookService
          .update(this.addressBook, this.addressBookId)
          .subscribe(
            (res: any) => {
              this.router.navigate(["rate/list"]);
            },
            (error) => {
              this.error(error);
            }
          );
      } else {
        this.addressBookService.create(this.addressBook).subscribe(
          (res) => {
            if (res && res.body) {
              this.router.navigate(["rate/list"]);
              console.log(res, res.body);
            }
          },
          (error) => {
            this.error(error);
            console.log(error);
          }
        );
      }
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.addressBookForm);
    }
  }

  updateForm(id?: any) {
    this.getAddressBookById(id);
  }

  getAddressBookById(id?: any) {
    this.addressBookService.getAddressBookById(id).subscribe((res) => {
      if (res && res.body) {
        this.addressBook = res.body;

        // this.patchCity(this.addressBook?.country);
        this.patchFormWithDto();
      }
    });
  }

  patchFormWithDto() {
    this.addressBookForm.patchValue({
      fromCountry: this.addressBook?.fromCountry,
      toCountry: this.addressBook?.toCountry,
      product: this.addressBook?.product,
      weightRangeFrom: this.addressBook?.weightRangeFrom,
      weightRangeTo: this.addressBook?.weightRangeTo,
      charges: this.addressBook?.charges,
      additionalCharges: this.addressBook?.additionalCharges,
    });
  }

  //   patchCity(country?: any) {
  //     this.cityService.getAllCitiesByCountryName(country).subscribe((res) => {
  //       if (res) {
  //         this.cities = res;
  //         this.cities = this.dropDownService.extractNames(this.cities);
  //       }
  //     });
  //   }

  createFromForm() {
    const formValue = this.addressBookForm.value;

    const addressBook: IWebRate = {
      id: this.addressBookId ? this.addressBookId : undefined,
      fromCountry: formValue.fromCountry,
      toCountry: formValue.toCountry,
      product: formValue.product,
      weightRangeFrom: formValue.weightRangeFrom,
      weightRangeTo: formValue.weightRangeTo,
      charges: formValue.charges,
      additionalCharges: formValue.additionalCharges,
    };

    return addressBook;
  }

  onSelectCountry(data: any) {
    const country = data.value;

    this.cityService.getAllCitiesByCountryName(country).subscribe((res) => {
      if (res) {
        this.cities = res;
        this.cities = this.dropDownService.extractNames(this.cities);
      }
    });
  }

  error(error) {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: error.error.error,
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
    this.router.navigate(["rate/list"]);
  }
}
