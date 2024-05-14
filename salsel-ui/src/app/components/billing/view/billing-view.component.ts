import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AccountService } from '../../accounts/service/account.service';
import { SessionStorageService } from '../../auth/service/session-storage.service';
import { IEmployee } from '../../hr-module/model/employeeDto';
import { HrModuleService } from '../../hr-module/service/hr-module.service';
import { TicketCommentsService } from '../../Tickets/service/ticket-comments.service';
import { TicktingService } from '../../Tickets/service/tickting.service';
import { BillingService } from '../service/billing.service';
import { IBilling } from '../model/billingDto';

@Component({
  selector: "app-billing-view",
  templateUrl: "./billing-view.component.html",
  styleUrls: ["./billing-view.component.scss"],
  providers:[MessageService]
})
export class BillingViewComponent {
  visible: boolean = false;
  @ViewChild("textArea") textArea!: ElementRef;

  editMode: any;
  refresh: boolean = true;
  passportImageSrc: string;
  idImageSrc: string;
  constructor(
    private employeeService: HrModuleService,
    private activatedRoute: ActivatedRoute,
    public sessionStorageService: SessionStorageService,
    private billingService:BillingService
  ) {}


  display: any;
  singleInvoice: IBilling;
  id: any;

  loading: any;
  employeeAttachments;
  postCommentForm!: FormGroup;

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      this.id = a;
      this.onView(a);
    });
  }

  onView(id){
    this.billingService.getInvoiceById(id).subscribe((res:any)=>{
        this.singleInvoice = res.body;
    })
  }










  onCancel() {
    this.editMode = false;
    this.postCommentForm.reset();
  }
}
