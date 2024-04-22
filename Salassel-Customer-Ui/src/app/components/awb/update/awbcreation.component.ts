import { HttpClient } from "@angular/common/http";
import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { AirbillService } from "../service/airbill.service";
import { MessageService } from "primeng/api";
import { TicktingService } from "../../Tickets/service/tickting.service";
import { DropdownService } from "src/app/layout/service/dropdown.service";
import { FormvalidationService } from "../../Tickets/service/formvalidation.service";
import { IAwbDto } from "src/app/components/awb/model/awbValuesDto";
import { CountryService } from "../../../service/country.service";
import { CityService } from "../../../service/city.service";
import { ProductTypeService } from "../../../service/product-type.service";
import { Ticket } from "src/app/components/Tickets/model/ticketValuesDto";
import { AccountService } from "../../accounts/service/account.service";
import { ServiceTypeService } from "src/app/service/service-type.service";
import { Dropdown } from "primeng/dropdown";
import { Table } from "primeng/table";
import { AddressBookService } from "../../addressBook/service/address-book.service";
import { IAddressBook } from "../../addressBook/model/addressBookDto";

@Component({
  selector: "app-awbcreation",
  templateUrl: "./awbcreation.component.html",
  styleUrls: ["./awbcreation.component.scss"],
  providers: [MessageService],
})
export class AwbcreationComponent implements OnInit, OnDestroy, AfterViewInit {
  visible: boolean = false;
  addressBooks: IAddressBook[];
  selectedAddress: IAddressBook;
  tooltipVisible: boolean = true;
  addressType: boolean = true;

  //   ALL PRODUCT FIELD FOR DROPDOWNS
  productFields?;

  // DROPDOWNS FORM PRODUCT FIELD
  currencies?;
  status?;
  dutyTaxes?;
  requestTypes?;
  countries;
  originCities?;
  destinationCities?;
  productType?;
  serviceType?;

  // FOR EDIT AND CREATE AIRBILL FROM TICKET
  ticketMode;
  TicketId;
  editId;
  productTypeId;
  serviceTypeId;
  serviceTypeCode?;
  accountNumbers?;
  addressBookStatus;
  addressDialog: boolean = false;

  // SINGLE BILL AND TICKET STORE IN IT
  singleBill?: IAwbDto;
  singleTicket?: Ticket;
  preprocessedAccountNumbers: any;
  pickupDate: Date;
  pickupTime: Date;

  @ViewChild("dropdown") dropdown?: Dropdown;
  @ViewChild("dropdown1") dropdown1?: Dropdown;
  @ViewChild("dropdown2") dropdown2?: Dropdown;
  @ViewChild("dropdown3") dropdown3?: Dropdown;

  //   CONSTRUCTOR
  constructor(
    private _airbillService: AirbillService,
    private _ticketService: TicktingService,
    private router: Router,
    private http: HttpClient,
    private MessageService: MessageService,
    private route: ActivatedRoute,
    private dropdownService: DropdownService,
    private formService: FormvalidationService,
    private countryService: CountryService,
    private cityService: CityService,
    private productTypeService: ProductTypeService,
    private serviceTypeService: ServiceTypeService,
    private accountService: AccountService,
    private addressBookService: AddressBookService
  ) {}

  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  params = { status: true };
  awbForm!: FormGroup;
  loginUserEmail;

  ngOnInit(): void {
    //Setting Up Reactive Form
    this.awbFormSetup();

    // Query Param for edit and create from ticket
    this.queryParamsSetup();

    // FOR EDIT BILL
    // this.UpdateBill();

    // Create From Ticket
    this.CreateAwbFromTicket();

    // GET VALUES OF DROPDOWNS
    this.getAllProductField();

    this.loginUserEmail = localStorage.getItem("loginUserEmail");
  }

  ngAfterViewInit(): void {
    const dropdowns = [
      this.dropdown,
      this.dropdown1,
      this.dropdown2,
      this.dropdown3,
    ];

    dropdowns.forEach((dropdown, index) => {
      if (dropdown) {
        (dropdown.filterBy as any) = {
          split: (_: any) => [(item: any) => item],
        };
      }
    });
  }

  awbFormSetup() {
    this.awbForm = new FormGroup({
      shipperName: new FormControl(null),
      shipperContactNumber: new FormControl(null),
      pickupAddress: new FormControl(null),
      shipperRefNumber: new FormControl(null),
      originCountry: new FormControl(null),
      originCity: new FormControl(null),
      recipientsName: new FormControl(null),
      recipientsContactNumber: new FormControl(null),
      destinationCountry: new FormControl(null),
      destinationCity: new FormControl(null),
      deliveryAddress: new FormControl(null),
      pickupDate: new FormControl(null, Validators.required),
      pickupTime: new FormControl(null, Validators.required),
      productType: new FormControl(null, Validators.required),
      serviceType: new FormControl(null, Validators.required),
      content: new FormControl(null, Validators.required),
      weight: new FormControl(null, Validators.required),
      pieces: new FormControl(null, Validators.required),
      currency: new FormControl(null, Validators.required),
      amount: new FormControl(null, Validators.required),
      dutyAndTaxesBillTo: new FormControl(null, Validators.required),
      requestType: new FormControl(null, Validators.required),
      accountNumber: new FormControl(null, Validators.required),
      deliveryStreetName: new FormControl(null),
      deliveryDistrict: new FormControl(null),
      pickupStreetName: new FormControl(null),
      pickupDistrict: new FormControl(null),
    });
  }

  queryParamsSetup() {
    this.route.queryParams.subscribe((params) => {
      if (params["id"] != null) {
        this.ticketMode = true;
        this.TicketId = +params["id"];
      } else {
        this.ticketMode = false;
      }
    });
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }

  getAllProductField() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productFields = res;

      this.currencies = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Currency")[0]
          .productFieldValuesList
      );

      this.dutyTaxes = this.dropdownService.extractNames(
        this.productFields.filter(
          (data) => data.name == "Duty And Tax Billing"
        )[0].productFieldValuesList
      );

      this.requestTypes = this.dropdownService.extractNames(
        this.productFields.filter((data) => data.name == "Request Type")[0]
          .productFieldValuesList
      );
    });

    // Get All Countries

    this.countryService.getAllCountries(this.params).subscribe((res) => {
      this.countries = res;
      this.countries = this.dropdownService.extractNames(this.countries);
    });

    // GET ALL PRODUCT TYPES
    this.productTypeService.getProductTypes(this.params).subscribe((res) => {
      this.productType = res.body;
      this.productType = this.dropdownService.extractNames(this.productType);
    });

    // GET ALL AccountNumbers
    this.accountService
      .getAllAccountsByUserLoggedIn({ status: true })
      .subscribe((res: any) => {
        this.accountNumbers = res.body;
        this.preprocessedAccountNumbers = this.accountNumbers.map(
          (account) => ({
            label: `${account.accountNumber}, ${account.customerName}`,
            value: account.number,
          })
        );
      });
  }

  getProductType(data) {
    this.serviceTypeService.getServiceTypes(this.params).subscribe((res) => {
      this.serviceType = res.body;

      let filterServiceType = this.serviceType.filter(
        (type) => type?.productType?.name == data.value
      );

      this.serviceType = this.dropdownService.extractNames(filterServiceType);
    });
  }

  getServiceType(data) {
    this.serviceTypeService
      .getServiceTypes(this.params)
      .subscribe((res: any) => {
        let filterServiceType = res.body.filter(
          (type) => type?.name == data.value
        );
        this.serviceTypeCode =
          this.dropdownService.extractCode(filterServiceType)[0];
      });
  }

  CreateAwbFromTicket() {
    if (this.ticketMode) {
      this._ticketService.getSingleTicket(this.TicketId).subscribe((res) => {
        this.singleTicket = res;
        this.getAllCities(
          this.singleTicket.destinationCountry,
          this.singleTicket.originCountry
        );
        if (
          this.singleTicket.pickupTime != null &&
          this.singleTicket.pickupDate != null
        ) {
          this.pickupDate = new Date(this.singleTicket.pickupDate);
          let pickTimeArray = this.singleTicket.pickupTime;
          this.pickupTime = new Date(`2023-11-12 ${pickTimeArray}`);
        } else {
          this.pickupDate = null;
          this.pickupTime = null;
        }
        this.awbForm.patchValue({
          shipperName: this.singleTicket.shipperName,
          shipperContactNumber: this.singleTicket.shipperContactNumber,
          pickupAddress: this.singleTicket.pickupAddress,
          shipperRefNumber: this.singleTicket.shipperRefNumber,
          originCountry: this.singleTicket.originCountry,
          originCity: this.singleTicket.originCity,
          recipientsName: this.singleTicket.recipientName,
          recipientsContactNumber: this.singleTicket.recipientContactNumber,
          destinationCountry: this.singleTicket.destinationCountry,
          destinationCity: this.singleTicket.destinationCity,
          deliveryAddress: this.singleTicket.deliveryAddress,
          pickupDate: this.pickupDate,
          pickupTime: this.pickupTime,
          deliveryStreetName: this.singleTicket.deliveryStreetName,
          deliveryDistrict: this.singleTicket.deliveryDistrict,
          pickupStreetName: this.singleTicket.pickupStreetName,
          pickupDistrict: this.singleTicket.pickupDistrict,
        });
      });
    }
  }

  getAllCities(destinationCountry: any, originCountry: any) {
    this.cityService.getAllCities(this.params).subscribe((res?: any) => {
      this.destinationCities = res;
      this.originCities = res;
      //   Destination Cities
      let filterDestinationCities = this.destinationCities.filter(
        (value) => value?.country?.name == destinationCountry
      );
      this.destinationCities = this.dropdownService.extractNames(
        filterDestinationCities
      );

      //   Origin Cities
      let filterOriginCities = this.originCities.filter(
        (value) => value?.country?.name == originCountry
      );
      this.originCities = this.dropdownService.extractNames(filterOriginCities);
    });
  }

  getAllServiceTypes(productType: any) {
    this.serviceTypeService
      .getServiceTypes(this.params)
      .subscribe((res?: any) => {
        this.serviceType = res.body;
        let filterServiceType = this.serviceType.filter(
          (type) => type?.productType?.name == productType
        );
        this.serviceType = this.dropdownService.extractNames(filterServiceType);
      });
  }

  getDestinationCountry(country) {
    this.cityService.getAllCities(this.params).subscribe((res?: any) => {
      this.destinationCities = res;
      let filterCities = this.destinationCities.filter(
        (city) => city?.country?.name == country.value
      );
      this.destinationCities = this.dropdownService.extractNames(filterCities);
    });
  }

  getOriginCountry(country) {
    this.cityService.getAllCities(this.params).subscribe((res?: any) => {
      this.originCities = res;
      let filterCities = this.originCities.filter(
        (city) => city?.country?.name == country.value
      );
      this.originCities = this.dropdownService.extractNames(filterCities);
    });
  }

  onSubmit() {
    if (this.awbForm.valid) {
      // Date and Time get from form
      let ticketDate: Date = this.awbForm.value.pickupDate;
      let ticketT: Date = this.awbForm.value.pickupTime;
      // Now converting date in to our desired format
      let formattedDate: string = this._airbillService.formatDate(ticketDate);

      // Now converting Time in to our desired format
      const ticketTime: string =
        this._airbillService.formatDateToHHMMSS(ticketT);

      //   To save time
      const formValue = this.awbForm.value;
      const billData: IAwbDto = {
        shipperName: formValue.shipperName,
        shipperContactNumber: formValue.shipperContactNumber,
        pickupAddress: formValue.pickupAddress,
        shipperRefNumber: formValue.shipperRefNumber,
        originCountry: formValue.originCountry,
        originCity: formValue.originCity,
        recipientsName: formValue.recipientsName,
        recipientsContactNumber: formValue.recipientsContactNumber,
        destinationCountry: formValue.destinationCountry,
        destinationCity: formValue.destinationCity,
        deliveryAddress: formValue.deliveryAddress,
        pickupDate: formattedDate,
        pickupTime: ticketTime,
        status: formValue.status,
        dutyAndTaxesBillTo: formValue.dutyAndTaxesBillTo,
        weight: formValue.weight,
        accountNumber:formValue?.accountNumber?.value,
        amount: formValue.amount,
        content: formValue.content,
        currency: formValue.currency,
        pieces: formValue.pieces,
        serviceType: formValue.serviceType,
        productType: formValue.productType,
        requestType: formValue.requestType,
        serviceTypeCode: this.serviceTypeCode,
        deliveryStreetName: formValue.deliveryStreetName,
        deliveryDistrict: formValue.deliveryDistrict,
        pickupStreetName: formValue.pickupStreetName,
        pickupDistrict: formValue.pickupDistrict,
        createdBy: this.loginUserEmail,
        assignedTo: formValue.requestType == "Pick-up" ? "ROLE_OPERATION_AGENT" : "",
      };

      //   Create Ticket
      this._airbillService.createBill(billData).subscribe((res) => {
        this.checkAddressStatus()
      });
    } else {
      this.alert();
      this.formService.markFormGroupTouched(this.awbForm);
    }
  }

  billPopup(){
    this.success();
    this.router.navigate(["awb/list"]);
    this.awbForm.reset();
  }

  confirmCreateAddress() {
    if (this.addressBookStatus == "Not Exist") {
      this.createRecipientAddress();
      this.createShipperAddress();
      this.billPopup()
    } else if (this.addressBookStatus == "shipper") {
      this.createRecipientAddress();
      this.billPopup();
    } else {
      this.createShipperAddress();
      this.billPopup();
    }
  }

  onCancelAddressRequest(){
    this.addressDialog = false
    this.billPopup();
  }

  checkAddressStatus() {
    const formValue = this.awbForm.value;
    const shipperUniqueId = `${formValue.shipperName}-${formValue.shipperContactNumber}-Shipper`;
    const recipientUniqueId = `${formValue.recipientsName}-${formValue.recipientsContactNumber}-Recipient`;
    this.addressBookService
      .isUniqueIdExists({
        uniqueId: recipientUniqueId,
        shipperUniqueId: shipperUniqueId,
      })
      .subscribe((res: any) => {
        this.addressBookStatus = res.body;

        if (res?.body == "both") {
          this.success();
          this.router.navigate(["awb/list"]);
          this.awbForm.reset();
        } else {
          this.addressDialog = true;
        }
      });
  }

  createShipperAddress() {
    const formValue = this.awbForm.value;
    const shipperData: IAddressBook = {
      name: formValue.shipperName,
      contactNumber: formValue.shipperContactNumber,
      address: formValue.pickupAddress,
      streetName: formValue.pickupStreetName,
      district: formValue.pickupDistrict,
      refNumber: formValue.shipperRefNumber,
      country: formValue.originCountry,
      city: formValue.originCity,
      userType: "Shipper",
      createdBy: this.loginUserEmail,
    };

    this.addressBookService.create(shipperData).subscribe((res: any) => {});
  }

  createRecipientAddress() {
    const formValue = this.awbForm.value;
    const recipientData: IAddressBook = {
      name: formValue.recipientsName,
      contactNumber: formValue.recipientsContactNumber,
      address: formValue.deliveryAddress,
      streetName: formValue.deliveryStreetName,
      district: formValue.deliveryDistrict,
      country: formValue.destinationCountry,
      city: formValue.destinationCity,
      userType: "Recipient",
      createdBy: this.loginUserEmail,
    };

    this.addressBookService.create(recipientData).subscribe((res: any) => {});
  }

  onAutoFillShipperDetails() {
    const params = {
      userType: "Shipper",
      status: true,
    };
    this.addressType = true;
    this.tooltipVisible = true;
    this.addressBookService
      .getAddressBooksByUserType(params)
      .subscribe((res) => {
        if (res && res.body) {
          this.addressBooks = res.body;
        }
      });
    this.visible = true;
  }

  onAutoFillRecipientsDetails() {
    const params = {
      userType: "Recipient",
      status: true,
    };
    this.addressType = false;
    this.tooltipVisible = true;
    this.addressBookService
      .getAddressBooksByUserType(params)
      .subscribe((res) => {
        if (res && res.body) {
          this.addressBooks = res.body;
        }
      });
    this.visible = true;
  }

  onSelectShipperAddress(id: any) {
    this.tooltipVisible = false;
    this.addressBookService.getAddressBookById(id).subscribe((res: any) => {
      this.selectedAddress = res.body;
      if (this.selectedAddress.userType == "Shipper") {
        this.originCityUsingShipperAddress(this.selectedAddress?.country);
        this.awbForm.patchValue({
          shipperName: this.selectedAddress?.name,
          shipperContactNumber: this.selectedAddress?.contactNumber,
          pickupAddress: this.selectedAddress?.address,
          pickupStreetName: this.selectedAddress?.streetName,
          pickupDistrict: this.selectedAddress?.district,
          shipperRefNumber: this.selectedAddress?.refNumber,
          originCity: this.selectedAddress?.city,
          originCountry: this.selectedAddress?.country,
        });

        this.visible = false;
      } else {
        this.destinationCityUsingShipperAddress(this.selectedAddress?.country);
        this.awbForm.patchValue({
          recipientsName: this.selectedAddress?.name,
          recipientsContactNumber: this.selectedAddress?.contactNumber,
          deliveryAddress: this.selectedAddress?.address,
          deliveryStreetName: this.selectedAddress?.streetName,
          deliveryDistrict: this.selectedAddress?.district,
          destinationCity: this.selectedAddress?.city,
          destinationCountry: this.selectedAddress?.country,
        });

        this.visible = false;
      }
    });
  }

  originCityUsingShipperAddress(country: any) {
    this.cityService
      .getAllCitiesByCountryName(country)
      .subscribe((res: any) => {
        this.originCities = res;
        this.originCities = this.dropdownService.extractNames(
          this.originCities
        );
      });
  }

  destinationCityUsingShipperAddress(country: any) {
    this.cityService
      .getAllCitiesByCountryName(country)
      .subscribe((res: any) => {
        this.destinationCities = res;
        this.destinationCities = this.dropdownService.extractNames(
          this.destinationCities
        );
      });
  }

  onCancel() {
    this.router.navigate(["awb/list"]);
  }

  // Pop up message
  success() {
    this.MessageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket added succesfully",
    });
  }

  update() {
    this.MessageService.add({
      severity: "success",
      summary: "Success",
      detail: "Ticket Updated succesfully",
    });
  }

  alert() {
    this.MessageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that all required details are filled out.",
    });
  }

  ngOnDestroy(): void {
    // this._airbillService.CreateAWB.next(false);
    // this._airbillService.updateAWB.next(false);
  }
}
