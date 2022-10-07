import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-read-users',
  templateUrl: './read-users.component.html',
  styleUrls: ['./read-users.component.css'],
})
export class ReadUsersComponent implements OnInit {
  users: User[] = [];

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {
    this.authService.checkPermissions('can_read_users');
    this.userService.loadAllUsers().subscribe((data) => {
      this.users = data;
    });
  }

  ngOnInit(): void {}
}
