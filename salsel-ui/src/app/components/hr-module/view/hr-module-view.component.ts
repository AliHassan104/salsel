import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AccountService } from '../../accounts/service/account.service';
import { SessionStorageService } from '../../auth/service/session-storage.service';
import { TicketCommentsService } from '../../Tickets/service/ticket-comments.service';
import { TicktingService } from '../../Tickets/service/tickting.service';
import { HrModuleService } from '../service/hr-module.service';
import { IEmployee } from '../model/employeeDto';

@Component({
  selector: "app-hr-module-view",
  templateUrl: "./hr-module-view.component.html",
  styleUrls: ["./hr-module-view.component.scss"],
  providers: [MessageService],
})
export class HrModuleViewComponent {
  visible: boolean = false;
  @ViewChild("textArea") textArea!: ElementRef;

  editMode: any;
  refresh: boolean = true;
    passportImageSrc: string;
    idImageSrc: string;
  constructor(
    private employeeService: HrModuleService,
    private activatedRoute: ActivatedRoute,
    private _ticketService: TicktingService,
    public sessionStorageService: SessionStorageService,
    private commentsService: TicketCommentsService,
    private messageServie: MessageService,
    private accountService: AccountService,
    private sanitizer: DomSanitizer
  ) {}

  imageUrl: SafeResourceUrl;
  pdfUrl: SafeResourceUrl;

  loginUserName?;
  loginUserEmail?;
  loginUser;

  display: any;
  singleEmployee: IEmployee;
  id: any;

  ticketComments?;
  singleComment?;
  commentCount?;
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

  //   On Single Ticket View
  onView(id) {
    this.display = true;
    this.employeeService.getEmployeeById(id).subscribe((res: any) => {
      this.singleEmployee = res.body;

      this.employeeAttachments = this.singleEmployee?.attachments;
      if (this.singleEmployee?.passportFilePath) {
          this.attachCopy(this.singleEmployee?.passportFilePath,"passport")
      }
      if (this.singleEmployee?.idFilePath) {
          this.attachCopy(this.singleEmployee?.idFilePath, "id");
      }
    });
  }

  attachCopy(agreementUrl: string, attachmentType: string) {
    this.accountService.downloadAgreement(agreementUrl).subscribe(
      (blob: Blob) => {
        const fileName = agreementUrl.split("/").pop();
        const file = new File([blob], fileName, {
          type: "application/pdf",
        });

        // Create a FileReader object
        const reader = new FileReader();

        // Define onload callback function
        reader.onload = (event) => {
          const imageUrl = event.target.result as string;

          // Display the image
          switch (attachmentType) {
            case "passport":
              this.passportImageSrc = imageUrl;
              break;
            case "id":
              this.idImageSrc = imageUrl;
              break;
          }
        };

        // Read the file content as data URL
        reader.readAsDataURL(file);
      },
      (error) => {
        this.messageServie.add({
          severity: "error",
          summary: "Error",
          detail: "Error Attaching File",
        });
      }
    );
  }

  onDownloadAttachment(name, id) {
    this.accountService.downloadAgreement(name).subscribe(
      (res: any) => {
        this.downloadSuccess();
        this.accountService.downloadFile(res, name.split("/").pop());
      },
      (error) => {
        this.downloadError();
      }
    );
  }

  downloadError() {
    this.messageServie.add({
      severity: "error",
      summary: "Error",
      detail: "Attachment Not Found",
    });
  }

  downloadSuccess() {
    this.messageServie.add({
      severity: "success",
      summary: "Success",
      detail: "File Successfully Downloaded",
    });
  }

  showImage() {
    this.visible = true;
  }

  onCloseDialog() {
    this.imageUrl = null;
    this.pdfUrl = null;
  }

  onViewAttachment(name) {
    this.accountService.downloadAgreement(name).subscribe(
      (res: any) => {
        const extension = this.getFileExtension(name);

        if (extension === "pdf") {
          const pdfBlob = new Blob([res], { type: "application/pdf" });
          this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
            URL.createObjectURL(pdfBlob)
          );
          this.imageUrl = null;
          this.visible = true;

          if (this.isMobileDevice()) {
            const downloadLink = document.createElement("a");
            downloadLink.href = URL.createObjectURL(pdfBlob);
            downloadLink.target = "_blank";
            downloadLink.download = name.split("/").pop();
            document.body.appendChild(downloadLink);
            downloadLink.click();
            document.body.removeChild(downloadLink);
            this.visible = false;
          }
        } else if (this.isImageExtension(extension)) {
          this.imageUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
            URL.createObjectURL(new Blob([res], { type: "image/" + extension }))
          );
          this.pdfUrl = null;
          this.visible = true;
        }
      },
      (error) => {
        this.downloadError();
      }
    );
  }

  isMobileDevice() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
      navigator.userAgent
    );
  }

  isImageExtension(extension: string): boolean {
    const imageExtensions = ["jpg", "jpeg", "png", "gif", "bmp"];
    return imageExtensions.includes(extension.toLowerCase());
  }

  // Helper function to get the file extension
  getFileExtension(filename: string): string {
    return filename.split(".").pop();
  }

  onCancel() {
    this.editMode = false;
    this.postCommentForm.reset();
  }
}
