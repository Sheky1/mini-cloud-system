import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css'],
})
export class UpdateUserComponent implements OnInit {
  updateUserForm: FormGroup;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) {
    this.authService.checkPermissions('can_update_users');
    this.updateUserForm = this.formBuilder.group({
      id: ['', Validators.required],
      firstName: [],
      lastName: [],
      email: ['', Validators.email],
      password: [],
      can_read_users: [],
      can_create_users: [],
      can_update_users: [],
      can_delete_users: [],
      can_search_machines: [],
      can_start_machines: [],
      can_stop_machines: [],
      can_restart_machines: [],
      can_create_machines: [],
      can_destroy_machines: [],
    });
  }

  ngOnInit(): void {}

  updateUser() {
    let permissions = [];
    if (this.updateUserForm.get('can_read_users')?.value == true)
      permissions.push('can_read_users');
    if (this.updateUserForm.get('can_create_users')?.value == true)
      permissions.push('can_create_users');
    if (this.updateUserForm.get('can_update_users')?.value == true)
      permissions.push('can_update_users');
    if (this.updateUserForm.get('can_delete_users')?.value == true)
      permissions.push('can_delete_users');
    if (this.updateUserForm.get('can_search_machines')?.value == true)
      permissions.push('can_search_machines');
    if (this.updateUserForm.get('can_start_machines')?.value == true)
      permissions.push('can_start_machines');
    if (this.updateUserForm.get('can_stop_machines')?.value == true)
      permissions.push('can_stop_machines');
    if (this.updateUserForm.get('can_restart_machines')?.value == true)
      permissions.push('can_restart_machines');
    if (this.updateUserForm.get('can_create_machines')?.value == true)
      permissions.push('can_create_machines');
    if (this.updateUserForm.get('can_destroy_machines')?.value == true)
      permissions.push('can_destroy_machines');
    let user = {
      firstName:
        this.updateUserForm.get('firstName')?.value == null
          ? ''
          : this.updateUserForm.get('firstName')?.value,
      lastName:
        this.updateUserForm.get('lastName')?.value == null
          ? ''
          : this.updateUserForm.get('lastName')?.value,
      email:
        this.updateUserForm.get('email')?.value == null
          ? ''
          : this.updateUserForm.get('email')?.value,
      password:
        this.updateUserForm.get('password')?.value == null
          ? ''
          : this.updateUserForm.get('password')?.value,
      permissions,
    };
    console.log(user);
    this.userService.updateUser(user, this.updateUserForm.get('id')?.value);
    this.updateUserForm.reset();
  }
}
