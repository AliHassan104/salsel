import {
  Component,
  OnInit,
  OnDestroy,
  ElementRef,
  ViewChild,
} from "@angular/core";
import { MenuItem } from "primeng/api";
import { Product } from "../../Api/product";
import { Subscription, finalize } from "rxjs";
import { TicktingService } from "../Tickets/service/tickting.service";
import { AirbillService } from "../awb/service/airbill.service";
import { AccountService } from "../accounts/service/account.service";
import { SessionStorageService } from "../auth/service/session-storage.service";
import { UserService } from "../auth/usermanagement/service/user.service";
import { Table } from "primeng/table";

@Component({
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.scss"],
})
export class DashboardComponent implements OnInit, OnDestroy {
  activeTicketsCount?;
  inActiveTicketsCount?;
  totalTickets?;
  activeawb?;
  inactiveAwb?;
  awbCount;
  activeAccountCount?;
  inactiveAccountCount?;
  totalAccountCount?;
  awbData;

  pieData;
  pieOptions;
  pieDataAwb;
  pieDataAccount;
  items!: MenuItem[];

  products!: Product[];

  chartData: any;

  chartOptions: any;

  subscription!: Subscription;
  refresh: boolean = true;

  @ViewChild("filter") filter!: ElementRef;

  constructor(
    private ticketService: TicktingService,
    private airbillService: AirbillService,
    private accountsService: AccountService,
    public sessionStorageService: SessionStorageService
  ) {}

  ngOnInit() {
    this.getDataForPieCharts();
  }

  getDataForPieCharts() {
    const params = { status: true };
    if (
      this.sessionStorageService.getRoleName() == "ADMIN" ||
      this.sessionStorageService.getRoleName() == "CUSTOMER_SERVICE_AGENT"
    ) {
      // Air Bill
      this.airbillService
        .getAwbStatusCount()
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((awb: any) => {
          this.pieChartAwbSetup(
            awb?.awbCreated,
            awb?.picked,
            awb?.arrivedInStation,
            awb?.heldInStation,
            awb?.departFromStation,
            awb?.arrivedInHub,
            awb?.departFromHub,
            awb?.outForDelivery,
            awb?.delivered
          );
        });

      this.airbillService.getStatusCount().subscribe((awb: any) => {
        this.activeawb = awb?.active;
        this.inactiveAwb = awb?.inactive;
        this.awbCount = this.activeawb + this.inactiveAwb;
      });

      this.airbillService.getBills(params).subscribe((res: any) => {
        this.awbData = res;
      });

      // Ticket
      this.ticketService.getStatusCount().subscribe((ticket: any) => {
        this.pieChartTicketSetup(ticket?.active, ticket?.inactive);
        this.activeTicketsCount = ticket?.active;
        this.inActiveTicketsCount = ticket?.inactive;
        this.totalTickets = this.activeTicketsCount + this.inActiveTicketsCount;
      });

      //   Account
      this.accountsService.getStatusCount().subscribe((account: any) => {
        this.pieChartAccountSetup(account?.active, account?.inactive);
        this.activeAccountCount = account?.active;
        this.inactiveAccountCount = account?.inactive;
        this.totalAccountCount =
          this.activeAccountCount + this.inactiveAccountCount;
      });
    } else {
      // Air Bill Details
      this.airbillService
        .getAwbStatusByLoggedInUser()
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((awb: any) => {
          this.pieChartAwbSetup(
            awb?.awbCreated,
            awb?.picked,
            awb?.arrivedInStation,
            awb?.heldInStation,
            awb?.departFromStation,
            awb?.arrivedInHub,
            awb?.departFromHub,
            awb?.outForDelivery,
            awb?.delivered
          );
        });

      this.airbillService
        .getStatusCountByLoggedInUser()
        .subscribe((awb: any) => {
          this.activeawb = awb?.active;
          this.inactiveAwb = awb?.inactive;
          this.awbCount = this.activeawb + this.inactiveAwb;
        });

      this.airbillService
        .getBillsByUserEmailAndRole(params)
        .subscribe((res: any) => {
          this.awbData = res;
        });

      // Ticket
      this.ticketService
        .getStatusCountByLoggedInUser()
        .subscribe((ticket: any) => {
          this.pieChartTicketSetup(ticket?.active, ticket?.inactive);
          this.activeTicketsCount = ticket.active;
          this.inActiveTicketsCount = ticket.inactive;
          this.totalTickets =
            this.activeTicketsCount + this.inActiveTicketsCount;
        });

      //   Account
      this.accountsService
        .getStatusCountByLoggedInUser()
        .subscribe((account: any) => {
          this.pieChartAccountSetup(account?.active, account?.inactive);
          this.activeAccountCount = account?.active;
          this.inactiveAccountCount = account?.inactive;
          this.totalAccountCount =
            this.activeAccountCount + this.inactiveAccountCount;
        });
    }
  }

  onRefresh() {
    this.getDataForPieCharts();
  }

  pieChartAccountSetup(active, inactive) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue("--text-color");

    this.pieDataAccount = {
      labels: ["Active", "InActive"],
      datasets: [
        {
          data: [active, inactive],
          backgroundColor: ["#d35400", "#8e44ad"],
          hoverBackgroundColor: ["#e74c3c", "#9b59b6"],
        },
      ],
    };

    this.pieOptions = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor,
          },
        },
      },
    };
  }

  pieChartAwbSetup(
    awbCreated,
    picked,
    arrivedInStation,
    heldInStation,
    departFromStation,
    arrivedInHub,
    departFromHub,
    outForDelivery,
    delivered
  ) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue("--text-color");

    this.pieDataAwb = {
      labels: [
        "Awb Created",
        "Picked Up",
        "Arrived In Station",
        "Held In Station",
        "Depart From Station",
        "Arrived In Hub",
        "Depart From Hub",
        "Out For Delivery",
        "Delivered",
      ],
      datasets: [
        {
          data: [
            awbCreated,
            picked,
            arrivedInStation,
            heldInStation,
            departFromStation,
            arrivedInHub,
            departFromHub,
            outForDelivery,
            delivered,
          ],
          backgroundColor: [
            "rgba(10, 148, 255, 1)",
            "rgba(220, 96, 239, 1)",
            "rgba(137, 91, 241, 1)",
            "rgba(153, 178, 198, 1)",
            "rgba(243, 101, 74, 1)",
            "rgba(0, 186, 52, 1)",
            "rgba(255, 193, 7, 1)",
            "rgba(128, 0, 128, 1)",
            "rgba(255, 0, 0, 1)",
          ],
          hoverBackgroundColor: [
            "rgba(10, 148, 255, 0.9)",
            "rgba(220, 96, 239, 0.9)",
            "rgba(137, 91, 241, 0.9)",
            "rgba(153, 178, 198, 0.9)",
            "rgba(243, 101, 74, 0.9)",
            "rgba(0, 186, 52, 0.9)",
            "rgba(255, 193, 7, 0.9)",
            "rgba(128, 0, 128, 0.9)",
            "rgba(255, 0, 0, 0.9)",
          ],
        },
      ],
    };

    this.pieOptions = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor,
          },
        },
      },
    };
  }

  pieChartTicketSetup(active, inactive) {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue("--text-color");

    this.pieData = {
      labels: ["Active", "InActive"],
      datasets: [
        {
          data: [active, inactive],
          backgroundColor: ["#9b59b6", "#f39c12"],
          hoverBackgroundColor: ["#8e44ad", "#d35400"],
        },
      ],
    };

    this.pieOptions = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor,
          },
        },
      },
    };
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, "contains");
  }

  clear(table: Table) {
    table.clear();
    this.filter.nativeElement.value = "";
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
