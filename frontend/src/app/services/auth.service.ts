import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Token, User } from '../model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  token: string = '';
  isLogged: boolean = false;
  loggedUser: User = {
    id: -1,
    firstName: '',
    lastName: '',
    email: '',
    permissions: [''],
  };
  private readonly mainApi = environment.mainApi;

  constructor(private httpClient: HttpClient, private router: Router) {
    if (localStorage.getItem('token') != null) {
      var newToken = localStorage.getItem('token');
      if (newToken) this.token = newToken;
      this.isLogged = true;
      var user = localStorage.getItem('loggedUser');
      if (user) this.loggedUser = JSON.parse(user);
    }
  }

  login(email: string, password: string) {
    let params = {
      email,
      password,
    };
    this.httpClient.post<Token>(`${this.mainApi}/auth/login`, params).subscribe(
      (data) => {
        localStorage.setItem('token', data.token);
        localStorage.setItem('loggedUser', JSON.stringify(data.user));
        this.token = data.token;
        this.isLogged = true;
        this.loggedUser = data.user;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('loggedUser');
    this.isLogged = false;
    this.token = '';
    this.loggedUser = {
      id: -1,
      firstName: '',
      lastName: '',
      email: '',
      permissions: [''],
    };
    this.router.navigate(['/']);
  }

  checkPermissions(permission: string) {
    if (!this.isLogged) {
      alert('You must log in to access this page.');
      this.router.navigate(['/']);
    } else if (!this.hasPermission(permission)) {
      alert("You don't have the permission to access this page.");
      this.router.navigate(['/']);
    } else if (localStorage.getItem('token') != null) {
      let parts = this.token.split('.');
      let claims = JSON.parse(atob(parts[1]));
      if (new Date() < claims.exp) {
        alert('The session has expired. Please log in again.');
        this.logout();
      }
    }
  }

  hasPermission(permission: string) {
    return this.loggedUser.permissions.indexOf(permission) > -1;
  }

  getToken() {
    return this.token;
  }
}
