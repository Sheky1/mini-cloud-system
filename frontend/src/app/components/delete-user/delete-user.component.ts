import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.css'],
})
export class DeleteUserComponent implements OnInit {
  deleteUserForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService
  ) {
    this.authService.checkPermissions('can_delete_users');
    this.deleteUserForm = this.formBuilder.group({
      id: ['', Validators.required],
    });
  }

  ngOnInit(): void {}

  deleteUser() {
    this.userService.deleteUser(this.deleteUserForm.get('id')?.value);
    this.deleteUserForm.reset();
  }
}
