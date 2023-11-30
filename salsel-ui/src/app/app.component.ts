import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { PrimeNGConfig } from "primeng/api";
import { LoaderService } from "./service/loader.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
})
export class AppComponent implements OnInit {
  constructor(
    private primengConfig: PrimeNGConfig,
    public loaderService: LoaderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loaderService.isLoading$.subscribe(() => {
      this.cdr.detectChanges();
    });
  }
}
