import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DepartmentService } from 'src/app/service/department.service';

@Component({
  selector: 'app-department-form',
  templateUrl: './department-form.component.html',
  styleUrls: ['./department-form.component.scss']
})
export class DepartmentFormComponent {
  editMode;
  editId;
  private _department: Object;
  constructor(
    private departmentService: DepartmentService,
    private router: Router,
    private http: HttpClient
  ) {
    this.departmentService.editTicketMode.subscribe((res) => {
      this.editMode = res;
    });

    this.departmentService.editId.subscribe((res) => {
      this.editId = res;
    });
  }
  DepartmentForm!: FormGroup;
  selectedCurrency: string;

  ngOnInit(): void {
    this.DepartmentForm = new FormGroup({
        name: new FormControl(null),
    });

    if (this.editId === "") {
      console.log("hello");
      console.log(this.editId);
    } else {
      this.departmentService.getById(this.editId).subscribe((res) => {
        console.log(res);
        
        // this._department = res;
        // let sd = this._department;

        // this.ticketForm.setValue({
        //     name: sd.name,
        // });
      });
    }
  }

  onSubmit(data: any) {
    if (this.editMode) {
      this.router.navigate(["department"]);
      // this.http
      //   .put<any>(`http://localhost:5000/api/v1/notes/${this.editId}`, data)
      //   .subscribe(() => {
      //     this.router.navigate(["tickets"]);
      //   });
      this.departmentService.update(data).subscribe();
      console.log(data);
      this.DepartmentForm.reset();
    } else {
      // Create Ticket
      this.departmentService.save(data).subscribe();
      console.log(data);
      this.DepartmentForm.reset();
    }
  }

  onCancel() {
    this.router.navigate(["tickets"]);
  }

  ngOnDestroy(): void {
    // this._ticketService.editTicketMode.next(false);
    // this._ticketService.editId.next("");
  }
}
