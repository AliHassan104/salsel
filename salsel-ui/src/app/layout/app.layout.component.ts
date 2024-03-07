import {
  Component,
  OnDestroy,
  Renderer2,
  ViewChild,
  OnInit,
  HostListener,
  ElementRef,
} from "@angular/core";
import { NavigationEnd, Router } from "@angular/router";
import { filter, Subscription } from "rxjs";
import { LayoutService } from "./service/app.layout.service";
import { AppSidebarComponent } from "./app.sidebar.component";
import { AppTopBarComponent } from "./app.topbar.component";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MessageService } from "primeng/api";
import { FormvalidationService } from "../components/Tickets/service/formvalidation.service";
import { AirbillService } from "../components/awb/service/airbill.service";
import { DropdownService } from "./service/dropdown.service";

@Component({
  selector: "app-layout",
  templateUrl: "./app.layout.component.html",
  styleUrls: ["./app.layout.scss"],
  providers: [MessageService],
})
export class AppLayoutComponent implements OnDestroy, OnInit {
  codeScan: boolean = false;
  uniqueScanNum;
  scanOptions;
  productField;
  scanForm!: FormGroup;
  @ViewChild("beepSound") beepSound: ElementRef<HTMLAudioElement>;

  overlayMenuOpenSubscription: Subscription;

  menuOutsideClickListener: any;

  profileMenuOutsideClickListener: any;

  @ViewChild(AppSidebarComponent) appSidebar!: AppSidebarComponent;

  @ViewChild(AppTopBarComponent) appTopbar!: AppTopBarComponent;

  constructor(
    public layoutService: LayoutService,
    public renderer: Renderer2,
    public router: Router,
    private messageService: MessageService,
    private dropdownService: DropdownService,
    private _airbillService: AirbillService,
    private formService: FormvalidationService
  ) {
    this.overlayMenuOpenSubscription =
      this.layoutService.overlayOpen$.subscribe(() => {
        if (!this.menuOutsideClickListener) {
          this.menuOutsideClickListener = this.renderer.listen(
            "document",
            "click",
            (event) => {
              const isOutsideClicked = !(
                this.appSidebar.el.nativeElement.isSameNode(event.target) ||
                this.appSidebar.el.nativeElement.contains(event.target) ||
                this.appTopbar.menuButton.nativeElement.isSameNode(
                  event.target
                ) ||
                this.appTopbar.menuButton.nativeElement.contains(event.target)
              );

              if (isOutsideClicked) {
                this.hideMenu();
              }
            }
          );
        }

        if (!this.profileMenuOutsideClickListener) {
          this.profileMenuOutsideClickListener = this.renderer.listen(
            "document",
            "click",
            (event) => {
              const isOutsideClicked = !(
                this.appTopbar.menu.nativeElement.isSameNode(event.target) ||
                this.appTopbar.menu.nativeElement.contains(event.target) ||
                this.appTopbar.topbarMenuButton.nativeElement.isSameNode(
                  event.target
                ) ||
                this.appTopbar.topbarMenuButton.nativeElement.contains(
                  event.target
                )
              );

              if (isOutsideClicked) {
                this.hideProfileMenu();
              }
            }
          );
        }

        if (this.layoutService.state.staticMenuMobileActive) {
          this.blockBodyScroll();
        }
      });

    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        this.hideMenu();
        this.hideProfileMenu();
      });
  }

  ngOnInit(): void {
    this.scanForm = new FormGroup({
      updatedStatus: new FormControl(null, [Validators.required]),
    });

    this.getAllProductFields();
  }

  getAllProductFields() {
    this.dropdownService.getAllProductFields().subscribe((res) => {
      this.productField = res;

      this.scanOptions = this.dropdownService.extractNames(
        this.productField.filter((data) => data?.name == "Awb Status")[0]
          .productFieldValuesList
      );
    });
  }

  onCloseScan() {
    this.uniqueScanNum = null;
    this.scannedData = "";
  }

  onUpdateStatus(data: any) {
    if (this.scanForm.valid) {
      this._airbillService
        .updateAwbTrackingStatus(data?.updatedStatus, this.uniqueScanNum)
        .subscribe((res) => {
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Status Updated",
          });
          this.scanForm.reset();
          this.scannedData = "";
          this.codeScan = false;
        });
    } else {
      this.formService.markFormGroupTouched(this.scanForm);
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please ensure that required Field is filled out.",
      });
    }
  }

  scannedData: string = "";
  codeLength: number = 9;

  @HostListener("document:keydown", ["$event"])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (
      event.key.length === 1 &&
      !event.ctrlKey &&
      !event.altKey &&
      !event.metaKey
    ) {
      // Append the scanned character to the scanned data
      this.scannedData += event.key;
      this.uniqueScanNum = this.scannedData;
      this.codeScan = true;

      if(this.scannedData.length == 8){
         this.beepSound.nativeElement.play();
      }
      // Reset scannedData if it reaches the specified length
      if (this.scannedData.length >= this.codeLength) {
        this.scannedData = "";
      }
    }
  }

  hideMenu() {
    this.layoutService.state.overlayMenuActive = false;
    this.layoutService.state.staticMenuMobileActive = false;
    this.layoutService.state.menuHoverActive = false;
    if (this.menuOutsideClickListener) {
      this.menuOutsideClickListener();
      this.menuOutsideClickListener = null;
    }
    this.unblockBodyScroll();
  }

  hideProfileMenu() {
    this.layoutService.state.profileSidebarVisible = false;
    if (this.profileMenuOutsideClickListener) {
      this.profileMenuOutsideClickListener();
      this.profileMenuOutsideClickListener = null;
    }
  }

  blockBodyScroll(): void {
    if (document.body.classList) {
      document.body.classList.add("blocked-scroll");
    } else {
      document.body.className += " blocked-scroll";
    }
  }

  unblockBodyScroll(): void {
    if (document.body.classList) {
      document.body.classList.remove("blocked-scroll");
    } else {
      document.body.className = document.body.className.replace(
        new RegExp(
          "(^|\\b)" + "blocked-scroll".split(" ").join("|") + "(\\b|$)",
          "gi"
        ),
        " "
      );
    }
  }

  get containerClass() {
    return {
      "layout-theme-light": this.layoutService.config.colorScheme === "light",
      "layout-theme-dark": this.layoutService.config.colorScheme === "dark",
      "layout-overlay": this.layoutService.config.menuMode === "overlay",
      "layout-static": this.layoutService.config.menuMode === "static",
      "layout-static-inactive":
        this.layoutService.state.staticMenuDesktopInactive &&
        this.layoutService.config.menuMode === "static",
      "layout-overlay-active": this.layoutService.state.overlayMenuActive,
      "layout-mobile-active": this.layoutService.state.staticMenuMobileActive,
      "p-input-filled": this.layoutService.config.inputStyle === "filled",
      "p-ripple-disabled": !this.layoutService.config.ripple,
    };
  }

  ngOnDestroy() {
    if (this.overlayMenuOpenSubscription) {
      this.overlayMenuOpenSubscription.unsubscribe();
    }

    if (this.menuOutsideClickListener) {
      this.menuOutsideClickListener();
    }
  }
}
