import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Table } from 'primeng/table';
import { ServiceTypeService } from '../service/service-type.service';
import { IServiceType } from 'src/app/api/serviceType.model';

@Component({
  selector: 'app-service-type-list',
  templateUrl: './service-type-list.component.html',
  styleUrls: ['./service-type-list.component.scss']
})
export class ServiceTypeListComponent {
  deleteDialog: any;
  serviceType?: IServiceType[];
  deleteId: any;

  constructor(
    private serviceTypeService: ServiceTypeService,
    private router: Router
  ) {}
  loading: any;
  @ViewChild("filter") filter!: ElementRef;

  ngOnInit(): void {
    this.getServiceTypes();
  }

  getServiceTypes() {
    this.serviceTypeService.getServiceTypes().subscribe((res) => {
      if(res && res.body){
        this.serviceType = res.body;
        console.log(this.serviceType)
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
  confirmDeleteSelected() {
    this.serviceTypeService.removeServiceType(this.deleteId).subscribe((res) => {
      if(res.status == 200){
        this.getServiceTypes();
        this.deleteDialog = false;
      }
    });
  }


  deleteServiceType(id?: any) {
    this.deleteId = id;
    this.deleteDialog = true;
  }

  editServiceType(id?: any) {
    const queryParams = { id: id };
    this.router.navigate(["service-type"], {queryParams: queryParams});
  }
}
  
