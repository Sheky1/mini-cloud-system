import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css'],
})
export class CreateUserComponent implements OnInit {
  createUserForm: FormGroup;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) {
    this.authService.checkPermissions('can_create_users');
    this.createUserForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
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

  createUser() {
    let permissions = [];
    if (this.createUserForm.get('can_read_users')?.value == true)
      permissions.push('can_read_users');
    if (this.createUserForm.get('can_create_users')?.value == true)
      permissions.push('can_create_users');
    if (this.createUserForm.get('can_update_users')?.value == true)
      permissions.push('can_update_users');
    if (this.createUserForm.get('can_delete_users')?.value == true)
      permissions.push('can_delete_users');
    if (this.createUserForm.get('can_search_machines')?.value == true)
      permissions.push('can_search_machines');
    if (this.createUserForm.get('can_start_machines')?.value == true)
      permissions.push('can_start_machines');
    if (this.createUserForm.get('can_stop_machines')?.value == true)
      permissions.push('can_stop_machines');
    if (this.createUserForm.get('can_restart_machines')?.value == true)
      permissions.push('can_restart_machines');
    if (this.createUserForm.get('can_create_machines')?.value == true)
      permissions.push('can_create_machines');
    if (this.createUserForm.get('can_destroy_machines')?.value == true)
      permissions.push('can_destroy_machines');
    let user = {
      firstName: this.createUserForm.get('firstName')?.value,
      lastName: this.createUserForm.get('lastName')?.value,
      email: this.createUserForm.get('email')?.value,
      password: this.createUserForm.get('password')?.value,
      permissions,
    };
    this.userService.createUser(user);
    this.createUserForm.reset();
  }
}
