import { Component } from "@angular/core";
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import { MessageService } from "primeng/api";
import { RolesService } from "./service/roles.service";
import { IPermissions } from "./model/permissionDto";
import { IRole } from "./model/roleDto";
import { ActivatedRoute } from "@angular/router";
import { SessionStorageService } from "../auth/service/session-storage.service";

@Component({
  selector: "app-permissions",
  templateUrl: "./permissions.component.html",
  styleUrls: ["./permissions.component.scss"],
  providers: [MessageService],
})
export class PermissionsComponent {
  permissionsForm!: FormGroup;
  roles?;
  permissions?: any = [];
  selectedRoleId?;
  roleName?;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private roleService: RolesService,
    private route: ActivatedRoute,
    private SessionStorageService: SessionStorageService
  ) {}

  ngOnInit(): void {
    this.formSetup();

    this.getAllRoles();
    this.getAllPermissions();
  }

  getAllRoles() {
    this.roleService.getRoles().subscribe((res) => {
      this.roles = res;
    });
  }

  getAllPermissions() {
    this.roleService.getPermissions().subscribe((res: any) => {
      if (res) {
        this.permissions = res;
        this.permissions = this.permissions.map((value: any) => {
          return {
            id: value.id,
            name: value.name,
            value: false,
          };
        });
      }
    });
  }

  formSetup() {
    this.permissionsForm = this.fb.group({
      role: ["", [Validators.required]],
      permissions: this.fb.array([]),
    });
  }

  getRoleName() {
    this.roles
      .filter((value) => value.id == this.selectedRoleId)
      .map((value) => (this.roleName = value.name));

    return this.roleName;
  }

  addPermission(permission: IPermissions) {
    let perm = this.permissions.find((value: any) => {
      value.id == permission.id;
    });
    if (perm) {
      console.log(perm);
      perm.value = !permission.value;
    }
  }

  onRoleChange(data) {
    this.selectedRoleId = data.value.id;
    this.permissions = this.permissions.map((p: any) => {
      return {
        ...p,
        value: false,
      };
    });

    this.roleService
      .getPermissionOfRoles(this.selectedRoleId)
      .subscribe((res: any) => {
        res.permissions.forEach((perm: any) => {
          const permission = this.permissions.find(
            (value: any) => value.id === perm.id
          );
          if (permission) {
            permission.value = true;
          }
        });
      });
  }

  onSubmit() {
    if (this.permissionsForm.valid) {
      const rolePermissions: IRole = {
        id: this.selectedRoleId,
        name: this.getRoleName(),
        permissions: this.permissions,
      };

      this.roleService
        .updatePermissionOfRoles(rolePermissions)
        .subscribe((res) => {
          this.getAllRoles();
          this.permissionsForm.reset();
          this.getAllPermissions();
        });
    } else {
      this.alert();
    }
  }

  alert() {
    this.messageService.add({
      severity: "error",
      summary: "Warning",
      detail: "Please ensure that you have selected the role",
    });
  }
  success() {
    this.messageService.add({
      severity: "success",
      summary: "success",
      detail: "Permission Updated Successfully",
    });
  }
}
