import { Component } from '@angular/core';
import { User } from '../model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'projekat-front';
  loggedUser: User;

  constructor(private authService: AuthService) {
    this.loggedUser = this.authService.loggedUser;
  }

  isUserLogged() {
    return this.authService.isLogged;
  }

  logout() {
    this.authService.logout();
  }

  hasPermission(permission: string) {
    return this.authService.hasPermission(permission);
  }
}
