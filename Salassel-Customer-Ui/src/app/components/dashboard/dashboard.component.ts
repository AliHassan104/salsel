import { Component, OnInit, OnDestroy } from "@angular/core";
import { MenuItem } from "primeng/api";
import { Product } from "../../Api/product";
import { Subscription, finalize } from "rxjs";
import { TicktingService } from "../Tickets/service/tickting.service";
import { AirbillService } from "../awb/service/airbill.service";
import { AccountService } from "../accounts/service/account.service";
import { SessionStorageService } from "../auth/service/session-storage.service";
import { UserService } from "../auth/usermanagement/service/user.service";

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

  newTicketsSinceLast24Hours;

  pieData;
  pieOptions;
  pieDataAwb;
  pieDataAccount;
  items!: MenuItem[];

  products!: Product[];

  chartData: any;

  chartOptions: any;
  refresh: boolean = true;

  subscription!: Subscription;

  loginUserName;
  loginUserEmail;
  loginUser;

  constructor(
    private ticketService: TicktingService,
    private airbillService: AirbillService,
    private accountsService: AccountService,
    public sessionStorageService: SessionStorageService,
    private userService: UserService
  ) {
    this.userService.loginUserName.subscribe((res) => {
      this.loginUserName = res;
    });

    this.userService.loginUserEmail.subscribe((res) => {
      this.loginUserEmail = res;
    });

    this.userService.loginUserEmail.subscribe((res) => {
      this.loginUser = res;
    });
  }

  ngOnInit() {
    this.getAllInfo();
    this.pieChartSetup();
  }

  onRefresh() {
    this.getAllInfo();
  }

  getAllInfo() {
    const twentyFourHoursAgo = new Date();
    twentyFourHoursAgo.setDate(twentyFourHoursAgo.getDate() - 1);

    if (
      this.sessionStorageService.getRoleName() == "ADMIN" ||
      this.sessionStorageService.getRoleName() == "CUSTOMER_SERVICE_AGENT"
    ) {
      this.ticketService
        .getTickets({ status: true })
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          this.activeTicketsCount = res?.body?.length;

          this.ticketService
            .getTickets({ status: false })
            .subscribe((res: any) => {
              this.inActiveTicketsCount = res?.body?.length;
              this.totalTickets =
                this.activeTicketsCount + this.inActiveTicketsCount;

              this.pieChartSetup();
            });
        });

      this.airbillService.getBills({ status: true }).subscribe((res: any) => {
        this.activeawb = res.length;
        this.airbillService
          .getBills({ status: false })
          .subscribe((res: any) => {
            this.inactiveAwb = res.length;
            this.awbCount = this.activeawb + this.inactiveAwb;
            this.pieChartAwbSetup();
          });
      });

      this.accountsService
        .getAllAccounts({ status: true })
        .subscribe((res: any) => {
          this.activeAccountCount = res?.body?.length;

          this.accountsService
            .getAllAccounts({ status: false })
            .subscribe((res: any) => {
              this.inactiveAccountCount = res?.body?.length;
              this.totalAccountCount =
                this.activeAccountCount + this.inactiveAccountCount;

              this.pieChartAccountSetup();
            });
        });
    } else {
      this.ticketService
        .getTicketsByLoggedInUserAndRole({ status: true })
        .pipe(
          finalize(() => {
            this.refresh = false;
          })
        )
        .subscribe((res: any) => {
          this.activeTicketsCount = res?.body?.length;

          this.ticketService
            .getTicketsByLoggedInUserAndRole({ status: false })
            .subscribe((res: any) => {
              this.inActiveTicketsCount = res?.body?.length;
              this.totalTickets =
                this.activeTicketsCount + this.inActiveTicketsCount;

              this.pieChartSetup();
            });
        });

      this.airbillService
        .getBillsByUserEmailAndRole({ status: true })
        .subscribe((res: any) => {
          this.activeawb = res.length;
          this.airbillService
            .getBillsByUserEmailAndRole({ status: false })
            .subscribe((res: any) => {
              this.inactiveAwb = res.length;
              this.awbCount = this.activeawb + this.inactiveAwb;
              this.pieChartAwbSetup();
            });
        });

      this.accountsService
        .getAllAccountsByUserLoggedIn({ status: true })
        .subscribe((res: any) => {
          this.activeAccountCount = res?.body?.length;

          this.accountsService
            .getAllAccountsByUserLoggedIn({ status: false })
            .subscribe((res: any) => {
              this.inactiveAccountCount = res?.body?.length;
              this.totalAccountCount =
                this.activeAccountCount + this.inactiveAccountCount;

              this.pieChartAccountSetup();
            });
        });
    }
  }

  pieChartAccountSetup() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue("--text-color");

    this.pieDataAccount = {
      labels: ["Active", "InActive"],
      datasets: [
        {
          data: [this.activeAccountCount, this.inactiveAccountCount],
          backgroundColor: ["#8e44ad", "#d35400"],
          hoverBackgroundColor: ["#9b59b6", "#e74c3c"],
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

  pieChartAwbSetup() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue("--text-color");

    this.pieDataAwb = {
      labels: ["Active", "InActive"],
      datasets: [
        {
          data: [this.activeawb, this.inactiveAwb],
          backgroundColor: ["#2ecc71", "#e67e22"],
          hoverBackgroundColor: ["#27ae60", "#d35400"],
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

  pieChartSetup() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue("--text-color");

    this.pieData = {
      labels: ["Active", "InActive"],
      datasets: [
        {
          data: [this.activeTicketsCount, this.inActiveTicketsCount],
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

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
