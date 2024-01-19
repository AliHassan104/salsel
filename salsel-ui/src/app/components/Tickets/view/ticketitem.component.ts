import {
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  OnDestroy,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { TicktingService } from "src/app/components/Tickets/service/tickting.service";
import { AirbillService } from "../../awb/service/airbill.service";
import { SessionStorageService } from "../../auth/service/session-storage.service";
import { TicketCommentsService } from "../service/ticket-comments.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../service/formvalidation.service";
import { AccountService } from "../../accounts/service/account.service";
import { DomSanitizer, SafeResourceUrl } from "@angular/platform-browser";
import { finalize } from "rxjs";

@Component({
  selector: "app-ticketitem",
  templateUrl: "./ticketitem.component.html",
  styleUrls: ["./ticketitem.component.scss"],
  providers: [MessageService],
})
export class TicketitemComponent implements OnInit {
  visible: boolean = false;
  @ViewChild("textArea") textArea!: ElementRef;

  editMode: any;
  refresh: boolean = true;
  constructor(
    private activatedRoute: ActivatedRoute,
    private _ticketService: TicktingService,
    private router: Router,
    private _airbillService: AirbillService,
    public sessionStorageService: SessionStorageService,
    private commentsService: TicketCommentsService,
    private messageServie: MessageService,
    private formService: FormvalidationService,
    private accountService: AccountService,
    private sanitizer: DomSanitizer
  ) {}

  imageUrl: SafeResourceUrl;
  pdfUrl: SafeResourceUrl;

  loginUserName?;
  loginUserEmail?;
  loginUser;

  display: any;
  singleTicket: any;
  id: any;

  ticketComments?;
  singleComment?;
  commentCount?;
  loading: any;
  ticketAttachments;
  postCommentForm!: FormGroup;

  ngOnInit(): void {
    this.formSetup();
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      this.id = a;
      this.onView(a);
    });

    this.getAllTicketComments();
  }

  onRefresh() {
    this.getAllTicketComments();
  }

  formSetup() {
    this.postCommentForm = new FormGroup({
      postComment: new FormControl(null, Validators.required),
    });
  }

  getAllTicketComments() {
    this.commentsService
      .getAllTicketCommentsByTicketId(this.id)
      .pipe(
        finalize(() => {
          this.refresh = false;
        })
      )
      .subscribe((res: any) => {
        this.ticketComments = res;
        this.commentCount = this.ticketComments?.length;
      });
  }

  //   On Single Ticket View
  onView(id) {
    this.display = true;
    this._ticketService.getSingleTicket(id).subscribe((res: any) => {
      this.singleTicket = res;
      this.ticketAttachments = this.singleTicket?.attachments;
    });
  }

  onEdit(id) {
    this.commentsService.getTicketCommentById(id).subscribe((res: any) => {
      this.editMode = true;
      this.singleComment = res;
      this.textArea.nativeElement.focus();
      this.postCommentForm.setValue({
        postComment: this.singleComment?.comment,
      });
    });
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

  onPostComment() {
    if (this.postCommentForm.valid) {
      let data = {
        comment: this.postCommentForm.value.postComment,
        name: localStorage.getItem("loginUserName"),
        ticket: { id: this.id },
      };
      if (this.editMode) {
        this.commentsService
          .updateTicketComment(this.singleComment.id, data)
          .subscribe((res: any) => {
            this.getAllTicketComments();
            this.messageServie.add({
              severity: "success",
              summary: "Success",
              detail: "Sucessfully Updated",
            });
            this.editMode = false;
            this.postCommentForm.reset();
          });
      } else {
        this.commentsService.createTicketComment(data).subscribe((res: any) => {
          this.messageServie.add({
            severity: "success",
            summary: "Success",
            detail: "Posted Successfully",
          });
          this.getAllTicketComments();
          this.postCommentForm.reset();
        });
      }
    } else {
      this.formService.markFormGroupTouched(this.postCommentForm);
      this.messageServie.add({
        severity: "error",
        summary: "Error",
        detail: "Please Fill the Required Field",
      });
    }
  }

  onDelete(id) {
    this.commentsService.deleteTicketComment(id).subscribe((res: any) => {
      this.getAllTicketComments();
      this.messageServie.add({
        severity: "success",
        summary: "Success",
        detail: "Deleted Successfully",
      });
    });
  }

  updateTicket(id) {
    const queryParams = { updateMode: "true", id: id };
    this.router.navigate(["create-ticket"], {
      queryParams: queryParams,
    });
  }

  createAwb() {
    this._airbillService.CreateAWB.next(true);
    this.activatedRoute.paramMap.subscribe((res) => {
      var a = res.get("id");
      const queryParams = { id: a };
      this.router.navigate(["create-awb"], {
        queryParams: queryParams,
      });
    });
  }

  getPriorityClass(): string {
    switch (this.singleTicket?.ticketFlag) {
      case "Normal":
        return "normal";
      case "Urgent":
        return "urgent";
      case "Priority":
        return "priority";
      case "Extreme Urgent":
        return "extreme-urgent";
      default:
        return "";
    }
  }

  getStatusClass(): string {
    switch (this.singleTicket?.ticketStatus) {
      case "Open":
        return "open-status";
      case "Closed":
        return "closed-status";
      case "On-Hold":
        return "on-hold-status";
      case "Under Process":
        return "underprocess-status";
      case "Overdue Escalation":
        return "overdue-escalation-status";
      case "Held-FI":
        return "held-fi-status";
      default:
        return "";
    }
  }
}
